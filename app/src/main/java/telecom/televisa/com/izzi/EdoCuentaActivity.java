package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;


public class EdoCuentaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edo_cuenta);
        ((TextView)findViewById(R.id.h_title)).setText("Estado de cuenta");
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        //si la fecha de la factura del estado de cuenta nos llega nula es que no existe y debemos de ocultar los campos que no tenemos
        //UserActivity.estado;
        try {
            ((TextView) findViewById(R.id.mes)).setText(AES.decrypt(UserActivity.estado.getResponse().getMes()));
            ((TextView) findViewById(R.id.periodo)).setText(AES.decrypt(UserActivity.estado.getResponse().getPeriodo()));
            ((TextView) findViewById(R.id.limite)).setText(AES.decrypt(UserActivity.estado.getResponse().getFechaLimite()));
            ((TextView) findViewById(R.id.anterior)).setText(AES.decrypt(UserActivity.estado.getResponse().getSaldoAnterior()));

            ((TextView) findViewById(R.id.paqName)).setText(AES.decrypt(UserActivity.estado.getResponse().getPaquete()));
            ((TextView) findViewById(R.id.paqP)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalPaquete()));
            if(UserActivity.estado.getResponse().isExtraTV()){
                ((LinearLayout)findViewById(R.id.tv)).setVisibility(LinearLayout.VISIBLE);
                ((TextView) findViewById(R.id.tvTot)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalTV()));
            }else{
                ((LinearLayout)findViewById(R.id.tv)).setVisibility(LinearLayout.GONE);
            }
            if(UserActivity.estado.getResponse().isExtraTel()){
                ((LinearLayout)findViewById(R.id.tel)).setVisibility(LinearLayout.VISIBLE);
                ((TextView) findViewById(R.id.telTot)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalTel()));
            }else{
                ((LinearLayout)findViewById(R.id.tel)).setVisibility(LinearLayout.GONE);
            }
            if(UserActivity.estado.getResponse().isExtraInt()){
                ((LinearLayout)findViewById(R.id.inter)).setVisibility(LinearLayout.VISIBLE);
                ((TextView) findViewById(R.id.intTot)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalInt()));
            }else{
                ((LinearLayout)findViewById(R.id.inter)).setVisibility(LinearLayout.GONE);
            }
            if(UserActivity.estado.getResponse().isExtraVeo()){
                ((LinearLayout)findViewById(R.id.veo)).setVisibility(LinearLayout.VISIBLE);
                ((TextView) findViewById(R.id.veoTot)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalInt()));
            }else{
                ((LinearLayout)findViewById(R.id.veo)).setVisibility(LinearLayout.GONE);
            }
            if(UserActivity.estado.getResponse().isExtraOtros()){
                ((LinearLayout)findViewById(R.id.otros)).setVisibility(LinearLayout.VISIBLE);
                ((TextView) findViewById(R.id.otrosTot)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalInt()));
            }else{
                ((LinearLayout)findViewById(R.id.otros)).setVisibility(LinearLayout.GONE);
            }
            if(UserActivity.estado.getResponse().isExtraBonus()){
                ((LinearLayout)findViewById(R.id.bon)).setVisibility(LinearLayout.VISIBLE);
                ((TextView) findViewById(R.id.bonTot)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotalInt()));
            }else{
                ((LinearLayout)findViewById(R.id.bon)).setVisibility(LinearLayout.GONE);
            }
            ((TextView) findViewById(R.id.st)).setText(AES.decrypt(UserActivity.estado.getResponse().getTotal()));
            ((TextView) findViewById(R.id.tp)).setText(AES.decrypt(UserActivity.estado.getResponse().getPagos()));
            ((TextView) findViewById(R.id.infop)).setText(AES.decrypt(UserActivity.estado.getResponse().getPagoTexto()));
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            ((TextView) findViewById(R.id.total)).setText("$"+AES.decrypt(info.getCvLastBalance()));
            int bal=Integer.parseInt(AES.decrypt(info.getCvLastBalance()));
            if(bal>0){
                ((TextView) findViewById(R.id.aclara)).setText("Pagar");
            }
        }catch(Exception e){

        }

    }
public void aclaraciones(View v){
    Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
    String telefono="";
    try {
        int bal=Integer.parseInt(AES.decrypt(info.getCvLastBalance()));
        if(bal<=0) {
            if (info.isLegacy()) {
                telefono = "51699699";
            } else {
                telefono = "018001205000";
                System.out.println("Es izzi");
            }
        }else{
            Intent i =new Intent(this,PagosMainActivity.class);
            finish();
            startActivity(i);
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        }
    }catch(Exception e){

    }

    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" + telefono));
    startActivity(callIntent);
}
    public void closeView(View v){
        this.finish();
    }
}
