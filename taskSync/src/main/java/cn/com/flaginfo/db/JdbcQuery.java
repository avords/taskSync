package cn.com.flaginfo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.com.flaginfo.support.PartialCollection;
import cn.com.flaginfo.support.QueryInfo;
/**
 * 仿照Hibernate中query，目标是代码结构查询简单，方便易用
 * 1.简化sql条件拼接
 * 2.封装查询
 * @author Rain
 *
 */
public class JdbcQuery implements Query{
	private Logger logger = Logger.getLogger(getClass());
	protected static final Pattern PATTERN_WHERE = Pattern
			.compile("\\s+(where|and|or|start\\s+with)[\\s\\(]+\\S+\\s*([>|=|<]{1,2}|(like)|in|not\\s+in)\\s*:([\\S&&[^\\(\\)]]+)|\\s+(where|and|or)\\s+instr\\s*\\(\\s*\\S+\\s*,\\s*:(\\S+)\\s*\\)\\s*[><=]{1,2}\\s*\\d");
	protected static final Pattern PATTERN_SET = Pattern.compile("(\\s+set|,)\\s*(?:\\w+\\.)?\\w+\\s*=\\s*:(\\w+)");
	protected static final Pattern PATTERN_FUNC = Pattern.compile("(?<=\\(|,)\\s*:(\\w+)");
	
	//private static final Pattern PATTERN_LIKE = Pattern.compile("\\s+(where|and|or)\\s+(\\S+)\\s*like+\\s*:(\\S+)");
	//\\s*(set|,)\\s*\\S+=\\s*:([\\S&&[^,]]+)
	//private static final Pattern PATTERN_INSTR = Pattern.compile("\\s+(where|and|or)\\s+instr\\s*\\(\\s*\\S+\\s*,\\s*:(\\S+)\\s*\\)\\s*[><=]{1,2}\\s*\\d");
	//private static final Pattern PATTERN_DATE = Pattern.compile("\\s+(where|and|or)\\s+\\S+\\s*[>|=|<]{1,2}\\s*to_date\\s*\\(\\s*:(\\S+)\\s*,\\S+\\)\\s*");
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String s = "update im_sp_info set sp_name=:name,sp_code=:spCode,create_time=:createTime "
				+ " where id not in (select task_id from im_task_audit_assign) and (sp_code = :spCode or id = :spId) and sp_name=:spName";
		JdbcQuery query = new JdbcQuery(null,s);
		//query.setDate("createTime", "2013-12-01 00:00:00","yyyy-MM-dd hh24:mi:ss");
		//query.setDate("createTime1", "2013-12-01 00:00:00","yyyy-MM-dd hh24:mi:ss");
		query.setParameter("spId", 123);
		query.setParameter("spName", "rain");
		query.setParameter("spCode", "00001");
		query.setDate("createTime","2014-04-11","yyyy-MM-dd");
		//query.setIn("sp_name", "rain","okkk");
		String sql = query.parseSql();
		
		System.out.println("sql="+sql+";\n args:"+query.args);
		
		

		/*Pattern pattern = Pattern.compile("\\s+(where|and|or)\\s+\\S+\\s*([>|=|<]{1,2}|(like))\\s*:(\\S+)|\\s+(where|and|or)\\s+instr\\s*\\(\\s*\\S+\\s*,\\s*:(\\S+)\\s*\\)\\s*[><=]{1,2}\\s*\\d");
		*/
		Matcher m = PATTERN_SET.matcher(" set sp_name=:name,sp_code=:spCode");
		while(m.find()){
			System.out.println(m.group(0));
			System.out.println(m.group(1));
			System.out.println(m.group(2));
			//System.out.println(m.group(3));
			//System.out.println(m.group(4));
			//System.out.println(m.group(5));
			//System.out.println(m.group(6));
		}
		
		
	}
	
	private CommonDao jdbcDao;
	private QueryInfo queryInfo;
	
	//参数
	private Map paramsMap = new HashMap();
	private Map dateMap = new HashMap();
	private Map inMap = new HashMap();
	
	private List args = new ArrayList();
	
	private List<String> orderByList = new ArrayList();
	
	private StringBuffer orderBy = new StringBuffer();
	
	private StringBuffer sql = new StringBuffer();
	
	public JdbcQuery(String dbAlias,String startSql) {
		this.jdbcDao = new CommonDao(dbAlias);
		sql.append(startSql);
	}
	

	public Query setQueryInfo(QueryInfo queryInfo) {
		this.queryInfo = queryInfo;
		return this;
	}

	public Query setParameter(String key,Object value){
		this.setParameter(key, value,false);
		return this;
	}
	
	public Query setParameter(String key,Object value,boolean required){
		if(required && value==null){
			throw new RuntimeException("sql parameter:"+key+" is null");
		}
		this.paramsMap.put(key, value);
		return this;
	}
	
	public Query setIn(String key,String ...values){
		return setIn(key, false, values);
	}
	
	
	public Query setIn(String key,boolean required,String ...values){
		if(values==null || values.length==0){
			return this;
		}
		List inList = new ArrayList(values.length);
		for(String value:values){
			if(value==null || "".equals(value)){
				continue;
			}
			inList.add(value);
		}
		if(inList.size()>0){
			//this.paramsMap.put(key,inList);
			inMap.put(key, inList);
			setParameter(key, values,required);
		}
		return this;
	}
	
	public Query setOrderBy(String columnName,String order){
		if(order==null){
			order= "asc";
		}
		orderByList.add(columnName +" "+ order);
		return this;
	}
	
	public Query setLike(String key,String value,String prefix,String suffix){
		if(value==null || "".equals(value)){
			return this;
		}
		if(prefix==null)prefix="";
		if(suffix==null)suffix="";
		this.paramsMap.put(key, prefix+value+suffix);
		return this;
	}
	
	public Query setDate(String key,String date,String dateFormat){
		if(date!=null && !"".equals(date)){
			//date = "to_date('"+date+"',"+dateFormat+"')";
			dateMap.put(key, dateFormat);
		}
		setParameter(key, date);
		return this;
	}
	
	public Query setDate(String key,String date){
		setDate(key, date, "yyyy-MM-dd hh24:mi:ss");
		return this;
	}
	
	
	public String parseSql(){
		String finalSql = sql.toString();
		finalSql = finalSql.replaceAll("\\s{2,}", " ");
		
		for(String order:orderByList){
			if("".equals(orderBy.toString())){
				orderBy.append(" order by ").append(order);
			}else{
				orderBy.append(",").append(order);
			}
		}
//		finalSql = finalSql+orderBy;
		
		Matcher set = PATTERN_SET.matcher(finalSql);
		while(set.find()){
			String conds = set.group();
			String key = set.group(2);
			Object value = this.paramsMap.get(key);
			String replace="?";
			String dbType ="";
			if(dateMap.get(key)!=null){
                dbType = JDBCMessage.getString("db_sms.jdbc.driver");
                if (dbType.contains("mysql")) {
                    replace = "?";   
                }else {
                    if(dbType.contains("oracle")||dbType.contains(".sqlserver.jdbc.")){
                        replace = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
                    }else if(dbType.contains("jdbc.sqlserver")){
                        replace ="cast(? as datetime)";
                    }
                }
                logger.debug("dbType=" + dbType);
            }
			replace = conds.replace(":"+key,replace);
			finalSql = finalSql.replaceAll(conds,replace);
			args.add(value);
		}
		
		Matcher where = PATTERN_WHERE.matcher(finalSql);
		while(where.find()){
			String conds = where.group();
			String key = null;
			if(where.group(4)!=null){
				key = where.group(4);
			}else if(where.group(6)!=null){
				key = where.group(6);
			}else{
				throw new RuntimeException("parse sql exception,can't find the key");
			}
			//String key = nm.group(2);
			conds = conds.replaceAll("\\s*where\\s+","").replaceAll("\\s*(and|or)\\s+\\(","");
			//System.out.println("nm conds="+conds+";key="+key);
			Object value = this.paramsMap.get(key);
			if(value==null || "".equals(value)){
				finalSql = finalSql.replace(conds,"");
			}else{
				String replace = " ? ";
				String dbType = "";
				if(dateMap.get(key)!=null){
				    dbType = JDBCMessage.getString("db_sms.jdbc.driver");
				    if (dbType.contains("mysql")) {
				        replace = "?";   
				    }else {
				        if(dbType.contains("oracle")||dbType.contains(".sqlserver.jdbc.")){
				            replace = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
				        }else if(dbType.contains("jdbc.sqlserver")){
				            replace ="cast(? as datetime)";
				        }
				    }
			        logger.debug("dbType=" + dbType);
				}
				if(inMap.get(key)!=null){
					List<String> inList = (List)inMap.get(key);
					replace = "(";
					for(String in:inList){
						if(replace.equals("(")){
							replace = replace+"?";
						}else{
							replace = replace+",?";
						}
						args.add(in);
					}
					replace = replace+")";
					
				}else{
					args.add(value);
				}
				finalSql = finalSql.replaceFirst(":"+key,replace);
			}
		}
		
		//finalSql = finalSql.toLowerCase();
		finalSql = finalSql.replaceAll("where\\s+\\)", "where ").replaceAll("where\\s+and", " where ")
				.replaceAll("where\\s*$", "").replaceAll("where\\s+order\\s+", " order ")
				.replaceAll("(and|or)\\s+\\(\\s*\\)\\s*", " ").replaceAll("\\s+\\(\\s+(and|or)\\s+", " ( ");
		logger.debug(Thread.currentThread().getName()+"===="+finalSql+";args="+args);
		return finalSql;
	}
	
	/**
	 * 执行分页查询
	 */
	@Override
	public PartialCollection listPartial() {
		
		long start = System.currentTimeMillis();
		String finalSql = this.parseSql();
		String countSql = "select count(1) C from (" + finalSql + ")";
		
		Map countMap = null;
		try {
			countMap = jdbcDao.findUniqueResultAndCloseStmt(countSql, args.toArray(), null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		int total = Integer.valueOf((String)countMap.get("c"));
		if(countMap==null || countMap.size()==0 || total==0){
			return new PartialCollection(null,total,this.queryInfo.getOffset());
		}
		finalSql = finalSql+orderBy.toString();
		String querySql = "select * from (select * from (select row_.*, rownum rownum_ from ("
				+ finalSql + ") row_  ) a where Upper##% )where Lower##% ";
		if (queryInfo.getOffset() == null
				&& queryInfo.getLimit() == null) {
			querySql = "select row_.*, rownum rownum_ from (" + querySql
					+ ") row_";
		} else if (queryInfo.getOffset() != null
				&& queryInfo.getLimit() == null) {
			int initIndx = queryInfo.getOffset() == null ? 0
					: queryInfo.getOffset().intValue();
			querySql = querySql.replace("Upper##%", "1=1");
			querySql = querySql.replace("Lower##%", "rownum_ >" + initIndx);
		} else if (queryInfo.getOffset() == null
				&& queryInfo.getLimit() != null) {
			int limit = queryInfo.getLimit() == null ? 0 : queryInfo
					.getLimit().intValue();
			querySql = querySql.replace("Upper##%", "a.rownum_ <=" + limit);
			querySql = querySql.replace("Lower##%", "rownum_ > 0");
		} else {
			int initIndx = queryInfo.getOffset() == null ? 0
					: queryInfo.getOffset().intValue();
			int limit = queryInfo.getLimit() == null ? 0 : queryInfo
					.getLimit().intValue();
			querySql = querySql.replace("Upper##%", "a.rownum_ <="
					+ (limit + initIndx));
			querySql = querySql.replace("Lower##%", "rownum_ > " + initIndx);
		}
		List<Map> list = jdbcDao.executeQuery(querySql, args.toArray());
		return new PartialCollection(list,total,this.queryInfo.getOffset());
		
	}
	
	/**
	 * 执行查询
	 */
	@Override
	public List<?> list() {
		//long start = System.currentTimeMillis();
		String finalSql = this.parseSql();
		finalSql = finalSql+orderBy.toString();
		//logger.info("execute query parse time:"+(System.currentTimeMillis()-start)+"ms");
		List<Map> list = jdbcDao.executeQuery(finalSql, args.toArray());
		//logger.info("execute query db time1:"+(System.currentTimeMillis()-start)+"ms");
		return list;
	}
	
	/**
	 * 获取唯一结果
	 */
	@Override
	public Map<String,String> uniqueResult(){
		List<Map> list = (List<Map>)list();
		if(list!=null && list.size()>0){
			Map result = list.get(0);
			return result;
		}
		return null;
	}
	
	@Override
	public <T> T uniqueResult(Class clazz) {
		Map resultMap = uniqueResult(); 
		if(resultMap!=null){
			
			Set<String> set = resultMap.keySet();
			Object value = null;
			for(String key:set){
				value = resultMap.get(key);
			}
			if(clazz.getName().equals("java.lang.String")){
				return (T)(value == null ||"".equals(value)?null:value.toString());
			}else if(clazz.getName().equals("java.lang.Integer")){
				return (T)new Integer((value == null ||"".equals(value)?"0":value.toString()));
			}else if(clazz.getName().equals("java.lang.Double")){
				return (T)new Double((value == null ||"".equals(value) ?"0.0":value.toString()));
			}else if(clazz.getName().equals("java.lang.Long")){
				return (T)new Long((value == null ||"".equals(value) ?"0":value.toString()));
			}
		}
		return null;
	}
	
	/**
	 * 执行update
	 */
	@Override
	public void executeUpdate() {
		String finalSql = this.parseSql();
		jdbcDao.executeUpdate(finalSql, args.toArray());
	}

	
	public void executeUpdate(Object args[]) {
		String finalSql = sql.toString();
		logger.debug(finalSql);
		jdbcDao.executeUpdate(finalSql, args);
	}
	
	
	
}
