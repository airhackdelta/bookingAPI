package com.hackathon.api.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 424047 on 4/6/2019.
 */
public class DateUtils {

    public static DateTime getDate(String dateString){
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        return dtf.parseDateTime(dateString);
    }
}
