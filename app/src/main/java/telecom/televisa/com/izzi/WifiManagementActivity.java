package telecom.televisa.com.izzi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.Util;

public class WifiManagementActivity extends IzziActivity implements IzziRespondable{

    private WifiManagementActivity act=this;
    private boolean firstTime=true;

    private boolean isHelp1Opened=false;
    private boolean isHelp2Opened=false;
    private LinearLayout help1;
    private LinearLayout help2;
    private int animDuration=200;
    int action=0;
    String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_wifi_management);
        help1=(LinearLayout)findViewById(R.id.chng_ssid);
        help2=(LinearLayout)findViewById(R.id.chng_pass);
        init();
        ((View)findViewById(R.id.papat)).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mode=mode==null?"":mode;
                if(mode.toLowerCase().contains("arris")){

                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.arris);
                    if(mode.toLowerCase().contains("tg1652")){
                        ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tg1652);
                    }
                }else if(mode.toLowerCase().contains("cisco")){
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.cisco);
                }else{
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tecnicolor);
                }
            }
        });
    }
    private void init(){
        TextView ssid=(TextView)findViewById(R.id.ssid_name);
        TextView ssid2=(TextView)findViewById(R.id.ssid2);
        TextView model=(TextView)findViewById(R.id.model);
        TextView passwd=(TextView)findViewById(R.id.passwd);
        final Switch swtch=(Switch)findViewById(R.id.wifiSwtch);
        final TextView status=(TextView)findViewById(R.id.wistatus);
        final Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();

        try{
            String modelo=AES.decrypt(info.getModel());
            modelo=modelo==null?"":modelo;
            mode=modelo;
            if(modelo.toLowerCase().contains("arris")){

                ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.arris);
                if(modelo.toLowerCase().contains("tg1652")){
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tg1652);
                }
            }else if(modelo.toLowerCase().contains("cisco")){
                ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.cisco);
            }else{
                ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tecnicolor);
            }
            ssid.setText(AES.decrypt(info.getWifiName()));
            ssid2.setText(AES.decrypt(info.getWifiName()));
            model.setText(AES.decrypt(info.getModel()));
            passwd.setText(AES.decrypt(info.getWifiPass()));
            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            turnWifiOnOff(false);
                            status.setText("Prender Mi Red");
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            swtch.setChecked(true);
                            break;
                    }
                }
            };

            swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        try {
                            swtch.setTrackDrawable(ContextCompat.getDrawable(act, R.drawable.custom_track2));
                            if (!firstTime && !((IzziMovilApplication) getApplication()).getCurrentUser().isWifiStatus()) {
                                turnWifiOnOff(true);
                                try {
                                    Answers.getInstance().logCustom(new CustomEvent("wifi_on_off").putCustomAttribute("user", info.getUserName()).putCustomAttribute("account", AES.decrypt(info.getCvNumberAccount())));
                                }catch (Exception e){

                                }
                            }
                            status.setText("Apagar Mi Red");
                        }catch(Exception e){

                        }

                    }else{
                        swtch.setTrackDrawable(ContextCompat.getDrawable(act, R.drawable.custom_track));
                        if(!firstTime){
                            AlertDialog.Builder builder = new AlertDialog.Builder(act);
                            builder.setMessage("Si apagas tu Red, todos tus dispositivos perderán conexión WiFi").setPositiveButton("Aceptar", dialogClickListener)
                                    .setNegativeButton("Cancelar", dialogClickListener).show();
                            try {
                                Answers.getInstance().logCustom(new CustomEvent("wifi_on_off").putCustomAttribute("user", info.getUserName()).putCustomAttribute("account", AES.decrypt(info.getCvNumberAccount())));
                            }catch (Exception e){

                            }
                        }

                    }
                    firstTime=false;
                }
            });
            if(info.isWifiStatus()){
                swtch.setChecked(true);
                status.setText("Apagar Mi Red");

            }else{
                swtch.setChecked(false);
                status.setText("Prender Mi Red");
                firstTime=false;
            }
        }catch (Exception e){

        }

    }
    public void goToHelp(View v) {
        Intent i=new Intent(this,InternetHelpActivity.class);
        startActivityForResult(i, 0);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
        ValueAnimator va = ValueAnimator.ofInt(0, Util.dpToPx(this,110));
        va.setDuration(animDuration);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help1.getLayoutParams().height = value.intValue();
                help1.requestLayout();

            }
        });
        va.start();
        ((LinearLayout)findViewById(R.id.ssid_cont)).setBackgroundColor(0xff4d959a);
        ((ImageView)findViewById(R.id.editIm1)).setImageResource(R.drawable.canceledit);
    }
    private void closeHelp1(){

        ValueAnimator va = ValueAnimator.ofInt(Util.dpToPx(this,110),0);
        va.setDuration(animDuration);
        isHelp1Opened=false;

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help1.getLayoutParams().height = value.intValue();
                help1.requestLayout();

            }
        });
        va.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                // done
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow((getCurrentFocus()==null)?null:getCurrentFocus().getWindowToken(), 0);
                mode=mode==null?"":mode;
                if(mode.toLowerCase().contains("arris")){

                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.arris);
                    if(mode.toLowerCase().contains("tg1652")){
                        ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tg1652);
                    }
                }else if(mode.toLowerCase().contains("cisco")){
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.cisco);
                }else{
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tecnicolor);
                }
            }
        });
        va.start();

        ((LinearLayout)findViewById(R.id.ssid_cont)).setBackgroundColor(0xffffffff);
        ((ImageView)findViewById(R.id.editIm1)).setImageResource(R.drawable.editinfo);


    }

    private void openHelp2(){
        ValueAnimator va = ValueAnimator.ofInt(0, Util.dpToPx(this,110));
        va.setDuration(animDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help2.getLayoutParams().height = value.intValue();
                help2.requestLayout();
            }
        });
        va.start();

        ((LinearLayout)findViewById(R.id.ssid_cont2)).setBackgroundColor(0xffFFA807);
        ((ImageView)findViewById(R.id.editIm2)).setImageResource(R.drawable.canceledit);


    }
    private void closeHelp2(){

        ValueAnimator va = ValueAnimator.ofInt(Util.dpToPx(this,110),0);
        init();
        isHelp2Opened=false;
        va.setDuration(animDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                help2.getLayoutParams().height = value.intValue();
                help2.requestLayout();
            }
        });
        va.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                // done
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow((getCurrentFocus()==null)?null:getCurrentFocus().getWindowToken(), 0);
                mode=mode==null?"":mode;
                if(mode.toLowerCase().contains("arris")){

                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.arris);
                    if(mode.toLowerCase().contains("tg1652")){
                        ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tg1652);
                    }
                }else if(mode.toLowerCase().contains("cisco")){
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.cisco);
                }else{
                    ((ImageView)findViewById(R.id.imageView1)).setImageResource(R.drawable.tecnicolor);
                }
            }
        });
        va.start();
        ((LinearLayout)findViewById(R.id.ssid_cont2)).setBackgroundColor(0xffffffff);
        ((ImageView)findViewById(R.id.editIm2)).setImageResource(R.drawable.editinfo);

    }


    @Override
    public void notifyChanges(Object response) {
        if(response!=null){
            //0 el generico
            final Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            switch (action){
                case 1:
                    showError("El cambio de nombre de tu Red WiFi fue exitoso. Actualiza tus dispositivos para asegurar su conexión.",0);
                    try {
                        Answers.getInstance().logCustom(new CustomEvent("wifi_change_ssid").putCustomAttribute("user", info.getUserName()).putCustomAttribute("account", AES.decrypt(info.getCvNumberAccount())));
                    }catch (Exception e){

                    }
                    break;
                case 2:
                    showError("El cambio de contraseña fue exitoso, actualiza tus dispositivos para asegurar su conexión.\n",0);
                    try {
                        Answers.getInstance().logCustom(new CustomEvent("wifi_change_password").putCustomAttribute("user", info.getUserName()).putCustomAttribute("account", AES.decrypt(info.getCvNumberAccount())));
                    }catch (Exception e){

                    }
                    break;
            }


        }else{
            showError("Error inesperado",783);
        }
        init();
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }

    private void turnWifiOnOff(Boolean status){
        try {
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","wifiManager/update");
            mp.put("account", info.getCvNumberAccount());
            mp.put("user",AES.encrypt(info.getUserName()));
            mp.put("pass",AES.encrypt(info.getPassword()));
            mp.put("cmaddrs",info.getCmaddrs());
            mp.put("cmd",AES.encrypt("3"));
            mp.put("value",AES.encrypt(status?"1":"0"));
            new AsyncResponse(this,false).execute(mp);
            info.setWifiStatus(status);
            info.save();
            action=status?4:3;
        }catch(Exception e){

        }
    }
/* */


    public void setSSID(View view) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        setSSID2(new View(act));

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setMessage("¿Estás seguro de querer modificar el nombre de tu red WiFi?").setPositiveButton("Aceptar", dialogClickListener)
                .setNegativeButton("Cancelar", dialogClickListener).show();
    }

    public void setSSID2(View view) {
        try {
            closeHelp1();
            String ssid=((EditText)findViewById(R.id.e_ssid)).getText().toString();
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","wifiManager/update");
            mp.put("account", info.getCvNumberAccount());
            mp.put("user",AES.encrypt(info.getUserName()));
            mp.put("pass",AES.encrypt(info.getPassword()));
            mp.put("cmaddrs",info.getCmaddrs());
            mp.put("cmd",AES.encrypt("1"));
            mp.put("value",AES.encrypt(ssid));
            info.setWifiName(AES.encrypt(ssid));
            info.save();
            ((EditText)findViewById(R.id.e_ssid)).setText("");
            ((TextView)findViewById(R.id.ssid_name)).setText(ssid);
            ((TextView)findViewById(R.id.ssid2)).setText(ssid);
            action=1;
            new AsyncResponse(this,false).execute(mp);
        }catch(Exception e){

        }
    }

    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    setSSID2(new View(act));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };



    public void setPass(View view) {

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        setPass2(new View(act));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setMessage("¿Estás seguro de querer modificar la contraseña de tu red WiFi?").setPositiveButton("Aceptar", dialogClickListener)
                .setNegativeButton("Cancelar", dialogClickListener).show();
    }
    public void setPass2(View view) {
        try {
            //validar
            closeHelp2();
            init();
            String pass=((EditText)findViewById(R.id.e_pass)).getText().toString();
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","wifiManager/update");
            mp.put("account", info.getCvNumberAccount());
            mp.put("user",AES.encrypt(info.getUserName()));
            mp.put("pass",AES.encrypt(info.getPassword()));
            mp.put("cmaddrs",info.getCmaddrs());
            mp.put("cmd",AES.encrypt("2"));
            mp.put("value",AES.encrypt(pass));
            new AsyncResponse(this,false).execute(mp);
            info.setWifiPass(AES.encrypt(pass));
            info.save();

            ((EditText)findViewById(R.id.e_pass)).setText("");
            ((TextView)findViewById(R.id.passwd)).setText(pass);

            init();
            action=2;
        }catch(Exception e){

        }
    }
}
