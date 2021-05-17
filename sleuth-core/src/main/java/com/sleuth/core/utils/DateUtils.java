package com.sleuth.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	static final String UTC_TIME_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	/** UTC转本地时间
	 * 
	 * @param utcTime
	 * @return
	 */
    public static Date utcToLocale(String utcTime) {
		try {
			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat(UTC_TIME_FMT);
			date = sdf.parse(utcTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			long times = calendar.getTime().getTime() + TimeZone.getDefault().getRawOffset();
			return new Date(times);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /** 本地时间转UTC
     * 
     * @param localeTime
     * @return
     */
	public static Date localeToUtc(Date localeTime) {
		long times = localeTime.getTime() - TimeZone.getDefault().getRawOffset();
		return new Date(times);
	}
	
	/** 本地时间转UTC
     * 
     * @param localeTime
     * @return
     */
	public static Long nowToUtc() {
		return new Date().getTime() - TimeZone.getDefault().getRawOffset();
	}
}
