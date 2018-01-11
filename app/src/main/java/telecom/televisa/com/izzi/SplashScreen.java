package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncLoginUpdate;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;




public class SplashScreen extends Activity  {
    private Usuario currentUser;
    private static final long SPLASH_SCREEN_DELAY = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



        AppsFlyerLib.setAppsFlyerKey("PNd7NVL8bDNQjHXJYj7dVH");
        AppsFlyerLib.sendTracking(getApplicationContext());



        List<Usuario> usr= new Select().from(Usuario.class).execute();
        if(usr!=null) {
            if (usr.size()>0) {
                currentUser=usr.get(0);
                ((IzziMovilApplication)getApplication()).setLogged(true);
                List<PagosList> pagos=new Select().from(PagosList.class).execute();
                currentUser.setPagos(pagos);
                Map<String,String> mp=new HashMap<>();
                //if para refrescar cuenta hijo o principal

                if(currentUser.getParentAccount().isEmpty()) {
                    mp.put("METHOD", "login");
                    mp.put("user", currentUser.getUserName());
                    mp.put("password", currentUser.getPassword());
                }else{
                    try {
                        mp.put("METHOD", "login/getChildAccountInfo");
                        mp.put("user", currentUser.getUserName());
                        mp.put("password", currentUser.getPassword());
                        mp.put("childAccount", AES.decrypt(currentUser.getCvNumberAccount()));
                        mp.put("type", currentUser.getParentType());
                    }catch(Exception e){

                    }
                }
                ((IzziMovilApplication)getApplication()).setCurrentUser(currentUser);
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB)
                  new AsyncLoginUpdate(((IzziMovilApplication)getApplication()),true).execute(mp);
                else
                    new AsyncLoginUpdate(((IzziMovilApplication)getApplication()),true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mp);
                if(currentUser.isExtrasTelefono()){
                    String [] extra=currentUser.getExtraTelefono().split("##");
                    List<String> ex=new ArrayList<>();
                    for (int i=0;i<extra.length;i++){
                        ex.add(extra[i]);
                    }
                    currentUser.setDataExtrasTelefono(ex);
                }
                if(currentUser.isExtrasInternet()){
                    String [] extra=currentUser.getExtraInternet().split("##");
                    List<String> ex=new ArrayList<>();
                    for (int i=0;i<extra.length;i++){
                        ex.add(extra[i]);
                    }
                    currentUser.setDataExtrasInternet(ex);
                }
                if(currentUser.isExtrasVideo()){
                    String [] extra=currentUser.getExtraVideo().split("##");
                    List<String> ex=new ArrayList<>();
                    for (int i=0;i<extra.length;i++){
                        ex.add(extra[i]);
                    }
                    currentUser.setDataExtrasVideo(ex);
                }
                ((IzziMovilApplication)getApplication()).setCurrentUser(currentUser);
            }
            else{
                ((IzziMovilApplication)getApplication()).setCurrentUser(null);
                currentUser=null;
                ((IzziMovilApplication)getApplication()).setLogged(false);
            }
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        SplashScreen.this, BtfLanding.class);
                startActivity(mainIntent);
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
