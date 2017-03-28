package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.model.ExtrasInt;
import televisa.telecom.com.model.ExtrasTv;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.MobileExtrasResponse;

public class PurchaseActivity extends IzziActivity implements IzziRespondable {
    List<ExtrasInt> complementosINT;
    List<ExtrasTv> complementosTV;
    Usuario info;
    String extras="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        info=((IzziMovilApplication)getApplication()).getCurrentUser();
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        ((TextView)findViewById(R.id.h_title)).setText("Resumen");
        complementosINT=ServiciosActivity.complementosINT;
        complementosTV=ServiciosActivity.complementosTV;
        initList();
    }
    private void initList(){
        double price=0.0;
        LinearLayout ll=(LinearLayout)findViewById(R.id.containerextras);
        if(complementosINT!=null)
            for(ExtrasInt ei:complementosINT){
                if(ei.isSelected()){
                    TextView tv = new TextView(this);
                    tv.setText("- " +ei.getName()+"          "+"$"+ei.getPrice());
                    tv.setTextSize(22);
                    tv.setPadding(20,10,10,10);
                    tv.setTextColor(0xff000000);
                    extras+=ei.getEx_id()+",";
                    price+=Double.parseDouble(ei.getPrice());
                    ll.addView(tv);
                }
            }
        if(complementosTV!=null)
            for(ExtrasTv ei:complementosTV){
                if(ei.isSelected()){
                    TextView tv = new TextView(this);
                    tv.setText("- " +ei.getName()+"          "+"$"+ei.getPrice());
                    tv.setTextSize(22);
                    tv.setPadding(20,10,10,10);
                    tv.setTextColor(0xff000000);
                    extras+=ei.getEx_id()+",";
                    price+=Double.parseDouble(ei.getPrice());
                    ll.addView(tv);
                }
            }
        ((TextView)findViewById(R.id.price)).setText("por $"+price+" + al mes");
    }
    public void showMenu(View v){
        finish();
    }

    public void checkout(View v){
        Map<String,String> mp=new HashMap<>();

        try {
            mp.put("METHOD", "mobileUpdateService/");
            mp.put("account", AES.decrypt(info.getCvNumberAccount()));

            mp.put("exid", extras.substring(0,extras.length()-1));
            mp.put("user", info.getUserName());
            mp.put("pass", info.getPassword());
            mp.put("sms", "1");
            mp.put("push","1");
            mp.put("device","1");
            new AsyncResponse(this,true).execute(mp);
        }catch(Exception e){

        }
    }

    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",20);
            return;
        }
        MobileExtrasResponse mer=(MobileExtrasResponse)response;
        if(mer.getIzziErrorCode()!=null&&!mer.getIzziErrorCode().isEmpty()){
            showError(mer.getIzziError(),20);
        }
        else{
            showError("Estamos procesando tu solicitud",0);
            setResult(6);
            finish();
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
}
