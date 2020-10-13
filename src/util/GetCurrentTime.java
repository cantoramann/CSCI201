package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetCurrentTime {
	public static String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("[mm:ss:SSS]");
		Date date = new Date();
		String time = dateFormat.format(date);
		return time;
	}
}