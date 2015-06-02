package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;

import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 22/04/2015.
 */
public class MenuActivity extends Activity{
    protected int layout = R.layout.menu;
    protected int menu_width=0;
    protected int menu_height=0;
    protected LinearLayout container;
    protected ImageView menu_btn;
    private boolean isVisible=false;
    private boolean isOpen=false;
    private boolean isVisibleE=false;
    private boolean isOpenE=false;
    private int _xDelta;
    private int _xpos;
    private RelativeLayout rlsm;
    private int margen=0;
    public double anchoPantalla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        anchoPantalla = display.getWidth();
        setContentView(layout);
        container=(LinearLayout)findViewById(R.id.vista);
        resizeElements();
        initSpecialEfects();
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        if(info.isLegacy()){
            ((RelativeLayout)findViewById(R.id.dr_wifi)).setVisibility(RelativeLayout.GONE);
            if(info.isPaperless())
                ((RelativeLayout)findViewById(R.id.paperless)).setVisibility(RelativeLayout.GONE);

        }else{

                ((RelativeLayout)findViewById(R.id.paperless)).setVisibility(RelativeLayout.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        if(info.isLegacy()){
            ((RelativeLayout)findViewById(R.id.dr_wifi)).setVisibility(RelativeLayout.GONE);
            if(info.isPaperless())
                ((RelativeLayout)findViewById(R.id.paperless)).setVisibility(RelativeLayout.GONE);

        }else{

                ((RelativeLayout)findViewById(R.id.paperless)).setVisibility(RelativeLayout.GONE);
        }
    }

    private void initSpecialEfects() {
        // TODO Auto-generated method stub
        container.setClickable(true);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                final int X = (int) event.getRawX();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if(!(container.getLayoutParams() instanceof RelativeLayout.LayoutParams))
                            break;
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _xpos=0;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(!(container.getLayoutParams() instanceof RelativeLayout.LayoutParams))
                            break;
                        if(_xpos>menu_width/2){
                            RelativeLayout.LayoutParams lp=((RelativeLayout.LayoutParams)container.getLayoutParams());
                            lp.setMargins(menu_width, 0, -menu_width, 0);
                            container.setLayoutParams(lp);
                            isOpen=true;
                            isVisible=true;
                            if(rlsm!=null){rlsm.setVisibility(RelativeLayout.GONE);}
                        }else if(_xpos<=menu_width/2&&_xpos>=0){
                            RelativeLayout.LayoutParams lp=((RelativeLayout.LayoutParams)container.getLayoutParams());
                            lp.setMargins(0, 0, 0, 0);
                            if(rlsm!=null){rlsm.setVisibility(RelativeLayout.VISIBLE);}
                            isOpen=false;
                            isVisible=false;
                            container.setLayoutParams(lp);
                        }else if(_xpos<0){
                            if(rlsm!=null){
                                if(_xpos<=-menu_width/2){
                                    RelativeLayout.LayoutParams lp=((RelativeLayout.LayoutParams)container.getLayoutParams());
                                    lp.setMargins(-menu_width, 0, menu_width, 0);
                                    container.setLayoutParams(lp);
                                    isVisibleE=true;
                                    isOpenE=true;
                                }else{
                                    RelativeLayout.LayoutParams lp=((RelativeLayout.LayoutParams)container.getLayoutParams());
                                    lp.setMargins(0, 0, 0, 0);
                                    container.setLayoutParams(lp);
                                    isVisibleE=false;
                                    isOpenE=false;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!(container.getLayoutParams() instanceof RelativeLayout.LayoutParams))
                            break;
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
                        layoutParams.leftMargin = X-_xDelta<0?0:X-_xDelta;
                        if(layoutParams.leftMargin>0){
                            layoutParams.rightMargin = -(X-_xDelta);
                            if(rlsm!=null){rlsm.setVisibility(RelativeLayout.GONE);}
                        }else{
                            layoutParams.rightMargin = 0;
                            if(rlsm!=null){
                                rlsm.setVisibility(RelativeLayout.VISIBLE);

                                layoutParams.leftMargin = (X-_xDelta);
                                layoutParams.rightMargin = -(X-_xDelta);
                                System.out.println(layoutParams.rightMargin);
                                if(layoutParams.rightMargin>menu_width){
                                    layoutParams.leftMargin =(int)-(anchoPantalla-margen);
                                    layoutParams.rightMargin = (int)(anchoPantalla-margen);
                                }
                            }
                        }
                        if(layoutParams.leftMargin>menu_width){
                            layoutParams.leftMargin=menu_width;
                            layoutParams.rightMargin=-menu_width;
                        }
                        _xpos=layoutParams.leftMargin;
                        container.setLayoutParams(layoutParams);
                        break;

                }
                container.invalidate();
                return false;
            }
        });
    }
    public void showMenu(View v){

        if(!isVisible){
            if(rlsm!=null){rlsm.setVisibility(RelativeLayout.GONE);}
            container.postDelayed(new Runnable() {
                int i = 0;
                public void run() {
                    try{
                        RelativeLayout.LayoutParams lp=((RelativeLayout.LayoutParams)container.getLayoutParams());
                        i+=menu_width/4;
                        lp.setMargins(i, 0, -i, 0);
                        container.setLayoutParams(lp);
                        if(i<menu_width)
                            container.postDelayed(this, 10);
                        else{
                            lp.setMargins(menu_width, 0, -menu_width, 0);
                            isOpen=true;
                            container.setLayoutParams(lp);
                            container.removeCallbacks(this);
                        }
                    }catch(Exception e){

                    }
                }
            }, 1);
        }
        else{
            TranslateAnimation anim = new TranslateAnimation(menu_width,0 , 0, 0);
            anim.setDuration(150);
            container.startAnimation(anim);
            RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams)container.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            container.setLayoutParams(lp);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams)container.getLayoutParams();
                    lp.setMargins(0, 0, 0, 0);
                    isOpen=false;
                    if(rlsm!=null){rlsm.setVisibility(RelativeLayout.VISIBLE);}
                    container.setLayoutParams(lp);
                }
            });
        }
        isVisible=!isVisible;
    }
    private void resizeElements(){
        container=(LinearLayout)findViewById(R.id.vista);

        RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) ((RelativeLayout)findViewById(R.id.menu_container)).getLayoutParams();
        lp.width=(int)(anchoPantalla-anchoPantalla/6);
        menu_width=lp.width;
    }
    public void menuClick(View v){
        Intent i=null;
        if(!isOpen)
            return;

        switch(v.getId()){
            case R.id.logout:
                i=new Intent(getApplicationContext(),MainActivity.class);
                new Delete().from(Usuario.class).execute();
                ((IzziMovilApplication)getApplication()).setCurrentUser(null);
                ((IzziMovilApplication)getApplication()).setLogged(false);
                finish();
                break;
            case R.id.dr_wifi:
                i=new Intent(getApplicationContext(),DrWifiActivity.class);
                showMenu(v);
                break;
            case R.id.legales:
                i=new Intent(getApplicationContext(),LegalesActivity.class);
                showMenu(v);
                break;
            case R.id.paperless:
                i=new Intent(getApplicationContext(),PaperlessActivity.class);
                showMenu(v);
                break;
            case R.id.m_lugares:
                i=new Intent(getApplicationContext(),PagoEstablecimientosActivity.class);
                showMenu(v);
                break;
            case R.id.m_programacion:
                i=new Intent(getApplicationContext(),TvGuideActivity.class);
                showMenu(v);
                break;
            case R.id.chat:
                i=new Intent(getApplicationContext(),ChatActivity.class);
                showMenu(v);
                break;
            case R.id.callcenter:
                Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
                String telefono="";
                if(info.isLegacy()){
                    telefono="51699699";
                }else{
                    telefono="018001205000";
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telefono));
                startActivity(callIntent);
                showMenu(v);
                break;
            default:
                return;
        }
        if(i!=null) {
            startActivity(i);
        }
    }


}
