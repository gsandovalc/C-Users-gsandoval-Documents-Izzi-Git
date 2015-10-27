package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;


public class RecuperaFinActivity extends Activity implements IzziRespondable{
    String account;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_fin);
        Intent i=getIntent();
        TextView tv=(TextView)findViewById(R.id.mensaje);

        account=i.getStringExtra("account");

        type=i.getStringExtra("type");
        if(type.equals("1")){
            tv.setText("Tu contraseña sera enviada al siguiente correo electrónico "+i.getStringExtra("response").replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*"));
        }else{
            tv.setText("Tu contraseña sera enviada al siguiente número celular "+i.getStringExtra("response").replaceAll("\\b(\\d{4})\\d+(\\d)", "$1*****$2"));
        }
    }

    public void closeView(View v){
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

    }

    public void fin(View v){
        try {
            Map<String, String> mp = new HashMap<>();
            mp.put("METHOD", "recupera/fin");
            mp.put("account", AES.encrypt(account));
            mp.put("type", AES.encrypt(type));
            mp.put("email", "hola");
            new AsyncResponse(this, false).execute(mp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void notifyChanges(Object response) {
        setResult(233);
        Intent i = new Intent(this, BtfLanding.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }
}
