package izzi.connect.controls;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by cevelez on 22/04/2015.
 */
 class Util {
     static int dpToPx(Activity ac, int dp)
    {
        float density = ac.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

     static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
