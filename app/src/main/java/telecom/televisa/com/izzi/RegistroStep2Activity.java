package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;

import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziValidateResponse;


public class RegistroStep2Activity extends IzziActivity implements IzziRespondable {
    int selected=1;
    HashMap<String,String> map;

    izziValidateResponse resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_step2);
        Intent i=getIntent();
        LinearLayout r1=(LinearLayout)findViewById(R.id.telcont);
        LinearLayout r2=(LinearLayout)findViewById(R.id.smsc);
        try{
           resp= (izziValidateResponse)i.getSerializableExtra("response");
           map=(HashMap<String,String>)i.getSerializableExtra("user");
            if(resp.getResponse().getSmsEnabled()==null||resp.getResponse().getSmsEnabled().isEmpty()){
                r2.setVisibility(View.GONE);
            }
            }catch(Exception e){

        }
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.radioon1).setVisibility(View.VISIBLE);
                findViewById(R.id.radioon2).setVisibility(View.GONE);
                selected=1;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.radioon1).setVisibility(View.GONE);
                findViewById(R.id.radioon2).setVisibility(View.VISIBLE);
                selected=2;
            }
        });
    }
    public void registro(View v){
        try {
            HashMap<String, String> mp = new HashMap<>();
            mp.put("METHOD", "registro");
            mp.put("code", resp.getResponse().getCode());
            mp.put("type", AES.encrypt(selected == 1 ? "0" : "1"));
            new AsyncResponse(this, false).execute(mp);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void closeView(View v){
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
        setResult(207);
        finish();
        Intent i;
        if(selected==1)
         i=new Intent(this,RegistroPaso3.class);
        else
           i=new Intent(this,SMSConfirmaActivity.class);
        i.putExtra("response",resp);
        i.putExtra("user",(HashMap)map);
        startActivity(i);
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
}
