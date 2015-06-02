package televisa.telecom.com.util;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.activeandroid.query.Delete;
import com.google.gson.Gson;

import java.util.Map;

import telecom.televisa.com.izzi.IzziMovilApplication;
import telecom.televisa.com.izzi.R;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.ws.IzziWS;

/**
 * Created by cevelez on 26/04/2015.
 */
public class AsyncLoginUpdate extends AsyncTask<Map<String,String>, Object, Object> {
    Application respondTo=null;
    Dialog loadingOverlay;
    boolean encryptData=false;
    public AsyncLoginUpdate(Application respondTo, boolean encryptData){
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
            Gson gson = new Gson();
            izziLoginResponse response = gson.fromJson((String) IzziWS.callWebService(params[0], metodo), izziLoginResponse.class);

            return response;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
        if(response==null)
            return;
        izziLoginResponse r=((izziLoginResponse)response);
        if(r.getIzziErrorCode().isEmpty()){
            new Delete().from(Usuario.class).execute();
            new Delete().from(PagosList.class).execute();
            Usuario sr=((izziLoginResponse)response).getResponse();
            String user="";
            String password="";
            IzziMovilApplication res=(IzziMovilApplication)respondTo;
            try {
                user = res.getCurrentUser().getUserName();
                password = res.getCurrentUser().getPassword();
            }catch(Exception e){}
            sr.setUserName(user);
            sr.setPassword(password);
            if(sr.getPagos()!=null)
                for(PagosList pago:sr.getPagos()){
                    pago.save();
                }
            if(sr.isExtrasVideo()){
                String extra="";
                for(String complemento:sr.getDataExtrasVideo()){
                    extra=complemento+"##"+extra;
                }
                sr.setExtraVideo(extra);
            }
            if(sr.isExtrasTelefono()){
                String extra="";
                for(String complemento:sr.getDataExtrasTelefono()){
                    extra=complemento+"##"+extra;
                }
                sr.setExtraTelefono(extra);
            }
            if(sr.isExtrasInternet()){
                String extra="";
                for(String complemento:sr.getDataExtrasInternet()){
                    extra=complemento+"##"+extra;
                }
                sr.setExtraInternet(extra);
            }
            sr.save();
            res.setCurrentUser(sr);
        }else{
            //llenar una bandera de error en el login async
        }

    }


    @Override
    protected void onCancelled() {
        super.onCancelled();


    }
}