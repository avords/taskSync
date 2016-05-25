package cn.com.flaginfo.action;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import cn.com.flaginfo.db.ConnectionHolder;
import cn.com.flaginfo.manager.TaskManger;
import cn.com.flaginfo.support.ManagerFactory;
import cn.com.flaginfo.util.SystemMessage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SendTaskSyncAction1 {
	private Logger logger = Logger.getLogger(SendTaskSyncAction1.class);
	private static TaskManger manager = ManagerFactory.getInstance(TaskManger.class);
	private static int THREAD_NUMBER = Runtime.getRuntime().availableProcessors()+1;
	private static ExecutorService MDN_THREAD_POOL = Executors.newFixedThreadPool(THREAD_NUMBER);
	
	public static void main(String[] args) {
		SendTaskSyncAction1 action = new SendTaskSyncAction1();
		action.execute();
		MDN_THREAD_POOL.shutdown();
	}
	
	public void execute(){

//		查询任务：
//		http://10.0.0.27:9041/jsonServer/cmc/task/list 
//		参数：{"beginTime":"2016-01-20 00:00:00","type":"id","endTime":"2016-01-20 23:59:59","id":"22451346189"}
//		返回值：{"returnCode":"200","list":[{"total":"7","succ":"28","arriveFail":"0","arriveSucc":"28","status":"4","begintime":"2016-01-19 11:03:49","taskType":"1","nmid":"22441982192","productId":"1","allnumber":"28","createtime":"2016-01-19 11:03:49","id":"22451346189","fail":"0","imContent":"","description":"","netmessageType":"1","userName":"SSD"}],"dataCount":1}
		
		String sql = "select t.*, s.sp_code from (select distinct portal_task_id id, task_id, sp_id from im_portal_task where task_id in(select * from (select id from im_send_task_summary t where t.begintime>sysdate-4-"+SystemMessage.getString("limitDay")+" and t.t.begintime<=sysdate-"+SystemMessage.getString("limitDay")+"and t.allnumber>nvl(succ,0)+nvl(fail,0) order by t.begintime desc) where rownum<=10000)) t left join im_sp_info s on t.sp_id = s.id ";
		List<Map> list = manager.getList(sql, "db_sms");
		if(list==null || list.size()<1){
			return;
		}
		for(Map m : list){
		    try{
			    httpHandler((String)m.get("id"), (String)m.get("taskId"), SystemMessage.getString((String)m.get("spCode")));
		    }catch(Exception e){
		        logger.error("portTaskId:"+(String)m.get("id")+";taskId:"+(String)m.get("taskId")+"---Synchronous data appear abnormal");
		    }
		}
		
	}
	
	private void httpHandler(String portTaskId, String taskId, String spId){
		
		String sql = "select id, nvl(succ,0)+nvl(fail,0) total from im_send_task_summary where id in(select task_id from im_portal_task where portal_task_id ="+portTaskId+")";
		List<Map> result = manager.getList(sql, "db_sms");
		int total = 0;
		for(Map m : result){
			total = Integer.parseInt((String)m.get("total"));
		}
		
		JSONObject json = new JSONObject();
		json.put("type", "id");
		json.put("id", portTaskId);
		String res = manager.post(SystemMessage.getString("apiServer")+"cmc/task/list", json.toJSONString(), spId);
		logger.info("server response: "+res);
		Map resMap = JSONObject.parseObject(res, Map.class);
		String code = (String) resMap.get("returnCode");
		if("200".equals(code)){
			JSONArray arr = (JSONArray) resMap.get("list");
			for(int i =0; i < arr.size(); i++){
				JSONObject obj = arr.getJSONObject(i);
				String pTotal = obj.getString("total");
				String succ = obj.getString("succ");
				String arriveFail = obj.getString("arriveFail");
				String arriveSucc = obj.getString("arriveSucc");
				String status = obj.getString("status"); 
				String allnumber = obj.getString("allnumber");
				String fail = obj.getString("fail"); 
				String portalId = obj.getString("id");
				Map setMap = new HashMap();
				setMap.put("total", pTotal);
				setMap.put("succ", succ);
				setMap.put("arriveFail", arriveFail);
				setMap.put("arriveSucc", arriveSucc);
				setMap.put("status", status);
				setMap.put("allnumber", allnumber);
				setMap.put("fail", fail);
				
				if(total<(Integer.parseInt(succ)+Integer.parseInt(fail))){
					manager.update(setMap, "im_send_task_summary", "id", taskId, "db_sms");
					
					json = new JSONObject();
					JSONObject pageInfo = new JSONObject();
					pageInfo.put("curPage", 1);
					pageInfo.put("pageLimit", 10);
					json.put("taskid", portalId);
					int currentPage = 1;
					
					String mdnRes = manager.post(SystemMessage.getString("apiServer")+"cmc/task/list/mdn", json.toJSONString(), spId);
					logger.info("server response: "+mdnRes);
					Map mdnMap = JSONObject.parseObject(mdnRes, Map.class);
					MdnHandler mdnTask = new MdnHandler(taskId, mdnMap);
					MDN_THREAD_POOL.submit(mdnTask);
					if("200".equals((String)mdnMap.get("returnCode"))){
						Integer dataCount = (Integer)mdnMap.get("dataCount");
						int pageCount = (dataCount+9)/10;
						
						while(currentPage<pageCount){
							currentPage++;
							json.put("curPage", currentPage);
							mdnRes = manager.post(SystemMessage.getString("apiServer")+"cmc/task/list/mdn", json.toJSONString(), spId);
							logger.info("server response: "+mdnRes);
							MdnHandler mdnTaskH = new MdnHandler(taskId, JSONObject.parseObject(mdnRes, Map.class));
							MDN_THREAD_POOL.submit(mdnTaskH);
						}
					}
				}
			}
		}
	}
	
	class MdnHandler implements Runnable{
		private String taskId;
		private Map resultMap;
		public MdnHandler(String taskId, Map resultMap){
			this.taskId = taskId;
			this.resultMap = resultMap;
		}
		@Override
		public void run() {
			if("200".equals((String)resultMap.get("returnCode"))){
				List<Map> mdnList = (List<Map>) resultMap.get("list");
				String sql = "update im_send_mdn set recieved=?, arrived=?, recieved_time=to_date(?,'yyyy-MM-dd hh24:mi:ss'), arrived_time=to_date(?,'yyyy-MM-dd hh24:mi:ss') where task_id=? and mdn=? and (recieved=-1 or arrived=-1)";
				PreparedStatement pstm = null;
				try {
					pstm = ConnectionHolder.getConnection("db_sms", false).prepareStatement(sql);
					for(Map m : mdnList){
						
						String mdn = (String)m.get("MDN");
						String arrivResult = (String)m.get("REPORTRESULT");
						String submitResult = (String)m.get("SUBMITRESULT");
						String submitTime = (String)m.get("SUBMITTIME");
						String arriveTime = (String)m.get("REPORTTIME");
						pstm.setObject(1, submitResult);
						pstm.setObject(2, arrivResult);
						pstm.setObject(3, submitTime);
						pstm.setObject(4, arriveTime);
						pstm.setObject(5, taskId);
						pstm.setObject(6, mdn);
						pstm.addBatch();
						logger.info("add batch mdn: "+mdn);
					}
					logger.info("mdntask: "+taskId);
					pstm.executeBatch();
					ConnectionHolder.getConnection("db_sms", false).commit();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						ConnectionHolder.getConnection("db_sms", false).rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} finally{
					try {
						pstm.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

}
