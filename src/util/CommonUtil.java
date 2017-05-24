package util;

import java.util.Calendar;

public class CommonUtil {
	
	/*
	 * 获取当前时间 格式:Year/Month/Day Hour:Minute:Second
	 * 
	 * @return String preTime
	 * */
	public static String getExaTime(){
		String ExaTime = "";
		//calender对象
		Calendar c1 = Calendar.getInstance();
		String year = Integer.toString(c1.get(Calendar.YEAR));
		String month = Integer.toString(c1.get(Calendar.MONTH) + 1);
		String day = Integer.toString(c1.get(Calendar.DATE));
		String hour = Integer.toString(c1.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(c1.get(Calendar.MINUTE));
		String second = Integer.toString(c1.get(Calendar.SECOND));
		ExaTime = String.format("%s/%s/%s %s:%s:%s", year,month,day,hour,minute,second);
		
		return ExaTime;
	}
}
