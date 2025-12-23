package fr.univangers.mashup.virtualcrm.utils;

import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
    private static final String APP_PATTERN = "yyyy-MM-dd";
    private static final String SALESFORCE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String SALESFORCE_RECEIVE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";


    public static Calendar parse(String date) throws InvalidDateFormatException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(APP_PATTERN);
        Calendar dateCalendar = Calendar.getInstance();
        try {
            dateCalendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            throw new InvalidDateFormatException(date, APP_PATTERN);
        }
        return dateCalendar;
    }

    public static String toStartOfDaySoql(String dateStr) throws InvalidDateFormatException {
        Calendar cal = parse(dateStr);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        SimpleDateFormat sfFormat = new SimpleDateFormat(SALESFORCE_PATTERN);
        sfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sfFormat.format(cal.getTime());
    }

    public static String toEndOfDaySoql(String dateStr) throws InvalidDateFormatException {
        Calendar cal = parse(dateStr);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        SimpleDateFormat sfFormat = new SimpleDateFormat(SALESFORCE_PATTERN);
        sfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sfFormat.format(cal.getTime());
    }

    public static String formatSfDateToApp(String sfDateStr) throws ParseException {
        SimpleDateFormat receiveFormat = new SimpleDateFormat(SALESFORCE_RECEIVE_PATTERN);
        SimpleDateFormat appFormat = new SimpleDateFormat(APP_PATTERN);
        return appFormat.format(receiveFormat.parse(sfDateStr));
    }
}
