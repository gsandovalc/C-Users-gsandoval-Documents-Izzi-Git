package televisa.telecom.com.controls;

import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import telecom.televisa.com.izzi.PagosMainActivity;
import telecom.televisa.com.izzi.R;
import televisa.telecom.com.util.Util;

/**
 * Created by cevelez on 29/12/2016.
 */

public class RelativeLayoutTouchListener implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    static final int MIN_DISTANCE = 200;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY,tx;
    private int position=0;
    private int CLICK_ACTION_THRESHHOLD = 20;


    // private MainActivity mMainActivity;

    public RelativeLayoutTouchListener(Activity mainActivity) {
        activity = mainActivity;

    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:{
                position=(int)(event.getRawX()+tx);
                if(position<0&&position>-Util.dpToPx(activity,100)){
                    v.animate()
                            .x(event.getRawX() + tx)
                            .setDuration(0)
                            .start();
                    ((PagosMainActivity)activity).closeOthers(v);
                }else{

                }
                return true;
            }
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                tx = v.getX() - event.getRawX();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float endX = event.getX();
                float endY = event.getY();
                if (isAClick(downX, endX, downY, endY)) {
                    System.out.println("fue click");
                    ((PagosMainActivity)activity).viewClick(v);
                }
                if(position>=-Util.dpToPx(activity,100)/2){
                    v.animate()
                            .x(0)
                            .setDuration(200)
                            .start();
                }else{
                    v.animate()
                            .x(-Util.dpToPx(activity,100))
                            .setDuration(200)
                            .start();
                }
                return true; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:

        }
        return true;
    }
    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > CLICK_ACTION_THRESHHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHHOLD) {
            return false;
        }
        return true;
    }
}