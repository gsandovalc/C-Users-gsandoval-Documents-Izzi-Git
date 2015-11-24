package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.model.Cuentas;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.CuentasListAdapter;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.PagosListAdapter;
import televisa.telecom.com.util.izziLoginResponse;


public class SwitchUserActivity extends Activity implements IzziRespondable {
    ListView lv;
    List<Cuentas> cuentas;
    SwitchUserActivity act=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_user);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        ImageView im=(ImageView)findViewById(R.id.show_menu);
        im.setImageResource(R.drawable.regresar);
        if(info==null){
            finish();
            Intent i= new Intent(getApplicationContext(),BtfLanding.class);
            startActivity(i);
            return;
        }
        if(info.isEsNegocios()) {
            ((ImageView) findViewById(R.id.splash_logo)).setImageResource(R.drawable.negocios);
        }
        initList();
    }
    public void showMenu(View v){
        finish();
    }
    private void initList(){
        lv=(ListView)findViewById(R.id.lista_cuenta);
        IzziMovilApplication app=(IzziMovilApplication)getApplication();
      //  ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.GONE);
        cuentas=app.getCurrentUser().getCuentasAsociadas();
        lv.setVisibility(ListView.VISIBLE);
        if(cuentas==null){
            lv.setVisibility(ListView.GONE);
        //    ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.VISIBLE);
        }else if(cuentas.size()==0){
            lv.setVisibility(ListView.GONE);
          //  ((TextView)findViewById(R.id.sinpagos)).setVisibility(TextView.VISIBLE);
        }else {
            CuentasListAdapter adapter = new CuentasListAdapter(this, R.layout.cuenta_list_item, cuentas, 0);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IzziMovilApplication app=(IzziMovilApplication)getApplication();
                try {
                    Map<String, String> mp = new HashMap<>();
                    mp.put("METHOD", "login/getChildAccountInfo");
                    mp.put("user", app.getCurrentUser().getUserName());
                    mp.put("password", app.getCurrentUser().getPassword());
                    mp.put("childAccount", AES.decrypt(cuentas.get(position).getCuentaNumero()));
                    if(app.getCurrentUser().getParentAccount().isEmpty())
                          mp.put("type", app.getCurrentUser().isEsNegocios()?"1":"0");
                    else
                        mp.put("type", app.getCurrentUser().getParentType());

                    new AsyncResponse(act, true).execute(mp);
                }catch(Exception e){

                }
            }
        });
    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            response= (Object)(new izziLoginResponse());
            ((izziLoginResponse)response).setIzziError("Error inesperado");
            ((izziLoginResponse)response).setIzziErrorCode("999");
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ocurrio un error al cargar la información.\n Intentarlo de nuevo")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        IzziMovilApplication app=(IzziMovilApplication)getApplication();
        String user="";
        String password="";
        String cuenta="";
        String type="";
        user=app.getCurrentUser().getUserName();
        password=app.getCurrentUser().getPassword();
        try {
            cuenta = app.getCurrentUser().getParentAccount().equals("") ? AES.decrypt(app.getCurrentUser().getCvNumberAccount()) : app.getCurrentUser().getParentAccount();
            type=app.getCurrentUser().getParentType().equals("") ? (app.getCurrentUser().isEsNegocios()?"1":"0") : app.getCurrentUser().getParentType();
        }catch(Exception e){

        }
        new Delete().from(Usuario.class).execute();
        new Delete().from(PagosList.class).execute();
        new Delete().from(Cuentas.class).execute();
        if(((izziLoginResponse)response).getIzziErrorCode().isEmpty()){
            Usuario sr=((izziLoginResponse)response).getResponse();

            sr.setParentType(type);
            sr.setParentAccount(cuenta);
            sr.setUserName(user);
            sr.setPassword(password);

            sr.setToken(((izziLoginResponse)response).getToken());
            if(sr.getPagos()!=null)
                for(PagosList pago:sr.getPagos()){
                    pago.save();
                }

            if(sr.getCuentasAsociadas()!=null){
                for(Cuentas ac:sr.getCuentasAsociadas())
                    ac.save();
            }
            if(sr.isExtrasVideo()){
                String extra="";
                for(String complemento:sr.getDataExtrasVideo()){
                    extra=complemento+"##"+extra;
                }
                sr.setExtraVideo(extra);
            }
            if(sr.isExtrasTelefono()){
                String extra="";
                for(String complemento:sr.getDataExtrasTelefono()){
                    extra=complemento+"##"+extra;
                }
                sr.setExtraTelefono(extra);
            }
            if(sr.isExtrasInternet()){
                String extra="";
                for(String complemento:sr.getDataExtrasInternet()){
                    extra=complemento+"##"+extra;
                }
                sr.setExtraInternet(extra);
            }
            sr.save();
            this.finish();
            ((IzziMovilApplication)this.getApplication()).setCurrentUser(sr);
            ((IzziMovilApplication)this.getApplication()).setLogged(true);
        }else{
            // mostrar mensaje rosa de usuario o contraseña invalido si es el caso
            //si no popup

        }

    }
}
