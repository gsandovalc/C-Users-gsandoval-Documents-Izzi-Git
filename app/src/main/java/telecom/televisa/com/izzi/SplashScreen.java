package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
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
import televisa.telecom.com.util.AsyncLoginUpdate;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;


public class SplashScreen extends Activity  {
    private Usuario currentUser;
    private static final long SPLASH_SCREEN_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppsFlyerLib.setAppsFlyerKey("PNd7NVL8bDNQjHXJYj7dVH");
        AppsFlyerLib.sendTracking(getApplicationContext());

        setContentView(R.layout.splash_screen);
        List<Usuario> usr= new Select().from(Usuario.class).execute();
        if(usr!=null) {
            if (usr.size()>0) {
                currentUser=usr.get(0);
                ((IzziMovilApplication)getApplication()).setLogged(true);
                List<PagosList> pagos=new Select().from(PagosList.class).execute();
                currentUser.setPagos(pagos);
                Map<String,String> mp=new HashMap<>();
                mp.put("METHOD","login");
                mp.put("user",currentUser.getUserName());
                mp.put("password",currentUser.getPassword());
                ((IzziMovilApplication)getApplication()).setCurrentUser(currentUser);
                new AsyncLoginUpdate(((IzziMovilApplication)getApplication()),true).execute(mp);
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
                        SplashScreen.this, MainActivity.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
