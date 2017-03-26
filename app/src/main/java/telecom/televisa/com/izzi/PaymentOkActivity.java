package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.kissmetrics.sdk.KISSmetricsAPI;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziLoginResponse;


public class PaymentOkActivity extends IzziActivity implements IzziRespondable {
    IzziRespondable acty=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_ok);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en línea");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);

        Intent i=getIntent();
        ((TextView)findViewById(R.id.autorizacion)).setText(i.getExtras().getString("auth"));
        KISSmetricsAPI.sharedAPI().record("Pago realizado en Apps");
    }
    public void askDom(View v){
        showMenu(v);

    }
    public void showMenu(View v){
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        Map<String,String> mp=new HashMap<>();

        String user="";
        String password="";
        user=usuario.getUserName();
        password=usuario.getPassword();
        mp.put("METHOD","login");
        mp.put("user",user);
        mp.put("password",password);
        new AsyncResponse(this,true).execute(mp);
    }
   public void finishPayment(View v){
       showMenu(v);
   }

    @Override
    public void notifyChanges(Object response) {
        if(PagosMainActivity.parametros!=null){
            PagosMainActivity.parametros=null;
            showMenu(new View(this));
        }
        if(response==null){
            response= (Object)(new izziLoginResponse());
            ((izziLoginResponse)response).setIzziError("Error inesperado");
            ((izziLoginResponse)response).setIzziErrorCode("999");
            this.finish();
            return;
        }
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        new Delete().from(Usuario.class).execute();
        new Delete().from(PagosList.class).execute();
        if(((izziLoginResponse)response).getIzziErrorCode().isEmpty()){
            Usuario sr=((izziLoginResponse)response).getResponse();
            String user="";
            String password="";
            user=usuario.getUserName();
            password=usuario.getPassword();
            sr.setUserName(user);
            sr.setPassword(password);
            sr.setToken(((izziLoginResponse)response).getToken());
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
            this.finish();

            ((IzziMovilApplication)this.getApplication()).setCurrentUser(sr);
            ((IzziMovilApplication)this.getApplication()).setLogged(true);

        }else{
            // mostrar mensaje rosa de usuario o contraseña invalido si es el caso
            //si no popup
        }

    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);

    }
}
