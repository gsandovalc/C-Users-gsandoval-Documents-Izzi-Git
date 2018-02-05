package telecom.televisa.com.izzi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;

public class PhoneTroubleShootActivity extends IzziActivity implements IzziRespondable{
    String problema="";
    boolean restrt=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_trouble_shoot);
        problema=getIntent().getStringExtra("problema");
        ((TextView)findViewById(R.id.h_title)).setText(problema);
        switch(getIntent().getIntExtra("caso",0)){
            case 1:
                ((LinearLayout)findViewById(R.id.problema1)).setVisibility(View.VISIBLE);
                break;
            case 2:
                ((LinearLayout)findViewById(R.id.problema2)).setVisibility(View.VISIBLE);
                break;
            case 3:
                ((LinearLayout)findViewById(R.id.problema3)).setVisibility(View.VISIBLE);
                break;
            case 4:
                ((LinearLayout)findViewById(R.id.problema4)).setVisibility(View.VISIBLE);
                break;

        }
        if(((IzziMovilApplication)getApplication()).getCurrentUser()==null){
            finish();
            return;
        }
        if(!((IzziMovilApplication)getApplication()).getCurrentUser().isDisplayWifiInfo())
            findViewById(R.id.rest).setVisibility(View.GONE);
    }


    public void restart(View v){
        try {
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","wifiManager/update");
            mp.put("account", info.getCvNumberAccount());
            mp.put("user", AES.encrypt(info.getUserName()));
            mp.put("pass",AES.encrypt(info.getPassword()));
            mp.put("cmaddrs",info.getCmaddrs());
            mp.put("cmd",AES.encrypt("4"));
            mp.put("value",AES.encrypt(""));
            new AsyncResponse(this,false).execute(mp);
            restrt=true;

        }catch(Exception e){

        }
    }

    @Override
    public void notifyChanges(Object response) {
        if(response!=null){
            showError("Tu modem se está reiniciando",0  );
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }


    public void goToChat(View v){
        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
        i.putExtra("asunto",problema+(restrt?"-restart":""));

        startActivity(i);
    }
    public void call(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        String telefono="";
        if(info.isLegacy()){
            telefono="018001205000";
        }else{
            telefono="018001205000";
            System.out.println("Es izzi");
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono));
        startActivity(callIntent);
    }
}
