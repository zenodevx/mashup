package fr.univangers.mashup.internalcrm.utils;

import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    private static final String datePattern = "yyyy-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    public static Calendar parse(String date) throws InvalidDateFormatException {
        Calendar dateCalendar = Calendar.getInstance();
        try {
            dateCalendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            throw new InvalidDateFormatException(date, datePattern);
        }
        return dateCalendar;
    }

    public static String toString(Calendar date) {
        return dateFormat.format(date.getTime());
    }
}
