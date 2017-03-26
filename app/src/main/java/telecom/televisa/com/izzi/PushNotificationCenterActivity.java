package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.model.Notification;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Push;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.PagosListAdapter;
import televisa.telecom.com.util.PushListAdapter;
import televisa.telecom.com.util.izziPushResponse;


public class PushNotificationCenterActivity extends IzziActivity implements IzziRespondable {
    ListView lv;
    List<Notification> push;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification_center);
        ((TextView)findViewById(R.id.h_title)).setText("Centro de notificaciónes");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        Map<String,String> mp=new HashMap<>();
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            mp.put("METHOD", "push/history");
            mp.put("account", AES.decrypt(usuario.getCvNumberAccount()));
            new AsyncResponse(this,true).execute(mp);

        }catch(Exception e){

        }

    }
    public void showMenu(View v){
        finish();
    }
    private void initList(){
        lv=(ListView)findViewById(R.id.lista_pagos);
        ((LinearLayout)findViewById(R.id.sinpagos)).setVisibility(LinearLayout.GONE);
        lv.setVisibility(ListView.VISIBLE);
        if(push==null){
            lv.setVisibility(ListView.GONE);
            ((LinearLayout)findViewById(R.id.sinpagos)).setVisibility(LinearLayout.VISIBLE);
        }else if(push.size()==0){
            lv.setVisibility(ListView.GONE);
            ((LinearLayout)findViewById(R.id.sinpagos)).setVisibility(LinearLayout.VISIBLE);
        }else {
            PushListAdapter adapter = new PushListAdapter(this, R.layout.push_list_item, push, 0);
            lv.setAdapter(adapter);
        }

        for(Notification n:push){
            if(n.getViewed()==0){
                Map<String,String> mp=new HashMap<>();
                mp.put("METHOD", "push/view");
                mp.put("id", n.getId()+"");
                new AsyncResponse(this,true).execute(mp);
            }
        }
    }

    private void markAsRead(){

    }
    public void closeView(View v){
        this.finish();
    }


    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",999);
            return;
        }
        if(response instanceof Integer){
            return;
        }
        push=((izziPushResponse)response).getResponse().getPush();
        initList();
    }
}
