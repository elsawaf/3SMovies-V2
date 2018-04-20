package com.elsawaf.thebrilliant.a3smovies.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {

    // It is a dynamic method to accurately calculate the no. of columns of the grid list.
    // This will be more adaptive for users of varying screen sizes without being restricted to only two possible values.
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        //You can vary the value held by the scalingFactor variable.
        // The smaller it is the more no. of columns you can display,
        // and the larger the value the less no. of columns will be calculated.
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}
