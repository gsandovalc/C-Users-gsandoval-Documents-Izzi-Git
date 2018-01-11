package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziEdoCuentaResponse;
import televisa.telecom.com.ws.IzziWS;


public class EdoCuentaActivity extends IzziActivity implements IzziRespondable {
    boolean sendmail=false;
    izziEdoCuentaResponse estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edo_cuenta);
        izziEdoCuentaResponse estado;  // VHP segun yo jajajaja
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        ((TextView) findViewById(R.id.periodo)).setVisibility(TextView.GONE);
        ((TextView)findViewById(R.id.h_title)).setText("Estado de cuenta");
        try {

            Usuario info = ((IzziMovilApplication) getApplication()).getCurrentUser();
            ((TextView)findViewById(R.id.totalText)).setText("$ "+AES.decrypt(info.getCvLastBalance()));
            int bal=Integer.parseInt(AES.decrypt(info.getCvLastBalance()));
            if(bal>0){
                ((TextView) findViewById(R.id.aclara)).setText("Pagar");
            }
            Map<String, String> mp = new HashMap<>();
            mp.put("METHOD", "estado");
            mp.put("user", AES.encrypt(info.getUserName()));
            mp.put("cuenta", info.getCvNumberAccount());
            mp.put("token", info.getToken());
            new AsyncResponse(this, false).execute(mp);
        }catch(Exception e){
            e.printStackTrace();
        }






    }
    public void showMenu(View v){
        finish();
    }
    public void goToHistoric(View v){
        Intent  i=new Intent(getApplicationContext(),ListaPagosActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        try {
            Usuario info = ((IzziMovilApplication) getApplication()).getCurrentUser();
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Historial de pagos").putContentType("edo tabbar").putCustomAttribute("account",AES.decrypt(info.getCvNumberAccount())).putCustomAttribute("user",info.getUserName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void sendBill(View v){
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        Map <String,String> mp=new HashMap<>();
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
            mp.put("month", meses.get(AES.decrypt(estado.getResponse().getMes())));
            String ano=Calendar.getInstance().get(Calendar.YEAR)+"";
            if(Integer.parseInt(meses.get(AES.decrypt(estado.getResponse().getMes())))==12){
                if(Calendar.getInstance().get(Calendar.MONTH)==0){
                    ano=(Calendar.getInstance().get(Calendar.YEAR)-1)+"";
                }
            }
            mp.put("year",ano);
            new AsyncResponse(this,true).execute(mp);
            sendmail=true;
            try {
                Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Enviar por correo edo").putContentType("edo tabbar").putCustomAttribute("account",AES.decrypt(usuario.getCvNumberAccount())).putCustomAttribute("user",usuario.getUserName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            showError("Tu estado de cuenta aun no esta disponible",1);
        }
    }
public void aclaraciones(View v){
    Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
    String telefono="";
    try {
        int bal=Integer.parseInt(AES.decrypt(info.getCvLastBalance()));
        if(bal<=0) {
            if (info.isLegacy()) {
                telefono = "018001205000";
            } else {
                telefono = "018001205000";
                System.out.println("Es izzi");
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + telefono));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }else{
            Intent i =new Intent(this,PagosMainActivity.class);
            finish();
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }catch(Exception e){
        e.printStackTrace();
    }

}



    public void closeView(View v){
        this.finish();
    }

    @Override
    public void notifyChanges(Object response) {
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        if(sendmail){
            showError("Se envió tu estado de cuenta a" +info.getUserName(),4);
            sendmail=false;
            return;
        }
        if(response==null){

            return;
        }
        izziEdoCuentaResponse rs=(izziEdoCuentaResponse)response;
estado=rs;
        if(!rs.getIzziErrorCode().isEmpty()){
            ((RelativeLayout)findViewById(R.id.vista)).setVisibility(RelativeLayout.VISIBLE);
            return;
        }

        ((RelativeLayout)findViewById(R.id.vista)).setVisibility(RelativeLayout.GONE);
        //si la fecha de la factura del estado de cuenta nos llega nula es que no existe y debemos de ocultar los campos que no tenemos
        //UserActivity.estado;
        try {
            int acumulador=0;
            ((TextView) findViewById(R.id.periodo)).setVisibility(TextView.VISIBLE);
            ((TextView) findViewById(R.id.periodo)).setText("Período del "+AES.decrypt(rs.getResponse().getPeriodo()));
            ((TextView) findViewById(R.id.limite)).setText(AES.decrypt(rs.getResponse().getFechaLimite()));
            ((TextView) findViewById(R.id.anterior)).setText(AES.decrypt(rs.getResponse().getSaldoAnterior()));

            ((TextView) findViewById(R.id.paqName)).setText(AES.decrypt(rs.getResponse().getPaquete()));
            ((TextView) findViewById(R.id.paqP)).setText(AES.decrypt(rs.getResponse().getTotalPaquete()));
            if(rs.getResponse().isExtraTV()){
                acumulador+=Integer.parseInt(AES.decrypt(rs.getResponse().getTotalTV()).substring(1));
            }
            if(rs.getResponse().isExtraTel()){
                acumulador+=Integer.parseInt(AES.decrypt(rs.getResponse().getTotalTel()).substring(1));
            }
            if(rs.getResponse().isExtraInt()){
                acumulador+=Integer.parseInt(AES.decrypt(rs.getResponse().getTotalInt()).substring(1));
            }
            ((LinearLayout)findViewById(R.id.veo)).setVisibility(LinearLayout.VISIBLE);
            if(rs.getResponse().isExtraVeo()){
                acumulador+=Integer.parseInt(AES.decrypt(rs.getResponse().getTotalVeo()).substring(1));

            }
            ((TextView) findViewById(R.id.veoTot)).setText("$"+acumulador);
            ((LinearLayout)findViewById(R.id.otros)).setVisibility(LinearLayout.VISIBLE);
            if(rs.getResponse().isExtraOtros()){
                ((TextView) findViewById(R.id.otrosTot)).setText(AES.decrypt(rs.getResponse().getTotalOtros()));
            }else{
                ((TextView) findViewById(R.id.otrosTot)).setText("$ 0");
            }

            ((TextView) findViewById(R.id.st)).setText(AES.decrypt(rs.getResponse().getSubTotal()));
            ((TextView) findViewById(R.id.tp)).setText(AES.decrypt(rs.getResponse().getPagos()));
            ((TextView) findViewById(R.id.infop)).setText(AES.decrypt(rs.getResponse().getPagoTexto()));
            System.out.println(AES.decrypt(rs.getResponse().getTotal()));

            ((TextView) findViewById(R.id.total)).setText("$"+AES.decrypt(info.getCvLastBalance()));

        }catch(Exception e){
            ((RelativeLayout)findViewById(R.id.vista)).setVisibility(RelativeLayout.VISIBLE);
        }




    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
}
