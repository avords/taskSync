package cn.com.flaginfo.util;


import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * StringUtil 字符串工具类.
 *
 * @author yunhu.duan
 */

public final class StringUtil {
	private static Logger logger = Logger.getLogger(StringUtil.class);
	/**
	 * 判断一个对象或者是字符串是否为空
	 * 
	 * @param obj
	 *            对象或字符串
	 * @return
	 */
	public static boolean isNullOrEmpty(final Object obj) {
		boolean result = false;
		if (obj == null || "null".equals(obj)
				|| "".equals(obj.toString().trim())) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 判断一个对象或者是字符串是否为空
	 * 
	 * @param obj
	 *            对象或字符串
	 * @return
	 */
	public static boolean isNotNullOrEmpty(final Object obj) {
		boolean result = false;
		if (obj == null || "null".equals(obj)
				|| "".equals(obj.toString().trim())) {
			result = true;
		}
		return !result;
	}
	/**
	 * 判断字符串是否为空
	 *
	 * @param stringValue
	 *            参数字符串
	 * @return true-空 false-非空
	 */
	public static boolean isEmpty(String stringValue) {
		return stringValue == null || stringValue.trim().length() == 0;
	}

	/**
	 * 判断字符串是否为数字类型
	 *
	 * @param stringValue
	 *            参数字符串
	 * @return true-非数字 false-数字
	 */
	public static boolean isDigital(String stringValue) {

		if (isEmpty(stringValue)) {
			return false;
		}

		for (int i = 0; i < stringValue.toCharArray().length; i++) {
			char c = stringValue.toCharArray()[i];
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * 取得指定字符串的长度<br>
	 * 一个英文字母或数字的长度为1，一个中文汉字的长度为2<br>
	 * 如果字符串为空，返回长度0
	 *
	 * @param str
	 *            指定的字符串
	 * @return
	 */
	public static int getLength(String str) {
		if (isEmpty(str)) {
			return 0;
		}
		return str.getBytes().length;
	}

	/**
	 * 字符串转化为整型
	 *
	 * @param stringNumber
	 *            要转化的字符串
	 * @return 转化后的整型值
	 */
	public static int string2Int(String stringNumber) {
		if (isEmpty(stringNumber) || !isDigital(stringNumber)) {
			throw new IllegalArgumentException(
					"String number parameter is null or not digital, invalid value :"
							+ stringNumber);
		}
		int intValue = 0;
		try {
			double doubleValue = string2Double(stringNumber);
			if (doubleValue >= Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Too large Int value!!!");
			}
			intValue = Integer.parseInt(stringNumber);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return intValue;
	}

	/**
	 * 字符串转化为Double
	 *
	 * @param pDoubleValue
	 *            要转化的字符串
	 * @return 转化后的Double
	 */
	public static double string2Double(String pDoubleValue) {
		if (isEmpty(pDoubleValue) || !isDigital(pDoubleValue)) {
			throw new IllegalArgumentException(
					"String parameter is null or not digital, invalid value :"
							+ pDoubleValue);
		}
		double doubleValue = 0d;
		try {
			doubleValue = Double.valueOf(pDoubleValue).doubleValue();
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"String parameter is not number!!!");
		}
		return doubleValue;
	}

	/**
	 * 字符串转化为Long
	 *
	 * @param pLongValue
	 *            要转化的字符串
	 * @return 转化后的Long
	 */
	public static long string2Long(String pLongValue) {
		if (isEmpty(pLongValue) || !isDigital(pLongValue)) {
			throw new IllegalArgumentException(
					"String parameter is null or not digital, invalid value :"
							+ pLongValue);
		}
		long longValue = 0;
		try {
			longValue = Long.valueOf(pLongValue).longValue();
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"String parameter is not number!!!");
		}
		return longValue;
	}

	/**
	 * 将 null 的字符串对象转换成空字符串
	 *
	 * @param nullString
	 *            null 的字符串对象
	 * @return
	 */
	public static String nullToEmpty(String nullString) {
		if (isEmpty(nullString)) {
			return "";
		}
		return nullString;
	}

	/**
	 * 将 null 的字符串对象转换成0
	 *
	 * @param nullString
	 *            null 的字符串对象
	 * @return
	 */
	public static String nullToZero(Object nullObj) {
		return nullObj == null ? "0" : String.valueOf(nullObj);
	}
	
	/**
	 * 数字格式化
	 * @param number
	 * @param format #.##,
	 * @return
	 */
	public static String formatNumber(String number, String format) {
		try{
			if (number == null)
				return "";
			if (format == null)
				format = "#.##";
			double datanumber = Double.parseDouble(number);
			java.text.NumberFormat nf = new DecimalFormat(format);
			return nf.format(datanumber);
		}catch (Exception e){
			return "";
		}
	}
	/**
	 * 数字格式化
	 * @param number
	 * @param format
	 * @return
	 */
	public static String formatNumber(float number, String format) {
		if (number == 0.0)
			return "0.0";
		if (format == null)
			format = "#.##";
		java.text.NumberFormat nf = new DecimalFormat(format);  
		return nf.format(number);
	}
	
	public static String replaceAllCharactors(String str, String[] arrRegStr, String newStr){
		if(str == null){
			return "";
		}
		if(arrRegStr==null || arrRegStr.length<1){
			return "";
		}
		if(newStr==null || "".equals(newStr)){
			newStr = ",";
		}
		for(String each : arrRegStr){
			str = str.replaceAll(each, newStr);
		}
        return str;
	}
	
	/**
	 * 格式化bean输出
	 * @param o
	 * @return
	 */
	public static String fmtBeanToString(Object o){
		Method fs[]=o.getClass().getMethods();
		StringBuffer sbf = new StringBuffer();
		for(Method f:fs){
			if(!f.getName().startsWith("get")
					|| f.getParameterTypes().length > 0
					|| f.getName().equals("getClass")
					){
				continue;
			}
			Object v;
			try {
				v = f.invoke(o, null);
				String field = f.getName().replace("get","");
				field = field.replaceFirst(field.substring(0, 1), field.substring(0, 1).toLowerCase());
				sbf.append(field).append(":[").append(v+"").append("]  ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sbf.toString();
	}
	/**
     * 判断是否为空
     * @param args
     * @return
     */
    protected static boolean isEmpty(Object ...args){
        if(args==null || args.length==0){
            return true;
        }
        for(Object o:args){
            if(o==null || "".equals(o)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断Map中的值是否为空
     * @param m
     * @param args
     * @return
     */
    public static boolean isEmptyInMap(Map m,Object ...keys){
        if(m==null){
            logger.debug("map is null");
            return true;
        }
        if(keys==null || keys.length==0){
            return true;
        }
        for(Object o:keys){
            if(isEmpty(m.get(o))){
                logger.info("isEmptyInMap key:"+o+" is null");
                return true;
            }
        }
        return false;
    }

    /**
     * 截取字符串中的某两段字符之间的字符串
     * @param string    进行处理的字符串
     * @param begin     前一段字符串
     * @param end       后一段字符串
     * @return
     */
    public static String subMidString(String string,String begin,String end){
        int first;
        int last;
        if(isEmpty(string)){
            return "";
        }
        
        if(isEmpty(begin)){
            first = 0;
        }else{
            first = string.indexOf(begin)+begin.length();
        }
        
        if( first== -1){
            return "";
        }
        
        if(isEmpty(end)){
            last = string.length();
        }else{
            last = string.indexOf(end);
        }
        
        if(last == -1){
            return string.substring(first);
        }
        
        return string.substring(first, last);
    }
    
    public static boolean isNull(String str){
        if(null ==str||str.equalsIgnoreCase("null")){
            return true;
        }else{
            return false;
        }
    }
	public static void main1(String[] args) {
		String[] arrCh = new String[]{"，",";","；","#","\\s+"};
		String newStr = ",";
		String str = "aaa,bbb，ccc;ddd；eee#fff   ttt			aaa,bbb，ccc;ddd；eee#fff   ttt";
		str = replaceAllCharactors(str, arrCh, newStr);
		System.out.println(str);
		String s = "result=0&description=发送短信成功&taskid=任务编号&&faillist=131&task_id=任务编号";
		System.out.println(subMidString(s, null, null));
	}
	
	public static void main(String[] args) {
        for(int j =0;j<10;j++){
            if(j ==5){
                continue;
            }
            System.out.println(j);
        }
    }
	
}