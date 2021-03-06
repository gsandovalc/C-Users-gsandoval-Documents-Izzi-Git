package televisa.telecom.com.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Map;

import telecom.televisa.com.izzi.PagosMainActivity;
import telecom.televisa.com.izzi.PaperlessActivity;
import telecom.televisa.com.izzi.R;
import televisa.telecom.com.ws.IzziWS;

/**
 * Created by cevelez on 11/05/2015.
 */
public class GetGuide extends AsyncTask<Map<String,String>, Object, Object> {
    IzziRespondable respondTo=null;
    Dialog loadingOverlay;
    boolean encryptData=false;
    String rpt;

    public GetGuide(IzziRespondable respondTo, boolean encryptData, String rpt){
        this.respondTo=respondTo;
        this.encryptData=encryptData;
        // hacer algo generico
        this.rpt=rpt;
    }

    @Override
    protected Object doInBackground(Map<String,String>... params) {
        String metodo="guide";
        Calendar cal=Calendar.getInstance();
        if(params[0].containsKey("scrolls")){
            cal.add(Calendar.HOUR_OF_DAY,(Integer.parseInt(params[0].get("scrolls"))*6));
        }
        int minutos=cal.get(Calendar.MINUTE);
        if(minutos<30)
            minutos=0;
        else
            minutos=30;
        cal.set(Calendar.MINUTE,minutos);
        cal.set(Calendar.SECOND,0);
        params[0].put("start",cal.getTimeInMillis()+"");
        params[0].put("hours","6");
        params[0].put("rpt","0");
        try {
            Map<String,String> infoMap=params[0];
            Gson gson = new Gson();

                izziGuideResponse response=gson.fromJson((String) IzziWS.callWebService(params[0], metodo), izziGuideResponse.class);
                return response;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingOverlay = new Dialog((Context)respondTo,android.R.style.Theme_Translucent);
        loadingOverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingOverlay.setCancelable(true);
        loadingOverlay.setContentView(R.layout.loading);
        WindowManager.LayoutParams lp = loadingOverlay.getWindow().getAttributes();
        lp.dimAmount=0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        loadingOverlay.getWindow().setAttributes(lp);
        loadingOverlay.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loadingOverlay.show();
        LinearLayout cuadro1=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro1);
        LinearLayout cuadro2=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro2);
        LinearLayout cuadro3=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro3);
        LinearLayout cuadro4=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro4);
        AnimationSet animationSet = new AnimationSet(true);
        AnimationSet animationSet2 = new AnimationSet(true);
        AnimationSet animationSet3 = new AnimationSet(true);
        AnimationSet animationSet4 = new AnimationSet(true);
        Animation an = new  RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation an2 = new  RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation an3 = new  RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation an4 = new  RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        an.setDuration(2000);
        an.setRepeatCount(-1);
        an2.setDuration(2000);
        an2.setRepeatCount(-1);
        an3.setRepeatCount(-1);
        an4.setRepeatCount(-1);
        an3.setDuration(2000);
        an4.setDuration(2000);
        an.setStartOffset(700);
        an2.setStartOffset(700);
        an3.setStartOffset(700);
        an4.setStartOffset(700);
        an.setFillAfter(true);
        an2.setFillAfter(true);
        an3.setFillAfter(true);
        an4.setFillAfter(true);


        TranslateAnimation a = new TranslateAnimation(0,0, 0,(Util.dpToPx((Activity)respondTo,100)));
        a.setDuration(500);
        a.setFillAfter(true);
        TranslateAnimation a1 = new TranslateAnimation(0,0, 0,-(Util.dpToPx((Activity)respondTo,100)));
        TranslateAnimation a2 = new TranslateAnimation(0,(Util.dpToPx((Activity)respondTo,100)), 0,0);
        TranslateAnimation a3 = new TranslateAnimation(0,-(Util.dpToPx((Activity)respondTo,100)), 0,0);
        a1.setDuration(500);
        a1.setFillAfter(true); //HERE
        a2.setDuration(500);
        a2.setFillAfter(true); //HERE
        a3.setDuration(500);
        a3.setFillAfter(true); //HERE
        animationSet.addAnimation(a);
        animationSet2.addAnimation(a1);
        animationSet3.addAnimation(a2);
        animationSet4.addAnimation(a3);
        animationSet.addAnimation(an);
        animationSet2.addAnimation(an2);
        animationSet3.addAnimation(an3);
        animationSet4.addAnimation(an4);
        cuadro1.startAnimation(animationSet);
        cuadro2.startAnimation(animationSet2);
        cuadro3.startAnimation(animationSet3);
        cuadro4.startAnimation(animationSet4);
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
        try{
            loadingOverlay.dismiss();
        }catch(Exception e){}
        respondTo.notifyChanges(response);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        try{
            loadingOverlay.dismiss();
        }catch(Exception e){}

    }
}
