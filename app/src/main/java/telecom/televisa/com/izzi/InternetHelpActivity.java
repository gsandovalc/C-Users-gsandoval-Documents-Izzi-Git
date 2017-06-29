package telecom.televisa.com.izzi;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.Util;
import televisa.telecom.com.util.Utils;

public class InternetHelpActivity extends IzziActivity implements IzziRespondable{

    private boolean isHelp1Opened=false;
    private boolean isHelp2Opened=false;
    private LinearLayout help1;
    private LinearLayout help2;
    private int animDuration=200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_help);
        help1=(LinearLayout)findViewById(R.id.nonav);
        help2=(LinearLayout)findViewById(R.id.sloww);
    }

    public void showHelp1(View view) {
        if(!isHelp1Opened){
            openHelp1();
            isHelp1Opened=true;
            if(isHelp2Opened)
                showHelp2(view);
        }else{
            closeHelp1();
            isHelp1Opened=false;
        }
    }
    public void showHelp2(View view) {
        if(!isHelp2Opened){
            openHelp2();
            isHelp2Opened=true;
            if(isHelp1Opened)
                showHelp1(view);
        }else{
            closeHelp2();
            isHelp2Opened=false;
        }
    }
    private void openHelp1(){
        ValueAnimator va = ValueAnimator.ofInt(0, Util.dpToPx(this,155));
        va.setDuration(animDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help1.getLayoutParams().height = value.intValue();
                help1.requestLayout();
            }
        });
        va.start();
        ((ImageView)findViewById(R.id.img1)).setImageResource(R.drawable.lessinfo);
        ((LinearLayout)findViewById(R.id.ssid_cont)).setBackgroundColor(0xff4d959a);
    }
    private void closeHelp1(){
        ValueAnimator va = ValueAnimator.ofInt(Util.dpToPx(this,155),0);
        va.setDuration(animDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help1.getLayoutParams().height = value.intValue();
                help1.requestLayout();
            }
        });
        va.start();
        ((ImageView)findViewById(R.id.img1)).setImageResource(R.drawable.moreinfo);
        ((LinearLayout)findViewById(R.id.ssid_cont)).setBackgroundColor(0xffffffff);
    }

    private void openHelp2(){
        ValueAnimator va = ValueAnimator.ofInt(0, Util.dpToPx(this,155));
        va.setDuration(animDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help2.getLayoutParams().height = value.intValue();
                help2.requestLayout();
            }
        });
        va.start();
        ((ImageView)findViewById(R.id.img2)).setImageResource(R.drawable.lessinfo);
        ((LinearLayout)findViewById(R.id.ssid_cont2)).setBackgroundColor(0xffca005d);

    }
    private void closeHelp2(){
        ValueAnimator va = ValueAnimator.ofInt(Util.dpToPx(this,155),0);
        va.setDuration(animDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help2.getLayoutParams().height = value.intValue();
                help2.requestLayout();
            }
        });
        va.start();
        ((ImageView)findViewById(R.id.img2)).setImageResource(R.drawable.moreinfo);
        ((LinearLayout)findViewById(R.id.ssid_cont2)).setBackgroundColor(0xffffffff);
    }

    public void restart(View v){
        try {
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","wifiManager/update");
            mp.put("account", info.getCvNumberAccount());
            mp.put("user",AES.encrypt(info.getUserName()));
            mp.put("pass",AES.encrypt(info.getPassword()));
            mp.put("cmaddrs",info.getCmaddrs());
            mp.put("cmd",AES.encrypt("4"));
            mp.put("value",AES.encrypt(""));
            new AsyncResponse(this,false).execute(mp);
        }catch(Exception e){

        }
    }
    public void goToChat(View v){
        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(i);
    }
    public void call(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        String telefono="";
        if(info.isLegacy()){
            telefono="018001205000";
        }else{
            telefono="018001205000";
            System.out.println("Es izzi");
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono));
        startActivity(callIntent);
    }

    @Override
    public void notifyChanges(Object response) {
        if(response!=null){
            showError("Tu modem se está reiniciando",0  );
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }


}
