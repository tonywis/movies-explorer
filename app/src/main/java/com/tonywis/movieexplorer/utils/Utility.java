package com.tonywis.movieexplorer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import java.util.Locale;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Tony on 03/02/2018.
 */

public class Utility {
    /**
     *
     * @param context
     * @return int Nb Columns optimized for user
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    /**
     *
     * @param text
     * @param textColor
     * @return Bitmap
     */
    public static Bitmap textAsBitmap(String text, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize((float) 40);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    /**
     *
     * @return String Language format exemple fr-FR or en-US
     */
    public static String getFullCodeLanguage() {
        return Locale.getDefault().toString().replace("_","-");
    }

    /**
     *
     * @return String Language simple format exemple FR or US
     */
    public static String getBasicCodeLanguage() {
        return Locale.getDefault().getCountry();
    }
}
