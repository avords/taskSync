package cn.com.flaginfo.manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.com.flaginfo.db.DBFieldFmtHepler;
import cn.com.flaginfo.db.JDBCMessage;
import cn.com.flaginfo.db.JdbcQuery;
import cn.com.flaginfo.db.Query;
import cn.com.flaginfo.support.PartialCollection;
import cn.com.flaginfo.support.UserInfo;
import cn.com.flaginfo.support.UserInfoHolder;
import cn.com.flaginfo.support.WebClient;
import cn.com.flaginfo.util.DateUtil;
import cn.com.flaginfo.util.StringUtil;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 基础类（发送请求）
 * @author Rain
 *
 */
public abstract class BaseManager{
    
    protected Logger logger = Logger.getLogger(getClass());
    
    //public static final String STM_URL = SystemMessage.getString("stm_url");

    public static final String RETURN_VALUE = "returnValue";
    public static final String RETURN_CODE = "returnCode";
    
    protected static Client client = new Client();
    protected static WebClient webClient = new WebClient();
    /**
     * 创建查询Query
     * @param sql
     * @param dbAlias
     * @return
     */
    public Query createJdbcQuery(String sql,String dbAlias){
        return new JdbcQuery(dbAlias, sql);
    }
    
    
    /**
     * 获取ID,无前缀
     * @param sequenceName
     * @param dbAlias
     * @return
     */
    public long getSequenceId(String sequenceName,String dbAlias){
        Map<String,String> idMap = createJdbcQuery("select "+sequenceName+".nextval id from dual",dbAlias).uniqueResult();
        String id = idMap.get("id");
        return Long.valueOf(id);
    }
    
    public List getList(String sql,String dbAlias){
    	return createJdbcQuery(sql, dbAlias).list();
    }
    
    
    /**
     * 保存数据
     * @param condsMap
     * @param tableName
     * @param sequenceName
     * @param platform
     * @return
     */
    public long save(Map condsMap,String tableName,String sequenceName,String dbAlias){
        
        long id = -1;
        if(sequenceName!=null){
            id = getSequenceId(sequenceName,dbAlias);
            condsMap.put("id", id);
        }
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ").append(tableName).append("(");
        Set<String> keySet = condsMap.keySet();
        boolean isFirst=true;
        List args = new ArrayList(condsMap.size()); 
        StringBuffer valuesSql = new StringBuffer();
        for(String key:keySet){
            String replace = "?";
            Object value = condsMap.get(key);
            key = DBFieldFmtHepler.changeFieldToColumnName(key);
            if(value!=null){
                if(value instanceof Date){
                    replace = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
                    value = DateUtil.fmtDate((Date)value);
                }
            }
            args.add(value);
            if(isFirst){
                isFirst = false;
                sql.append(key);
                valuesSql.append(replace);
            }else{
                sql.append(",").append(key);
                valuesSql.append(",").append(replace);
            }
        }
        sql.append(") values (").append(valuesSql).append(")");
        Query query = createJdbcQuery(sql.toString(),dbAlias);
        query.executeUpdate(args.toArray());
        return id;
    }
    
    /**
     * 更新记录
     * @param setMap
     * @param tableName
     * @param fieldName
     * @param id
     * @param dbAlias
     */
    public void update(Map setMap,String tableName,String condFieldName,String condFieldValue,String dbAlias){
        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(tableName);
        Set<String> keySet = setMap.keySet();
        List args = new ArrayList();
        boolean isFirst = true;
        for(String key:keySet){
            String replace = "?";
            Object value = setMap.get(key);
            if(value!=null && value instanceof java.util.Date){
                String dbType = JDBCMessage.getString("db_sms.jdbc.driver");
                if (dbType.contains("mysql")) {
                    replace = "?";   
                }else {
                    if(dbType.contains("oracle")||dbType.contains(".sqlserver.jdbc.")){
                        replace = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
                    }else if(dbType.contains("jdbc.sqlserver")){
                        replace ="cast(? as datetime)";
                    }
                }
                logger.info("dbType=" + dbType);
                value = DateUtil.fmtDate((Date)value);
            }
            args.add(value);
            key = DBFieldFmtHepler.changeFieldToColumnName(key);
            if(isFirst){
                sql.append(" set ").append(key).append("=").append(replace);
                isFirst = false;
            }else{
                sql.append(",").append(key).append("=").append(replace);
            }
        }
        sql.append(" where ").append(DBFieldFmtHepler.changeFieldToColumnName(condFieldName)).append("=?");
        args.add(condFieldValue);
        createJdbcQuery(sql.toString(), dbAlias).executeUpdate(args.toArray());
        
    }
    
    /**
     * 获取一条记录
     * @param condField 条件名称
     * @param condFieldValue 条件值
     * @param tableName 表名
     * @param dbAlias 数据库别名
     * @param fieldName 为空表示获取所有的数据库字段的值
     * @return
     */
    public Map getObject(String condField,String condFieldValue,String tableName,String dbAlias,String ...fieldName){
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        if(fieldName!=null && fieldName.length>0){
            boolean isFirst = true;
            for(String field:fieldName){
                if(isFirst){
                    sql.append(DBFieldFmtHepler.changeFieldToColumnName(field));
                    isFirst = false;
                }else{
                    sql.append(",").append(DBFieldFmtHepler.changeFieldToColumnName(field));
                }
            }
        }else{
            sql.append(" * ");
        }
        sql.append(" from ").append(tableName).append(" where ");
        sql.append(DBFieldFmtHepler.changeFieldToColumnName(condField)).append("=:").append(condField);
        return createJdbcQuery(sql.toString(), dbAlias).setParameter(condField, condFieldValue).uniqueResult();
    
    }
    
    /**
     * 获取一条记录,通过主键
     * @param condFieldValue 条件值
     * @param tableName 表名
     * @param dbAlias 数据库别名
     * @param fieldName 获取所有的数据库字段的值,如果为空表示获取所有的
     * @return
     */
    public Map getObjectById(String condFieldValue,String tableName,String dbAlias,String ...fieldName){
        return getObject("id", condFieldValue, tableName, dbAlias, fieldName);
    }
    

    /**
     * 物理删除一条记录
     * @param id
     * @param tableName
     * @param dbAlias
     */
    public void delete(String id,String tableName,String dbAlias){
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("id不能为空");
        }
        if (StringUtils.isBlank(tableName)) {
            throw new RuntimeException("tableName不能为空");
        }
        if (StringUtils.isBlank(dbAlias)) {
            throw new RuntimeException("dbAlias不能为空");
        }
        
         String sql = "delete from "+tableName+" where id=:id ";

         createJdbcQuery(sql,dbAlias)
         .setParameter("id", id,true)
         .executeUpdate();
    }
    
    
    
    
    
    
    /**
     * 请求jsonSever.
     * @param url
     * @param json
     * @return
     */
    public String post(String url,String json, String spId){
        WebResource r= client.resource(url);
        ClientResponse resp = null;
        if(spId!=null && !"".equals(spId)){
        	JSONObject sessionUser = new JSONObject();
        	sessionUser.put("spId", spId);
        	sessionUser.put("userId", "20371503049");
        	resp = r.header("sessionUser", sessionUser.toString()).post(ClientResponse.class, json);
        }else{
        	resp = r.post(ClientResponse.class, json);
        }
        String res = resp.getEntity(String.class);
        return res;
    }
    public String post(String url,String json){
    	return post(url, json, null);
    }
    
    /**
     * 请求jsonSever
     * @param url
     * @param requestMap
     * @return
     */
    public Map postJsonServerForMap(String url,Map<String,Object> requestMap){
        String json;
        try {
        	json = JSONObject.toJSONString(requestMap);
            logger.info("post:"+url+";params:"+json);
            String response = post(url,json);
            Map result = JSONObject.parseObject(response, Map.class);
            return result;
        } catch (Exception e) {
            logger.warn("请求"+url+",失败!");
            logger.warn(e.getMessage());
            return null;
        }
    }
    
    protected UserInfo getUserInfo(){
        return UserInfoHolder.get();
    }
    
    /**
     * 发送模拟表单请求
     * @param url 请求地址
     * @param reqMap    请求参数
     * @param isUserAuth
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String postForm(String url,Map<String,String> reqMap)  {
        Set<String> set = reqMap.keySet();
        MultivaluedMapImpl params = new MultivaluedMapImpl();
        for(String key:set){
            if(!StringUtil.isNullOrEmpty(reqMap.get(key))){
                params.add(key, reqMap.get(key));
            }
        }
        System.out.println("params= "+params+";url = "+url);
        WebResource resource = client.resource(url);
        
        String temp =  resource.type(MediaType.APPLICATION_FORM_URLENCODED).post(String.class,params);
        System.out.println(temp);
        try {
            temp = new String(temp.getBytes(),"gbk");;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
        System.out.println("result=   "+temp);
        return temp;
    }
    
    
    
    public <T> T postFormForObject(String url,Map reqMap,Class clazz) {
        String result = postForm(url,reqMap);
        return (T)JSONObject.parseObject(result, clazz);
    }
    
    
    public String postCanCode(String url,Map reqMap,String code){
        return  webClient.postForm(url, reqMap, code);
    }
}
