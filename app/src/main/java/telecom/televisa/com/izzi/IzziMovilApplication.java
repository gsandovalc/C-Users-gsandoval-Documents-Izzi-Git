package telecom.televisa.com.izzi;

import android.app.Application;

import com.activeandroid.query.Select;
import com.kissmetrics.sdk.KISSmetricsAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AsyncLoginUpdate;

/**
 * Created by cevelez on 25/04/2015.
 */
public class IzziMovilApplication extends com.activeandroid.app.Application {
    private boolean isLogged = false;
    private Usuario currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
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
        return currentUser;
    }
    public void setCurrentUser(Usuario u){
        currentUser=u;
    }
}
