package pl.taskyers.taskybase.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final long ONE_DAY = 24L * 60L * 60L * 1000L;
    
    public static Date getCurrentTimestamp() {
        return new Date();
    }
    
    public static Date getCurrentDate() {
        return parseDate(DATE_FORMAT.format(new Date()));
    }
    
    public static Date addDayToDate(Date date) {
        return parseDate(DATE_FORMAT.format(new Date(date.getTime() + ONE_DAY)));
    }
    
    public static Date parseDate(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String parseString(Date date) {
        return DATE_FORMAT.format(date);
    }
    
}
