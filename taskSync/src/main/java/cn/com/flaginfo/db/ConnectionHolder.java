package cn.com.flaginfo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class ConnectionHolder {
	
	private static ThreadLocal<Map> threadLocal = new ThreadLocal<Map>();
	private static Logger logger = Logger.getLogger(ConnectionHolder.class);
	/**
	 * 获取本次请求的对应的连接
	 * @param dbAlias
	 * @return
	 */
	public static Connection getConnection(String dbAlias,boolean isCommit){
		Map<String,Connection> poolMap = (Map<String,Connection>)threadLocal.get();
		if(poolMap==null){
			poolMap = new HashMap<String,Connection>();
			threadLocal.set(poolMap);
		}
		Connection conn = poolMap.get(dbAlias);
		if(conn==null){
			conn = ConnectionFactory.getInstance().getConnection(dbAlias);
			poolMap.put(dbAlias, conn);
		}
		try {
			if(!isCommit && conn.getAutoCommit()){
				conn.setAutoCommit(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 获取自动提交的Connection
	 * @param dbAlias
	 * @return
	 */
	public static Connection getConnection(String dbAlias){
		return getConnection(dbAlias, true);
	}
	
	/**
	 * 获取本次请求所有的连接
	 * @return
	 */
	public static Map<String,Connection> get(){
	
		Map<String,Connection> poolMap = (Map<String,Connection>)threadLocal.get();
		return poolMap;
	
	}
	/**
	 * 提交事务
	 */
	public static void commit(){
		Map<String,Connection> poolMap = get();
		if(poolMap == null){
			return;
		}
		Set<String> set = poolMap.keySet();
		for(String key:set){
			Connection conn = poolMap.get(key);
			try {
				if(!conn.getAutoCommit()){
					conn.commit();
					logger.info("commit dbAlias:"+key);
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 回滚
	 */
	public static void rollback(){
		Map<String,Connection> poolMap = get();
		if(poolMap == null){
			return;
		}
		Set<String> set = poolMap.keySet();
		for(String key:set){
			Connection conn = poolMap.get(key);
			try {
				if(conn!=null && !conn.getAutoCommit()){
					logger.info(key+" rollback");
					conn.rollback();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public static void colse(){
		Map<String,Connection> poolMap = get();
		if(poolMap == null){
			return;
		}
		Set<String> set = poolMap.keySet();
		for(String key:set){
			Connection conn = poolMap.get(key);
			try {
				if(!conn.getAutoCommit()){
					conn.setAutoCommit(true);
				}
				conn.close();
				logger.info(key+" colse");
				conn = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void colseAndRemove(){
		colse();
		remove();
	}
	
	public static void colseAutoSubmit(){
		Map<String,Connection> poolMap = get();
		if(poolMap == null){
			return;
		}
		Set<String> set = poolMap.keySet();
		for(String key:set){
			Connection conn = poolMap.get(key);
			try {
				if(!conn.getAutoCommit()){
					continue;
				}
				conn.close();
				poolMap.remove(key);
				logger.info(key+" colse");
				conn = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void remove(){
		threadLocal.remove();
	}
	
	
}
