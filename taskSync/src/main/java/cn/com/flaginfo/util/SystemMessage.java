package cn.com.flaginfo.util;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * 配置
 * 1.先加载本地配置文件
 * 2.如果配置需要从远程读取,那么获取远程文件配置覆盖掉本地配置
 * @author Rain
 *
 */
public class SystemMessage {

	private final static Logger logger = Logger.getLogger(SystemMessage.class);
	private static volatile Properties properties = new Properties();
	
	//本地配置文件
	private static final String BUNDLE_NAME = "system";

	static{
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
		Enumeration<String> e = bundle.getKeys();
		while(e.hasMoreElements()){
			String key = e.nextElement();
			properties.put(key, bundle.getString(key));
		}
	}
	
	
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	
	/**
	 * 获取配置，并将配置值转换为整数
	 * 
	 * @param key
	 * @return
	 */
	public static Integer getInteger(String key) {
		String value = getString(key);
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为Long类型
	 * 
	 * @param key
	 * @return
	 */
	public static Long getLong(String key) {
		String value = getString(key);
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为Double类型
	 * 
	 * @param key
	 * @return
	 */
	public static Double getDouble(String key) {
		String value = getString(key);
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为Float类型
	 * 
	 * @param key
	 * @return
	 */
	public static Float getFloat(String key) {
		String value = getString(key);
		try {
			return Float.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为bool类型
	 * 
	 * @param key
	 * @return
	 */
	public static Boolean getBoolean(String key) {
		String value = getString(key);
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为BigDecimal类型
	 * 
	 * @param key
	 * @return
	 */
	public static BigDecimal getBigDecimal(String key) {
		String value = getString(key);
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return null;
		}
	}
}

