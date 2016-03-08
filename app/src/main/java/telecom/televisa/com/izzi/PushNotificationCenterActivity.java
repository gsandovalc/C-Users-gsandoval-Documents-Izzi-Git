package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Push;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.PagosListAdapter;
import televisa.telecom.com.util.PushListAdapter;


public class PushNotificationCenterActivity extends Activity {
    ListView lv;
    List<Push> push;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification_center);
        ((TextView)findViewById(R.id.h_title)).setText("Centro de notificaciónes");
        initList();
    }

    private void initList(){
        lv=(ListView)findViewById(R.id.lista_pagos);
        IzziMovilApplication app=(IzziMovilApplication)getApplication();
        ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.GONE);
        push=new Select().from(Push.class).where("correo=?",app.getCurrentUser().getUserName()).orderBy("fecha").execute();
        lv.setVisibility(ListView.VISIBLE);
        if(push==null){
            lv.setVisibility(ListView.GONE);
            ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.VISIBLE);
        }else if(push.size()==0){
            lv.setVisibility(ListView.GONE);
            ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.VISIBLE);
        }else {
            PushListAdapter adapter = new PushListAdapter(this, R.layout.push_list_item, push, 0);
            lv.setAdapter(adapter);
        }
    }
    public void closeView(View v){
        this.finish();
    }
}
