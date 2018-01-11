package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import java.util.HashMap;

import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziValidateResponse;


public class RegistroPaso3 extends IzziActivity implements IzziRespondable {
    HashMap<String,String> map;

    izziValidateResponse resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_paso3);
        Intent i =getIntent();
        resp= (izziValidateResponse)i.getSerializableExtra("response");
        map=(HashMap<String,String>)i.getSerializableExtra("user");
        try {
            ((TextView) findViewById(R.id.email)).setText(AES.decrypt(map.get("user")));
        }catch(Exception e){

        }
    }
    public void resend(View v){
        try {
            HashMap<String, String> mp = new HashMap<>();
            mp.put("METHOD", "registro/re-authorize");
            mp.put("account", map.get("account"));
            mp.put("type", AES.encrypt("0"));
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("registro confirmacion reenviar correo").putContentType("registro").putCustomAttribute("account",AES.decrypt(map.get("account"))));
            new AsyncResponse(this, false).execute(mp);
        }catch(Exception e){

        }
    }
    public void close(View v){
        Intent i = new Intent(this, BtfLanding.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        finish();
    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",4);
            return;
        }
        izziValidateResponse rs=(izziValidateResponse)response;
        if(!rs.getIzziErrorCode().isEmpty()){
            showError(rs.getIzziError(),0);
            return;
        }
        try {
            showError("Hemos enviado un correo electrónico a :" + AES.decrypt(map.get("user")), 0);
        }catch(Exception e){
            
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
}
