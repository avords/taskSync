package cn.com.flaginfo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.math.NumberUtils;

public class DateUtil {
	private static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";

	/**
     * Date类型转换为Calendar类型
     * 
     * @param date
     *            日期
     * @return
     */
    public static Calendar toCalendar(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    
	public static Date getDate(String text, String format) {
		if(text==null || "".equals(text)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;

		try {
			date = sdf.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}

	public static Date getDate(String text) {
		return getDate(text, defaultFormat);
	}

	public static Date getDate(long time, String format) {

		return null;

	}

	public static String fmtDate(Date date) {
		return fmtDate(date, defaultFormat);
	}

	public static String fmtDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date);

	}

	public static java.sql.Date getSQLDate(String text, String format) {
		if (text == null || "".equals(text)) {
			return null;
		}

		long time = getDate(text, format).getTime();
		return new java.sql.Date(time);

	}

	public static java.sql.Timestamp getTimestamp(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}

	public static String getCurDateField(String format) {
		Date date = new Date();
		return fmtDate(date, format);
	}

	public static void main(String[] args) {

		System.out.println(getMonthLastDayAndSecond(new Date()));
		
		System.out.println(fmtDate(getPreMonthLastDayAndSecond(new Date())));

	}
	
	/**
	 * 获取date日期的上一个月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getPreMonthLastDayAndSecond(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return getDate(fmtDate(c.getTime(),"yyyy-MM-dd")+" 23:59:59");
		
	}

	/**
	 * 获取当月第1天'yyyy-MM'
	 * 
	 * @param date
	 * @return 'yyyy-MM-dd'
	 * @throws ParseException
	 */
	public static String getMonthFirstDay(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date theDate;
		try {
			theDate = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			theDate = new Date();
		}
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		return df1.format(gcLast.getTime());

	}

	/**
	 * 获取当月最后一天'yyyy-MM'
	 * 
	 * @param date
	 * @return 'yyyy-MM-dd'
	 * @throws ParseException
	 */
	public static String getMonthLastDay(String date)  {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date theDate;
		GregorianCalendar gcLast;
		try {
			theDate = df.parse(date);
			gcLast = (GregorianCalendar) Calendar.getInstance();
			gcLast.setTime(theDate);
			gcLast.set(Calendar.DAY_OF_MONTH, 1);
			gcLast.roll(Calendar.DAY_OF_MONTH, -1);
			return df1.format(gcLast.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getMonthLastDayAndSecond(Date date)  {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDate(fmtDate(c.getTime(),"yyyy-MM-dd")+" 23:59:59");
		
	}
	
	

	/**
	 * 获取指定月前一月最后一天'yyyy-MM'
	 * 
	 * @param date
	 * @return 'yyyy-MM-dd'
	 * @throws ParseException
	 */
	public static String getLastMonthMaxDay(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date theDate = df.parse(date);
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.add(Calendar.MONTH, -1);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		gcLast.roll(Calendar.DAY_OF_MONTH, -1);
		return df1.format(gcLast.getTime());
	}

	/**
	 * 获取指定月倒数第二天'yyyy-MM'
	 * 
	 * @param date
	 * @return 'yyyy-MM-dd'
	 * @throws ParseException
	 */
	public static String getMonthLastSecondDay(String date)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Date theDate = df.parse(date);
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		gcLast.roll(Calendar.DAY_OF_MONTH, -1);
		gcLast.add(Calendar.DAY_OF_MONTH, -1);
		return df1.format(gcLast.getTime());
	}
	
	/**
     * 日期加（减）amount天
     * 
     * @param date
     *            源日期
     * @param amount
     *            增加的数量，可以为负
     * @return 增加后的日期
     */
    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }
	
	/**
     * 将源日期增加一定量，如加一分钟，加一天，加一个月等等
     * 
     * @param date
     *            源日期
     * @param field
     *            日历类（Calendar）定义的域，如：Calendar.SENCOND,Calendar.MINUTES
     * @param amount
     *            增加的数量，可以为负
     * @return 增加后的日期
     */
    public static Date add(final Date date, final int field, final int amount) {
        final Date addDate = date == null ? new Date() : date;
        final Calendar calendar = toCalendar(addDate);
        calendar.add(field, amount);
        return calendar.getTime();
    }


	/**
	 * 时间加（减）amount 分钟
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMinutes(final Date date, final int amount) {
		Date useDate = date == null ? new Date() : date;
		useDate.setTime(useDate.getTime() + (amount * 60 * 1000));
		return useDate;
	}
	/**
	 * 时间加减分钟
	 * @param dateStr 日期字符串
	 * @param simpleDateFmt  时间格式如:yyyy-MM-dd HH:mm:ss
	 * @param amount 分钟数
	 * @return
	 * @throws ParseException
	 */
	public static Date addMinutes(String dateStr,String simpleDateFmt, final int amount){
		SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFmt);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}
		date.setTime(date.getTime()+(amount * 60 * 1000));
		return date;
	}
	/**
	 * 时间加减分钟 
	 * @param dateStr 格式:yyyy-MM-dd HH:mm:ss
	 * @param amount
	 * @return
	 * @throws ParseException 
	 */
	public static Date addMinutes(String dateStr, final int amount){
		return addMinutes(dateStr, defaultFormat, amount);
	}
	
	/**
	 * 获取上月
	 * @return
	 */
	public static String getPreMonth(Date date){
		if(date==null){
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return fmtDate(c.getTime(),"yyyy-MM");
	}
	
	/**
	 * 动态获取天或时间(精确到秒)
	 * @param date
	 * @param year
	 * @param month
	 * @param isLastSecond
	 * @return
	 */
	public static String fmtDayOrTime(Date date,int year,int month,boolean isLastSecond){
		Calendar c = Calendar.getInstance();
		if(date == null){
			date = new Date();
		}
		c.set(Calendar.YEAR,year);
		c.set(Calendar.MONTH,month);
		String day = fmtDate(date, "yyyy-MM-dd");
		if(isLastSecond){
			return day+" 23:59:59";
		}
		return day;
	}
	
	/**
	 * 按小时减去时间
	 * @param date  yyyy-MM-dd HH:mm:ss
	 * @param hour  需为整数
	 * @return
	 */
	public static String minusTimeUnitHour(String date,String hour){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date time = fmt.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(time);
			c.add(Calendar.HOUR, 0-NumberUtils.toInt(hour));
			return fmt.format(c.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String addMonth(String date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Date theDate;
		try {
			theDate = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			theDate = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(Calendar.MONTH, 1);
		return df.format(cal.getTime());
	}
	
	public static String addMonth(String date,int num)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Date theDate;
		try {
			theDate = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			theDate = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(Calendar.MONTH, num);
		return df.format(cal.getTime());
	}
}
