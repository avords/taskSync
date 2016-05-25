package cn.com.flaginfo.manager;

import com.alibaba.fastjson.JSONObject;

/**
 * 任务同步
 * @author liusm
 *
 */
public class TaskManger extends BaseManager {

	public static void main(String[] args) {
		TaskManger manager = new TaskManger();
		JSONObject json = new JSONObject();
		json.put("type", "id");
		json.put("id", "203436536941");
		String res = manager.post("http://192.168.20.80:9041/jsonServer/cmc/task/list", json.toJSONString(), "2034001462");
		System.out.println(res);
	}
}
