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
import com.google.gson.GsonBuilder;

import java.net.SocketTimeoutException;
import java.util.Map;

import telecom.televisa.com.izzi.AddCardActivity;
import telecom.televisa.com.izzi.EditAccountActivity;
import telecom.televisa.com.izzi.EdoCuentaActivity;
import telecom.televisa.com.izzi.PagoEstablecimientosActivity;
import telecom.televisa.com.izzi.PagosMainActivity;
import telecom.televisa.com.izzi.PaperlessActivity;
import telecom.televisa.com.izzi.PurchaseActivity;
import telecom.televisa.com.izzi.PushNotificationCenterActivity;
import telecom.televisa.com.izzi.R;
import telecom.televisa.com.izzi.RecuperaPass;
import telecom.televisa.com.izzi.RegistroPaso3;
import telecom.televisa.com.izzi.RegistroStep2Activity;
import telecom.televisa.com.izzi.Registro_main_activity;
import telecom.televisa.com.izzi.SMSConfirmaActivity;
import telecom.televisa.com.izzi.UserActivity;
import televisa.telecom.com.ws.IzziWS;

/**
 * Created by cevelez on 17/04/2015.
 */
public class PulltoRefreshAsyncResponse extends AsyncTask<Map<String,String>, Object, Object> {
    IzziRespondable respondTo=null;

    boolean encryptData=false;
    public PulltoRefreshAsyncResponse(IzziRespondable respondTo, boolean encryptData){
        this.respondTo=respondTo;
        this.encryptData=encryptData;
        // hacer algo generico

    }

    @Override
    protected Object doInBackground(Map<String,String>... params) {
         String metodo=params[0].get("METHOD");
        params[0].remove("METHOD");
        try {
            Map<String,String> infoMap=params[0];
            if(encryptData){
              for(Map.Entry<String, String> entry : infoMap.entrySet()){
                  entry.setValue(AES.encrypt(entry.getValue()));
              }
            }
            //Gson gson = new Gson();
           Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            //GsonBuilder gsb=new GsonBuilder();


            if(respondTo instanceof UserActivity){
                izziLoginResponse response=gson.fromJson((String) IzziWS.callWebService(params[0], metodo), izziLoginResponse.class);
                return response;
            }

        }catch(Exception e){
            e.printStackTrace();
            if(e instanceof SocketTimeoutException)
                return e;
            return null;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
        try{
            if(response instanceof Exception){
              respondTo.slowInternet();
                return;
            }
            respondTo.notifyChanges(response);
        }catch(Exception e){
            e.printStackTrace();

        }

    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        try{
        }catch(Exception e){}

    }
}
