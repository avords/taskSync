package cn.com.flaginfo.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xichen
 */
@SuppressWarnings({"unchecked","serial"})
public class QueryInfo implements Serializable {

	/**
	 * ORDER BY clause without 'ORDER BY' keyword
	 */
	private String orderByClause = new String();

	/**
	 * Number of records to include in result set
	 */
	private Integer limit;

	/**
	 * Value to start counting records from
	 */
	private Integer offset;

	/**
	 * Map of queryParameters to put in query
	 */
	private Map queryParameters = Collections.synchronizedMap(new HashMap());

	/**
	 * Creates new instance of QueryInfo
	 */
	public QueryInfo() {
		queryParameters = Collections.synchronizedMap(new HashMap());
	}

	/**
	 * Creates new instance of QueryInfo
	 * 
	 * @param whereClause
	 *            WHERE clause without 'WHERE' keyword
	 * @param orderByClause
	 *            ORDER BY clause without 'ORDER BY' keyword
	 * @param limit
	 *            Number of records to include in result set
	 * @param offset
	 *            Value to start counting records from
	 */
	public QueryInfo(String orderByClause, Integer limit, Integer offset) {
		this.orderByClause = orderByClause;
		this.limit = limit;
		this.offset = offset;
		queryParameters = Collections.synchronizedMap(new HashMap());
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Map getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(Map queryParameters) {
		this.queryParameters = queryParameters;
	}
	
	public QueryInfo getQueryInfo(String pagesize, String offset){
		QueryInfo queryInfo = new QueryInfo();
		if(pagesize!=null && !"".equals(pagesize)){
			queryInfo.setLimit(Integer.parseInt(pagesize)); 
		}
		if(offset!=null && !"".equals(offset)){
			queryInfo.setOffset(Integer.parseInt(offset)); 
		}
		
		return queryInfo;
	}
	
}
