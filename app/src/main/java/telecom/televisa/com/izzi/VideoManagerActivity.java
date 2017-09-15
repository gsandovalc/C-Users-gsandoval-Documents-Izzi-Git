package telecom.televisa.com.izzi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.model.EquiposVideo;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.Util;
import televisa.telecom.com.util.izziVideoManagerResponse;

public class VideoManagerActivity extends IzziActivity implements IzziRespondable, ViewPagerEx.OnPageChangeListener {
   List<EquiposVideo> equiposLista;
    private boolean isHelp1Opened=false;
    private LinearLayout help1;
    private int animDuration=200;
    String mode;
    boolean getInfo=false;
    private SliderLayout mDemoSlider;
    EquiposVideo selectedEqui;


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        ((TextView)findViewById(R.id.alias)).setText(equiposLista.get(i).getAlias());
        ((TextView)findViewById(R.id.serial)).setText(equiposLista.get(i).getSerialNumber());
        ((TextView)findViewById(R.id.ssid2)).setText(equiposLista.get(i).getAlias());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_manager);
        help1=(LinearLayout)findViewById(R.id.chng_ssid);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Map<String,String> parametross=new HashMap<>();
        mDemoSlider.stopAutoCycle();
        TextSliderView textSliderView = new TextSliderView(this);
        // initialize a SliderLayout
        textSliderView.image(R.drawable.generico);
        mDemoSlider.addSlider(textSliderView);
        try {
            parametross.put("METHOD", "videoManager/getEquipos");
            parametross.put("account", info.getCvNumberAccount());
            parametross.put("user", AES.encrypt(info.getUserName()));
            parametross.put("pass", AES.encrypt(info.getPassword()));
            getInfo = true;
            new AsyncResponse(this, false).execute(parametross);
        }catch(Exception e){

        }
    }
    public void setCajaName(View v){
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        sendAlias();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de querer modificar el nombre de tu equipo de video?").setPositiveButton("Aceptar", dialogClickListener)
                .setNegativeButton("Cancelar", dialogClickListener).show();
    }
    public void sendAlias(){
        try {
            closeHelp1();
            String ssid=((EditText)findViewById(R.id.e_ssid)).getText().toString();
            EquiposVideo ev=equiposLista.get(mDemoSlider.getCurrentPosition());
            ev.setAlias(ssid);
            onPageSelected(mDemoSlider.getCurrentPosition());
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> parametross=new HashMap<>();
            parametross.put("METHOD", "videoManager/setAlias");
            parametross.put("serial", AES.encrypt(ev.getSerialNumber()));
            parametross.put("user", AES.encrypt(info.getUserName()));
            parametross.put("pass", AES.encrypt(info.getPassword()));
            parametross.put("alias", AES.encrypt(ssid));
            parametross.put("account", info.getCvNumberAccount());
            ((EditText)findViewById(R.id.e_ssid)).setText("");

            new AsyncResponse(this,false).execute(parametross);
        }catch(Exception e){

        }
    }
    public void showHelp1(View view) {
        if(!isHelp1Opened){
            openHelp1();
            isHelp1Opened=true;
        }else{
            closeHelp1();
            isHelp1Opened=false;
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
            }
        });
        va.start();

        ((LinearLayout)findViewById(R.id.ssid_cont)).setBackgroundColor(0xffffffff);
        ((ImageView)findViewById(R.id.editIm1)).setImageResource(R.drawable.editinfo);


    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null)
            finish();
        else{
            if(getInfo) {
                getInfo = false;
                izziVideoManagerResponse res = (izziVideoManagerResponse) response;
                if (res.getIzziError().isEmpty()) {
                    List<EquiposVideo> equips = res.getResponse().getEquipos();
                    equiposLista=equips;
                    boolean isMirada = false;
                    for (EquiposVideo ev : equips)
                        if (ev.getType().equalsIgnoreCase("mirada"))
                            isMirada = true;
                    mDemoSlider.removeAllSliders();
                    if (isMirada) {
                        ((TextView)findViewById(R.id.h_title)).setText("Ayuda de Mi TV");

                        ((LinearLayout)findViewById(R.id.aliasnomirada)).setVisibility(View.GONE);
                        for (EquiposVideo ev : equips) {
                            TextSliderView textSliderView = new TextSliderView(this);
                            // initialize a SliderLayout
                            textSliderView.image(ev.getModel().toLowerCase().contains("pace"    )?R.drawable.deco_pace:R.drawable.technicolor_deco)
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
                            mDemoSlider.addSlider(textSliderView);
                        }
                    } else {
                        ((LinearLayout)findViewById(R.id.mirada)).setVisibility(View.GONE);
                        for (EquiposVideo ev : equips) {
                            TextSliderView textSliderView = new TextSliderView(this);
                            // initialize a SliderLayout
                            textSliderView.image(R.drawable.generico)
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
                            mDemoSlider.addSlider(textSliderView);
                        }


                    }
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.stopAutoCycle();
                    mDemoSlider.addOnPageChangeListener(this);
                    mDemoSlider.setCurrentPosition(0);

                } else {
                    //mostrar error
                }
            }
        }

    }

    @Override
    public void slowInternet() {

    }

    public void problema1(View v){
        noMirada(1);
    }
    private void noMirada(int problem){
        Intent i=new Intent(this,NoMiradaTroubleshoot.class);
        String serie=equiposLista.get(mDemoSlider.getCurrentPosition()).getSerialNumber();

        i.putExtra("problema",problem);
        i.putExtra("serie",serie);
        startActivity(i);
    }
    public void problema2(View v){
        noMirada(2);
    }
    public void problema3(View v){
        noMirada(3);
    }
    public void problema4(View v){
        noMirada(4);
    }
    public void problemaM1(View v){
        mirada(1);
    }
    public void problemaM2(View v){
        mirada(2);
    }
    public void problemaM3(View v){
        mirada(3);
    }
    public void problemaM4(View v){
        mirada(4);
    }
    public void problemaM5(View v){
        mirada(5);
    }
    public void problemaM6(View v){
        mirada(6);
    }
    public void problemaM7(View v){
        mirada(7);
    }
    public void problemaM8(View v){
        mirada(8);
    }
    public void problemaM9(View v){
    mirada(9);
    }
    public void problemaM10(View v){
        mirada(10);
    }
    private void mirada(int problem){
        Intent i=new Intent(this,MiradaTroubleShoot.class);
        String serie=equiposLista.get(mDemoSlider.getCurrentPosition()).getSerialNumber();
        i.putExtra("problema",problem);
        i.putExtra("serie",serie);
        startActivity(i);
    }

}
