package com.tonywis.movieexplorer.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tony on 20/10/2016.
 */
public class FormatHelper {

    /**
     *
     * @param context
     * @param cal
     * @param international
     * @return String Date
     */
    public static String formatCalDateFormatToString(Context context, Calendar cal, boolean international) {
        SimpleDateFormat dateFormat;
        if (international)
            dateFormat = new SimpleDateFormat("yyyy-MM-dd", context.getResources().getConfiguration().locale);
        else
            dateFormat = (SimpleDateFormat) DateFormat.getDateFormat(context);
        String datePattern = dateFormat.toPattern();
        //Log.d("Schedule Date Pattern", datePattern);
        SimpleDateFormat format = new SimpleDateFormat(datePattern, context.getResources().getConfiguration().locale);
        return format.format(cal.getTime());
    }

    /**
     *
     * @param context
     * @param strCal
     * @return String Date
     */
    public static Calendar formatStringToCal(Context context, String strCal) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", context.getResources().getConfiguration().locale);
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
