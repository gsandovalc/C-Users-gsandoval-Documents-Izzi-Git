package televisa.telecom.com.util;

import android.app.Application;
import android.app.Dialog;
import android.os.AsyncTask;

import com.activeandroid.query.Delete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import telecom.televisa.com.izzi.IzziMovilApplication;
import telecom.televisa.com.izzi.UserActivity;
import televisa.telecom.com.model.Cuentas;
import televisa.telecom.com.model.ExtrasInt;
import televisa.telecom.com.model.ExtrasTv;
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
    static String USR="";
    static String PASS="";
    public static UserActivity refresca;
    public AsyncLoginUpdate(Application respondTo, boolean encryptData){
        this.respondTo=respondTo;
        this.encryptData=encryptData;
        // hacer algo generico

    }

    @Override
    protected Object doInBackground(Map<String,String>... params) {
        String metodo=params[0].get("METHOD");
        params[0].remove("METHOD");
        USR=new String(params[0].get("user"));
        PASS=new String(params[0].get("password"));
        try {
            Map<String,String> infoMap=params[0];
            if(encryptData){
                for(Map.Entry<String, String> entry : infoMap.entrySet()){
                    entry.setValue(AES.encrypt(entry.getValue()));
                }
            }
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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
            String user="";
            String password="";

            String cuenta="";
            String type="";
            IzziMovilApplication res=(IzziMovilApplication)respondTo;
            try {
                user = USR;
                password = PASS;
                cuenta = res.getCurrentUser().getParentAccount().equals("") ? "" : res.getCurrentUser().getParentAccount();
                type=res.getCurrentUser().getParentType().equals("") ? "" : res.getCurrentUser().getParentType();
            }catch(Exception e){

                e.printStackTrace();
            }
            new Delete().from(Usuario.class).execute();
            new Delete().from(PagosList.class).execute();
            new Delete().from(Cuentas.class).execute();
            new Delete().from(ExtrasTv.class).execute();
            new Delete().from(ExtrasInt.class).execute();
            Usuario sr=((izziLoginResponse)response).getResponse();





            sr.setUserName(user);
            sr.setPassword(password);
            sr.setParentAccount(cuenta);
            sr.setParentType(type);
            if(sr.getPagos()!=null)
                for(PagosList pago:sr.getPagos()){
                    pago.save();
                }
            if(sr.getCuentasAsociadas()!=null){
                for(Cuentas ac:sr.getCuentasAsociadas())
                    ac.save();
            }
            if(sr.getComplementosTV()!=null)
                for(ExtrasTv et:sr.getComplementosTV())
                    et.save();
            if(sr.getComplementosINT()!=null)
                for(ExtrasInt et:sr.getComplementosINT())
                    et.save();
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
            if(refresca!=null)
                refresca.refresh();

        }else{
            //llenar una bandera de error en el login async
        }

    }


    @Override
    protected void onCancelled() {
        super.onCancelled();


    }
}