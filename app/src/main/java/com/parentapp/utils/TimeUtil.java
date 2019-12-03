package com.parentapp.utils;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    /**
     * Method to parse string time. Accepts all ISO 8601 string formats
     *
     * @param time Time string to be parsed
     * @return joda-dateTime object with UTC time zone in ISO8601 format
     */
    public static DateTime getFormattedDateTimeUTC(String time) {
        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat
                .dateTime()
                .withZoneUTC();
        return dateTimeFormatter.parseDateTime(time);
    }

    /**
     * @return joda-dateTime object with current time with UTC time zone
     */
    public static DateTime getFormattedDateTimeUTC() {
        return new DateTime().withZone(DateTimeZone.UTC);
    }

    /**
     * Method to parse time in milliseconds
     *
     * @param time Time in milliseconds to be parsed
     * @return joda-dateTime object with UTC time zone in ISO8601 format
     */
    public static DateTime getFormattedDateTimeUTC(long time) {
        return new DateTime(time).withZone(DateTimeZone.UTC);
    }

    /**
     * Method to convert java date to joda-datetime
     *
     * @param date Date object
     * @return joda-dateTime object with UTC time zone in ISO8601 format
     */
    public static DateTime getFormattedDateTimeUTC(Date date) {
        return new DateTime(date).withZone(DateTimeZone.UTC);
    }

    public static String convertUTCToLocalDate(DateTime dateTimeUTC) {
        LocalDateTime localDatetime = dateTimeUTC.toLocalDateTime();
        return localDatetime.toString(DateTimeFormat.patternForStyle("LS", null));
    }
}
