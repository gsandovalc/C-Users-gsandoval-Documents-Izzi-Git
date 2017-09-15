package telecom.televisa.com.izzi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;

public class MiradaTroubleShoot extends IzziActivity implements IzziRespondable {
    String serie;
    String problema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirada_trouble_shoot);

        int problem=getIntent().getIntExtra("problema",0);
        serie=getIntent().getStringExtra("serie");
        findViewById(R.id.lastStep).setVisibility(View.GONE);
        switch (problem){
            case 1:
                problema="No tengo audio";
                findViewById(R.id.problema1).setVisibility(View.VISIBLE);
                break;
            case 2:
                problema="No tengo señal";
                findViewById(R.id.problema2).setVisibility(View.VISIBLE);
                break;
            case 8:
                problema="Mi equipo no enciende";
                findViewById(R.id.problema3).setVisibility(View.VISIBLE);
                findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
                break;
            case 3:
                problema="La guía no tiene información";
                findViewById(R.id.problema4).setVisibility(View.VISIBLE);

                break;
            case 4:
                problema="Sin resultados en catálogo";
                findViewById(R.id.problema4).setVisibility(View.VISIBLE);

                break;
            case 5:
                problema="Probema en uno o más contenidos";
                findViewById(R.id.problema5).setVisibility(View.VISIBLE);

                break;
            case 6:
                problema="No puedo visualizar TV en vivo";
                findViewById(R.id.problema6).setVisibility(View.VISIBLE);
                findViewById(R.id.lastStep).setVisibility(View.VISIBLE);

                break;
            case 7:
                problema="No puedo visualizar On Demand";
                findViewById(R.id.problema6).setVisibility(View.VISIBLE);
                findViewById(R.id.lastStep).setVisibility(View.VISIBLE);

                break;
            case 9:
                problema="No recuerdo mi PIN";
                findViewById(R.id.problema7).setVisibility(View.VISIBLE);
                break;
            case 10:
                problema="Ya pagué y sigo sin servicio";
                findViewById(R.id.problema8).setVisibility(View.VISIBLE);
                break;


        }
        ((TextView)findViewById(R.id.h_title)).setText(problema);
    }

    @Override
    public void notifyChanges(Object response) {
        showError("Su cambio ha sido procesado para que tenga efecto podrian pasar hasta 5 minutos ",0);

    }

    @Override
    public void slowInternet() {

    }

    public void allChannels(View v){
        findViewById(R.id.audioTodos).setVisibility(View.VISIBLE);
        findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        findViewById(R.id.audioalgunos).setVisibility(View.GONE);
    }
    public void someChannels(View v){
        findViewById(R.id.audioTodos).setVisibility(View.GONE);
        findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        findViewById(R.id.audioalgunos).setVisibility(View.VISIBLE);
    }

    public void allChannels2(View v){
        findViewById(R.id.senalTodos).setVisibility(View.VISIBLE);
        findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        findViewById(R.id.senalAlgunos).setVisibility(View.GONE);
    }
    public void someChannels2(View v){
        findViewById(R.id.senalTodos).setVisibility(View.GONE);
        findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        findViewById(R.id.senalAlgunos).setVisibility(View.VISIBLE);
    }

    public void nover(View v){
        findViewById(R.id.otrosprob).setVisibility(View.GONE);
        findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        findViewById(R.id.noverlo).setVisibility(View.VISIBLE);
    }

    public void otros(View v){
        findViewById(R.id.noverlo).setVisibility(View.GONE);
        findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        findViewById(R.id.otrosprob).setVisibility(View.VISIBLE);
    }

    public void sendRestart(View v){
        try {
            Map<String,String> parametross=new HashMap<>();
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            parametross.put("METHOD", "videoManager/sendCommand");
            parametross.put("account", info.getCvNumberAccount());
            parametross.put("user", AES.encrypt(info.getUserName()));
            parametross.put("command", AES.encrypt("1"));
            parametross.put("serial", AES.encrypt(serie));
            parametross.put("pass", AES.encrypt(info.getPassword()));
            new AsyncResponse(this, false).execute(parametross);
            findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        }catch(Exception e){

        }
    }

    public void sendRestartPIN(View v){
        try {
            Map<String,String> parametross=new HashMap<>();
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            parametross.put("METHOD", "videoManager/sendCommand");
            parametross.put("account", info.getCvNumberAccount());
            parametross.put("user", AES.encrypt(info.getUserName()));
            parametross.put("command", AES.encrypt("3"));
            parametross.put("serial", AES.encrypt(serie));
            parametross.put("pass", AES.encrypt(info.getPassword()));
            new AsyncResponse(this, false).execute(parametross);
            findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        }catch(Exception e){

        }
    }

    public void sendRestartPay(View v){
        try {
            Map<String,String> parametross=new HashMap<>();
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            parametross.put("METHOD", "videoManager/sendCommand");
            parametross.put("account", info.getCvNumberAccount());
            parametross.put("user", AES.encrypt(info.getUserName()));
            parametross.put("command", AES.encrypt("4"));
            parametross.put("serial", AES.encrypt(serie));
            parametross.put("pass", AES.encrypt(info.getPassword()));
            new AsyncResponse(this, false).execute(parametross);
            findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        }catch(Exception e){

        }
    }

    public void sendRefresh(View v){
        try {
            Map<String,String> parametross=new HashMap<>();
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            parametross.put("METHOD", "videoManager/sendCommand");
            parametross.put("account", info.getCvNumberAccount());
            parametross.put("user", AES.encrypt(info.getUserName()));
            parametross.put("command", AES.encrypt("2"));
            parametross.put("serial", AES.encrypt(serie));
            parametross.put("pass", AES.encrypt(info.getPassword()));
            new AsyncResponse(this, false).execute(parametross);
            findViewById(R.id.lastStep).setVisibility(View.VISIBLE);
        }catch(Exception e){

        }
    }
    public void goToChat(View v){
        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
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
