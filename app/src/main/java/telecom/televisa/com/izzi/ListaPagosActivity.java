package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.util.PagosListAdapter;


public class ListaPagosActivity extends Activity {
    ListView lv;
    List<PagosList> pagos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pagos);
        ((TextView)findViewById(R.id.h_title)).setText("Historial de pagos");
        initList();
    }

    private void initList(){
        lv=(ListView)findViewById(R.id.lista_pagos);
        IzziMovilApplication app=(IzziMovilApplication)getApplication();
        ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.GONE);
        pagos=app.getCurrentUser().getPagos();
        lv.setVisibility(ListView.VISIBLE);
        if(pagos==null){
            lv.setVisibility(ListView.GONE);
            ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.VISIBLE);
        }else if(pagos.size()==0){
            lv.setVisibility(ListView.GONE);
            ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.VISIBLE);
        }else {
            PagosListAdapter adapter = new PagosListAdapter(this, R.layout.pagos_list_item, pagos, 0);
            lv.setAdapter(adapter);
        }
    }

    public void closeView(View v){
        this.finish();
    }
}
