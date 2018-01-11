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


public class SMSConfirmaActivity extends IzziActivity implements IzziRespondable {
    HashMap<String,String> map;
    izziValidateResponse resp;
    int action=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsconfirma);
        Intent i =getIntent();
        resp= (izziValidateResponse)i.getSerializableExtra("response");
        map=(HashMap<String,String>)i.getSerializableExtra("user");
        try {
            ((TextView) findViewById(R.id.email)).setText("*****"+" "+AES.decrypt(resp.getResponse().getSmsEnabled()).substring(6));
        }catch(Exception e){

        }
    }
    public void resend(View v){
        try {
            action=0;
            HashMap<String, String> mp = new HashMap<>();
            mp.put("METHOD", "registro/re-authorize");
            mp.put("account", map.get("account"));
            mp.put("type", AES.encrypt("1"));
            new AsyncResponse(this, false).execute(mp);
        }catch(Exception e){

        }
    }
    public void close(View v){

        //aqui se debe de validar la cuenta
        String code=((TextView)findViewById(R.id.codigo)).getText().toString();
        if(code.isEmpty()) {
            showError("El campo código es obligatorio", 0);
            return;
        }
        if(code.length()>5){
            showError("El código no esta en el formato correcto", 0);
            return;
        }
        action=1;
        try {
            HashMap<String, String> mp = new HashMap<>();
            mp.put("METHOD", "registro/confirma");
            mp.put("account", map.get("account"));
            mp.put("verification", AES.encrypt(code));
            new AsyncResponse(this, false).execute(mp);
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("registro confirmacion  sms").putContentType("registro").putCustomAttribute("account",AES.decrypt(map.get("account"))));



        }catch(Exception e){

        }

    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",4);
            return;
        }
        if(action==0) {
            izziValidateResponse rs = (izziValidateResponse) response;
            if (!rs.getIzziErrorCode().isEmpty()) {
                showError(rs.getIzziError(), 0);
                return;
            }
            try {
                showError("El código se envió de manera correcta", 0);
            } catch (Exception e) {

            }
        }else{
            izziValidateResponse rs = (izziValidateResponse) response;
            if (!rs.getIzziErrorCode().isEmpty()) {
                showError(rs.getIzziError(), 0);
                return;
            }
            Intent i = new Intent(this, BtfLanding.class);
            startActivity(i);
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            finish();
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
}
