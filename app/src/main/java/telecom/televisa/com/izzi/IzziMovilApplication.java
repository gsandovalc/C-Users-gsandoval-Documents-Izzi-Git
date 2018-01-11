package telecom.televisa.com.izzi;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.crashlytics.android.Crashlytics;
import com.kissmetrics.sdk.KISSmetricsAPI;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import televisa.telecom.com.model.Cuentas;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 25/04/2015.
 */
public class IzziMovilApplication extends com.activeandroid.app.Application {
    private boolean isLogged = false;
    private Usuario currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        KISSmetricsAPI.sharedAPI("831d7f420b079a7e41295f416c7141393cec4032", getApplicationContext());
        KISSmetricsAPI.sharedAPI().autoRecordInstalls();
        KISSmetricsAPI.sharedAPI().autoSetAppProperties();
        KISSmetricsAPI.sharedAPI().autoSetHardwareProperties();
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
    public Usuario getCurrentUser(){
        List<Usuario> usr= new Select().from(Usuario.class).execute();
        if(usr!=null) {
            if (usr.size() > 0) {
                currentUser = usr.get(0);
                setLogged(true);
                List<PagosList> pagos = new Select().from(PagosList.class).execute();
                currentUser.setPagos(pagos);
                List<Cuentas> cuentas=new Select().from(Cuentas.class).execute();
                currentUser.setCuentasAsociadas(cuentas);
                if (currentUser.isExtrasTelefono()) {
                    String[] extra = currentUser.getExtraTelefono().split("##");
                    List<String> ex = new ArrayList<>();
                    for (int i = 0; i < extra.length; i++) {
                        ex.add(extra[i]);
                    }
                    currentUser.setDataExtrasTelefono(ex);
                }
                if (currentUser.isExtrasInternet()) {
                    String[] extra = currentUser.getExtraInternet().split("##");
                    List<String> ex = new ArrayList<>();
                    for (int i = 0; i < extra.length; i++) {
                        ex.add(extra[i]);
                    }
                    currentUser.setDataExtrasInternet(ex);
                }
                if (currentUser.isExtrasVideo()) {
                    String[] extra = currentUser.getExtraVideo().split("##");
                    List<String> ex = new ArrayList<>();
                    for (int i = 0; i < extra.length; i++) {
                        ex.add(extra[i]);
                    }
                    currentUser.setDataExtrasVideo(ex);
                }
            } else {
                setLogged(false);
                return null;

            }
        }else{
            setLogged(false);
                return null;
        }
        return currentUser;
    }
    public void setCurrentUser(Usuario u){
        currentUser=u;
    }
}
