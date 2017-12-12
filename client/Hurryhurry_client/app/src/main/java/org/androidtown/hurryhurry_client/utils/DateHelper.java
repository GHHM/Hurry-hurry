package org.androidtown.hurryhurry_client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by HAMHAM on 2017-12-11.
 */

public class DateHelper {

    public static String format(String format, Date date) throws NullPointerException, IllegalArgumentException{
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parse(String format, String dateString) throws ParseException {
        return new SimpleDateFormat(format).parse(dateString);
    }

    public static String reformat(String sourceFormat, String targetFormat, String dateString) throws NullPointerException, IllegalArgumentException, ParseException {
        return new SimpleDateFormat(targetFormat).format(new SimpleDateFormat(sourceFormat).parse(dateString));
    }

    public static final String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static final String getCurrentTime(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static final String getCurrentDotDate(){
        return new SimpleDateFormat("yyyy.MM.dd").format(new Date());
    }

    public static final String getCurrentHHmmTime(){
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static final String getNextDate(){
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(cal.DATE, 1);
        Date currentDt = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currentDt);
    }

    public static final String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDt = new Date();
        return formatter.format(currentDt);
    }

}
