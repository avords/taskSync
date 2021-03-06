package cn.com.flaginfo.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.flaginfo.util.DateUtil;

public class CommonDao {

	private String dbAlias;

	public CommonDao(String dbAlias) {
		this.dbAlias = dbAlias;
	}

	public String getDbAlias() {
		return dbAlias;
	}

	public void setDbAlias(String dbAlias) {
		this.dbAlias = dbAlias;
	}

	public List<Map> executeQuery(String sqlx, Object[] args) {
		List<Map> list = new ArrayList<Map>();
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHolder.getConnection(dbAlias).prepareStatement(sqlx);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData mdata = stmt.getMetaData();
			Calendar cal = Calendar.getInstance();
			int columnCount = mdata.getColumnCount();
			Map<String, String> fieldMap = new HashMap(columnCount);
			Map typeMap = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				String columnName = mdata.getColumnLabel(i);
				if (columnName.equals("rownum_".toUpperCase())) {
					continue;
				}
				fieldMap.put(columnName, DBFieldFmtHepler.changeColumnToFieldName(columnName));
				typeMap.put(columnName, mdata.getColumnType(i));
			}
			Set<String> keySet = fieldMap.keySet();
			while (rs.next()) {
				Map<String, String> result = new HashMap<String, String>();
				list.add(result);
				for (String key : keySet) {
					String val = null;
					String fieldName = fieldMap.get(key);
					int type = (Integer) typeMap.get(key);
					if (type == java.sql.Types.TIMESTAMP || type == java.sql.Types.DATE) {
						Timestamp t = rs.getTimestamp(key);
						Date o = null;
						if (t != null) {
							Long time = t.getTime();
							cal.setTimeInMillis(time);
							o = cal.getTime();
						}
						if (o == null) {
							val = "";
						} else {
							val = DateUtil.fmtDate(o);
						}
					} else {
						Object o = rs.getObject(key);
						if (o == null) {
							val = "";
						} else {
							val = o.toString();
						}
					}
					result.put(fieldName, val);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询表中字段信息
	 * @param sqlx
	 * @param args
	 * @return
	 */
	public Map<String, Map> executeColumnQuery(String sqlx) {
		Map<String, Map> fieldMap = null;
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHolder.getConnection(dbAlias).prepareStatement(sqlx);
			stmt.executeQuery();
			ResultSetMetaData mdata = stmt.getMetaData();
			fieldMap = new HashMap(mdata.getColumnCount());
			for (int i = 1; i <= mdata.getColumnCount(); i++) {
				if (mdata.getColumnName(i).equals("rownum_".toUpperCase())) {
					continue;
				}
				Map<String, Object> detailMap = new HashMap();
				detailMap.put("dataType", mdata.getColumnType(i));
				detailMap.put("dataTypeName", mdata.getColumnTypeName(i));
				detailMap.put("columnName", mdata.getColumnName(i).toLowerCase());
				fieldMap.put(mdata.getColumnName(i).toLowerCase(), detailMap);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return fieldMap;

	}

	public Map<String, String> findUniqueResultAndCloseStmt(String sql, Object[] args, Map fieldAlias) throws Exception {
		Calendar cal = Calendar.getInstance();
		Map<String, String> result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHolder.getConnection(dbAlias).prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			rs = stmt.executeQuery();
			ResultSetMetaData mdata = stmt.getMetaData();
			result = new HashMap<String, String>();
			if (rs.next()) {
				for (int i = 1; i <= mdata.getColumnCount(); i++) {
					String columnName = mdata.getColumnLabel(i);
					if (columnName.equals("rownum_".toUpperCase())) {
						continue;
					}
					String val = null;
					if (mdata.getColumnType(i) == java.sql.Types.DATE) {
						Timestamp t = rs.getTimestamp(columnName);
						Date o = null;
						if (t != null) {
							Long time = t.getTime();
							cal.setTimeInMillis(time);
							o = cal.getTime();
						}
						if (o == null) {
							val = "";
						} else {
							val = DateUtil.fmtDate(o);
						}
					} else {
						Object o = rs.getObject(columnName);
						val = o == null ? null : o.toString();
					}
					String fieldAliasStr = columnName;
					fieldAliasStr = DBFieldFmtHepler.changeColumnToFieldName(columnName);
					result.put(fieldAliasStr, val);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw (e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return result;
	}

	public void executeUpdate(String sql, Object[] args) {
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHolder.getConnection(dbAlias, false).prepareStatement(sql);
			for (int i = 0; args != null && i < args.length; i++) {
				stmt.setObject(i + 1, args[i]);
			}
			stmt.addBatch();
			stmt.executeBatch();
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					//e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

}
