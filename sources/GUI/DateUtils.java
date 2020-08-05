package GUI;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Beth
 */
public class DateUtils {
  public static final String DATE_FORMAT_FOLDER = "yyyyMMdd";
  public static final String TIME_FORMAT_NOW = "HH:mm.ss";

  public static String getDateFolder() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FOLDER);
    return sdf.format(cal.getTime());

  }
  public static String getCurrentTime(){
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT_NOW);
    return sdf.format(cal.getTime());
  }
  
  public static String getCurrentTime(String format){
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(cal.getTime());
  }
  
  public static String getStartTimeOfDay(String format) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    return sdf.format(cal.getTime()); 
  }
  public static Long getDateLong(int hour, int minute){
    // TODO this does not work
    Long time;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    time = cal.getTimeInMillis();
    return (time);
  }
  

}
