package pl.taskyers.taskybase.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public static Date getCurrentTimestamp() {
        return new Date();
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
