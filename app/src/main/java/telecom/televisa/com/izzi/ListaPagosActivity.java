package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.PagosListAdapter;


public class ListaPagosActivity extends IzziActivity  implements IzziRespondable {
    ListView lv;
    List<PagosList> pagos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pagos);
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);

        ((TextView)findViewById(R.id.h_title)).setText("Historial de pagos");
        initList();
    }
    public void showMenu(View v){
        finish();
    }
    private void initList(){
        lv=(ListView)findViewById(R.id.lista_pagos);
        IzziMovilApplication app=(IzziMovilApplication)getApplication();
        ((LinearLayout)findViewById(R.id.sinpagos)).setVisibility(LinearLayout.GONE);
        if(app.getCurrentUser()==null){
            finish();
            return;
        }

        pagos=app.getCurrentUser().getPagos();
        lv.setVisibility(ListView.VISIBLE);
        if(pagos==null){
            lv.setVisibility(ListView.GONE);
            ((LinearLayout)findViewById(R.id.sinpagos)).setVisibility(LinearLayout.VISIBLE);
        }else if(pagos.size()==0){
            lv.setVisibility(ListView.GONE);
            ((LinearLayout)findViewById(R.id.sinpagos)).setVisibility(LinearLayout.VISIBLE);
        }else {
            PagosListAdapter adapter = new PagosListAdapter(this, R.layout.pagos_list_item, pagos, 0,this);
            lv.setAdapter(adapter);
        }
    }
    public void edocuenta(View v){

      finish();
    }
    public void sendBill(View v){
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        Map<String,String> mp=new HashMap<>();
        Map <String,String> meses=new HashMap<>();
        meses.put("ENERO","1");
        meses.put("FEBRERO","2");
        meses.put("MARZO","3");
        meses.put("ABRIL","4");
        meses.put("MAYO","5");
        meses.put("JUNIO","6");
        meses.put("JULIO","7");
        meses.put("AGOSTO","8");
        meses.put("SEPTIEMBRE","9");
        meses.put("OCTUBRE","10");
        meses.put("NOVIEMBRE","11");
        meses.put("DICIEMBRE","12");

        try {
            mp.put("METHOD", "estado/send");
            mp.put("user", usuario.getUserName());
            mp.put("cuenta", AES.decrypt(usuario.getCvNumberAccount()));
            mp.put("month", meses.get(AES.decrypt(UserActivity.estado.getResponse().getMes())));
            String ano= Calendar.getInstance().get(Calendar.YEAR)+"";
            if(Integer.parseInt(meses.get(AES.decrypt(UserActivity.estado.getResponse().getMes())))==12){
                if(Calendar.getInstance().get(Calendar.MONTH)==0){
                    ano=(Calendar.getInstance().get(Calendar.YEAR)-1)+"";
                }
            }
            mp.put("year",ano);
            new AsyncResponse(this,true).execute(mp);

        }catch(Exception e){
            showError("Tu estado de cuenta aun no esta disponible",1);
        }
    }

    public void closeView(View v){
        this.finish();
    }

    @Override
    public void notifyChanges(Object response) {
        if(response==null) {
            showError("Tu estado de cuenta aun no esta disponible", 1);
            return;
        }
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();

            showError("Se envió tu estado de cuenta a" +info.getUserName(),4);

            return;

    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
}
