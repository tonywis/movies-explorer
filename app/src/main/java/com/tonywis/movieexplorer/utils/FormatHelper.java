package com.tonywis.movieexplorer.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Tony on 20/10/2016.
 */
public class FormatHelper {

    public static String formatCalDateFormatToString(Context c, Calendar cal, boolean international) {
        SimpleDateFormat dateFormat;
        if (international)
            dateFormat = new SimpleDateFormat("yyyy-MM-dd", c.getResources().getConfiguration().locale);
        else
            dateFormat = (SimpleDateFormat) DateFormat.getDateFormat(c);
        String datePattern = dateFormat.toPattern();
        //Log.d("Schedule Date Pattern", datePattern);
        SimpleDateFormat format = new SimpleDateFormat(datePattern, c.getResources().getConfiguration().locale);
        return format.format(cal.getTime());
    }

    public static Calendar formatStringToCal(Context c, String strCal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", c.getResources().getConfiguration().locale);
        Calendar cal = null;
        try {
            cal = Calendar.getInstance();
            cal.setTime(format.parse(strCal));
        } catch (ParseException e) {
            e.printStackTrace();
            cal = null;
        }
        return cal;
    }

}
