package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;

import televisa.telecom.com.controls.ResideMenu;
import televisa.telecom.com.controls.ResideMenuItem;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.ImageLoader;

/**
 * Created by cevelez on 22/04/2015.
 */
public class MenuActivity extends Activity implements DrawerLayout.DrawerListener{

    DrawerLayout drawer;
    LinearLayout menu;
    FrameLayout frame;
    public static Bitmap prf=null;
    private float lastTranslate = 0.0f;
    Usuario info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawer=((DrawerLayout)findViewById(R.id.drawer_layout));
        //initSpecialEfects();
        drawer.setDrawerListener(this);
        menu=((LinearLayout)findViewById(R.id.left_drawer));
       // setUpMenu();
        frame=((FrameLayout)findViewById(R.id.content_frame));
        info=((IzziMovilApplication)getApplication()).getCurrentUser();
        if(info==null){
            finish();
            Intent i= new Intent(getApplicationContext(),BtfLanding.class);
            startActivity(i);
            return;
        }
        init(info);
        ImageLoader loader;
        loader=new ImageLoader(this);
        try {
            if (info.getFotoPerfil() != null) {
                String image = AES.decrypt(info.getFotoPerfil());
                if (!image.isEmpty()) {
                    loader.DisplayImage(image, (ImageView) findViewById(R.id.imageViewp));

                }
            }
            if (MainActivity.facebookImg != null) {
                loader.DisplayImage(MainActivity.facebookImg, (ImageView) findViewById(R.id.imageViewp));

                System.out.println(MainActivity.facebookImg);
            }
        }catch(Exception e){

        }
    }
    void init(Usuario i){
        try{
            ((TextView)findViewById(R.id.nombre)).setText(AES.decrypt(i.getNombreContacto()));
            ((TextView)findViewById(R.id.contrato)).setText(i.getUserName());
        }catch (Exception e){

        }
    }

    public void goToView(View v){
        Intent i=null;
        switch(v.getId()){
            case R.id.vHome:
                i=new Intent(getApplicationContext(),UserActivity.class);
                break;
            case R.id.vServices:
                i=new Intent(getApplicationContext(),ServiciosActivity.class);
                break;
            case R.id.vLegales:
                i=new Intent(getApplicationContext(),LegalesActivity.class);
                break;
            case R.id.vEstado:
                i=new Intent(getApplicationContext(),EdoCuentaActivity.class);
                break;
            default:
                break;
        }
        finish();
        startActivity(i);
    }
    @Override
    public void setContentView(int id){

        super.setContentView(R.layout.navigation_menu);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(id, null, false);
        ((FrameLayout)findViewById(R.id.content_frame)).addView(contentView, 0);

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
/*
    @Override
    public void onClick(View v) {
        Intent i = null;
        if (v == null) {
            new Delete().from(Usuario.class).execute();
            ((IzziMovilApplication) getApplication()).setCurrentUser(null);
            ((IzziMovilApplication) getApplication()).setLogged(false);
            i = new Intent(this, BtfLanding.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            return;
        } else if (v == iwifi) {
            i = new Intent(getApplicationContext(), DrWifiActivity.class);
            // showMenu(v);
        } else if (v == iLegales) {

            i = new Intent(getApplicationContext(), LegalesActivity.class);
        } else if (v == iPaper) {

            i = new Intent(getApplicationContext(), PaperlessActivity.class);
        } else if (v == ipago) {

            i = new Intent(getApplicationContext(), PagoEstablecimientosActivity.class);
        } else if (v == iguia) {
            i = new Intent(getApplicationContext(), TvGuideActivity.class);

        } else if (v == inotifica) {
            i = new Intent(getApplicationContext(), PushNotificationCenterActivity.class);

        }else if (v == ichatea){
            i = new Intent(getApplicationContext(), ChatActivity.class);
        }else if( v==illamanos) {
            Usuario info = ((IzziMovilApplication) getApplication()).getCurrentUser();
            String telefono = "";
            if (info.isLegacy() && !info.isEsMigrado()) {
                telefono = "018001205000";
            } else {
                telefono = "018001205000";
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + telefono));
            startActivity(callIntent);
            //showMenu(v);
        }else if(v==resideMenu.getCerrarSession()) {
            new Delete().from(Usuario.class).execute();
            ((IzziMovilApplication) getApplication()).setCurrentUser(null);
            ((IzziMovilApplication) getApplication()).setLogged(false);
            i = new Intent(this, BtfLanding.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            return;
        } else if(v==inotifica){

        } else if(v==iuser){
            i=new Intent(getApplicationContext(),SwitchUserActivity.class);
        }else
            return;

        if(i!=null) {
            startActivity(i);
            resideMenu.closeMenu();
        }
    }
    //nuevooooo
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    protected void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.bluee);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setSwipeDirectionDisable(1);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(.5f);
        resideMenu.getCerrarSession().setOnClickListener(this);
        ipago= new ResideMenuItem(this,R.drawable.lugares,"Lugares de pago");
        iwifi= new ResideMenuItem(this, R.drawable.wifi,  "Dr WiFi");
        iguia = new ResideMenuItem(this,R.drawable.guia,"Guía de programación");
        iLegales  = new ResideMenuItem(this, R.drawable.legales,  "Avisos Legales");
        illamanos     = new ResideMenuItem(this,R.drawable.call,"Llamanos");
        ichatea  = new ResideMenuItem(this, R.drawable.chat,  "Chatea con nosotros");
        iPaper = new ResideMenuItem(this, R.drawable.paperless2, "Paperless");
        inotifica=new ResideMenuItem(this,R.drawable.pushnotification,"Centro de mensajes");
        iuser=new ResideMenuItem(this,R.drawable.switchuser,"Cambio de usuario");
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        if(info==null){
            finish();
            Intent i= new Intent(getApplicationContext(),BtfLanding.class);
            startActivity(i);
            return;
        }
        try {
            ImageLoader loader;
            loader = new ImageLoader(this);
            ((TextView) resideMenu.getLeftMenuView().findViewById(R.id.nombre)).setText(info.getNombreContacto() != null ? AES.decrypt(info.getNombreContacto()).split(" ")[0] : "");

            ((TextView) resideMenu.getLeftMenuView().findViewById(R.id.contrato)).setText(info.getCvNumberAccount() != null ? AES.decrypt(info.getCvNumberAccount()): "");
            if (info.getFotoPerfil() != null) {
                String image = AES.decrypt(info.getFotoPerfil());
                if (!image.isEmpty()) {
                    loader.DisplayImage(image, (ImageView)resideMenu.getLeftMenuView().findViewById(R.id.profile).findViewById(R.id.imageView1));
                }
            }
            if (MainActivity.facebookImg != null) {
                loader.DisplayImage(MainActivity.facebookImg, (ImageView)resideMenu.getLeftMenuView().findViewById(R.id.profile).findViewById(R.id.imageView1));
                System.out.println(MainActivity.facebookImg);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        resideMenu.addMenuItem(ipago, ResideMenu.DIRECTION_LEFT);

        if(info.isLegacy()||info.isEsMigrado()){
            if(!info.isPaperless()) {

                resideMenu.addMenuItem(iPaper,ResideMenu.DIRECTION_LEFT);
            }

        }else{
            resideMenu.addMenuItem(iwifi, ResideMenu.DIRECTION_LEFT);
        }

        ipago.setOnClickListener(this);
        iwifi.setOnClickListener(this);
        iguia.setOnClickListener(this);
        iLegales.setOnClickListener(this);
        illamanos.setOnClickListener(this);
        ichatea.setOnClickListener(this);
        iPaper.setOnClickListener(this);
        inotifica.setOnClickListener(this);
        // create menu items;
        iuser.setOnClickListener(this);

        resideMenu.addMenuItem(iguia, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(iLegales, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(illamanos, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(ichatea, ResideMenu.DIRECTION_LEFT);
        findViewById(R.id.menuu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        resideMenu.addMenuItem(inotifica,ResideMenu.DIRECTION_LEFT);
        if(info.getCuentasAsociadas().size()>0){
            resideMenu.addMenuItem(iuser,ResideMenu.DIRECTION_LEFT);
        }
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            // Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //  Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
*/

    public void showMenu(View v){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        }else{
            drawer.openDrawer(GravityCompat.START);
        }
    }
    public void logout(View v){
        Intent i;
        new Delete().from(Usuario.class).execute();
        ((IzziMovilApplication) getApplication()).setCurrentUser(null);
        ((IzziMovilApplication) getApplication()).setLogged(false);
        i = new Intent(this, BtfLanding.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        return;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

        float moveFactor = (menu.getWidth() * slideOffset);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            frame.setTranslationX(moveFactor);
        }
        else
        {
            TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
            anim.setDuration(0);
            anim.setFillAfter(true);
            frame.startAnimation(anim);
            lastTranslate = moveFactor;
        }
    }



    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


}
