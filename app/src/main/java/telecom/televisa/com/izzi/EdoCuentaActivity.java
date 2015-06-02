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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;


public class EdoCuentaActivity extends Activity {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf2=new SimpleDateFormat("MMMM", new Locale("es","MX"));
    SimpleDateFormat sdf3=new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
    SimpleDateFormat sdff = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edo_cuenta);
        ((TextView)findViewById(R.id.h_title)).setText("Estado de cuenta");
        //obtenemos el usuario desde izziMobileApp
        Usuario usuario=((IzziMovilApplication)getApplication()).getCurrentUser();
        //si la fecha de la factura del estado de cuenta nos llega nula es que no existe y debemos de ocultar los campos que no tenemos
        if(usuario.getEdoDate()!=null){
            try {
                String fechaFactura = AES.decrypt(usuario.getEdoDate());
                fechaFactura=sdf2.format(sdf.parse(fechaFactura));
                ((TextView)findViewById(R.id.mesFactura)).setText(fechaFactura);
                String dueDate=AES.decrypt(usuario.getEdoDueDate());
                 dueDate=sdf3.format(sdf.parse(dueDate));
                ((TextView)findViewById(R.id.dueFactura)).setText(dueDate);
                if(usuario.getSaldoMesAnterior()!=null){
                    String mesAnterior=AES.decrypt(usuario.getSaldoMesAnterior());
                    ((TextView)findViewById(R.id.mesanteriordata)).setText("$"+mesAnterior);

                }else{
                    ((TextView)findViewById(R.id.mesanteriordata)).setText("$0.00");
                }
                String saldo=AES.decrypt(usuario.getSaldoTotalCta());
                ((TextView)findViewById(R.id.totalmes)).setText(saldo);
                ((TextView)findViewById(R.id.paqname)).setText(AES.decrypt(usuario.getPaqName()));
                ((TextView)findViewById(R.id.paqTotal)).setText("$"+AES.decrypt(usuario.getPaqTotal()));
                boolean telefono=false;
                boolean internet=false;
                boolean video=false;
                boolean otros =false;
                boolean  veo=false;
                if(usuario.getTelTotal()!=null){
                    ((TextView)findViewById(R.id.teltotal)).setText("$"+AES.decrypt(usuario.getTelTotal()));
                    String total=AES.decrypt(usuario.getTelTotal());
                    if(Double.parseDouble(total)==0.00){
                        ((LinearLayout)findViewById(R.id.telText)).setVisibility(LinearLayout.GONE);
                        ((TextView)findViewById(R.id.teltotal)).setVisibility(TextView.GONE);
                    }else{
                        telefono=true;
                    }
                }else{
                    ((LinearLayout)findViewById(R.id.telText)).setVisibility(LinearLayout.GONE);
                    ((TextView)findViewById(R.id.teltotal)).setVisibility(TextView.GONE);
                }
                if(usuario.getIntTotal()!=null){
                    ((TextView)findViewById(R.id.inttotal)).setText("$"+AES.decrypt(usuario.getIntTotal()));
                    String total=AES.decrypt(usuario.getIntTotal());
                    if(Double.parseDouble(total)==0.00){
                        ((LinearLayout)findViewById(R.id.inttext)).setVisibility(LinearLayout.GONE);
                        ((TextView)findViewById(R.id.inttotal)).setVisibility(TextView.GONE);
                    }else{
                        internet=true;
                    }
                }else{
                    ((LinearLayout)findViewById(R.id.inttext)).setVisibility(LinearLayout.GONE);
                    ((TextView)findViewById(R.id.inttotal)).setVisibility(TextView.GONE);
                }
                if(usuario.getVidTotal()!=null){
                    ((TextView)findViewById(R.id.vidtotal)).setText("$"+AES.decrypt(usuario.getVidTotal()));
                    String total=AES.decrypt(usuario.getVidTotal());
                    if(Double.parseDouble(total)==0.00){
                        ((LinearLayout)findViewById(R.id.vidtext)).setVisibility(LinearLayout.GONE);
                        ((TextView)findViewById(R.id.vidtotal)).setVisibility(TextView.GONE);
                    }else{
                        video=true;
                    }
                }else{
                    ((LinearLayout)findViewById(R.id.vidtext)).setVisibility(LinearLayout.GONE);
                    ((TextView)findViewById(R.id.vidtotal)).setVisibility(TextView.GONE);
                }
                if(usuario.getOtrosTotal()!=null){
                    ((TextView)findViewById(R.id.otrosTotal)).setText("$"+AES.decrypt(usuario.getOtrosTotal()));
                    String total=AES.decrypt(usuario.getOtrosTotal());
                    if(Double.parseDouble(total)==0.00){
                        ((LinearLayout)findViewById(R.id.otrostext)).setVisibility(LinearLayout.GONE);
                        ((TextView)findViewById(R.id.otrosTotal)).setVisibility(TextView.GONE);
                    }else{
                        otros=true;
                    }
                }else{
                    ((LinearLayout)findViewById(R.id.otrostext)).setVisibility(LinearLayout.GONE);
                    ((TextView)findViewById(R.id.otrosTotal)).setVisibility(TextView.GONE);
                }
                if(usuario.getVeoTotal()!=null){
                    ((TextView)findViewById(R.id.veototal)).setText("$"+AES.decrypt(usuario.getVeoTotal()));
                    String total=AES.decrypt(usuario.getVeoTotal());
                    if(Double.parseDouble(total)==0.00){
                        ((LinearLayout)findViewById(R.id.veotext)).setVisibility(LinearLayout.GONE);
                        ((TextView)findViewById(R.id.veototal)).setVisibility(TextView.GONE);
                    }else{
                        otros=true;
                    }
                }else{
                    ((LinearLayout)findViewById(R.id.veotext)).setVisibility(LinearLayout.GONE);
                    ((TextView)findViewById(R.id.veototal)).setVisibility(TextView.GONE);
                }

                if(internet||veo||video||otros||telefono){

                }else{
                    ((LinearLayout)findViewById(R.id.cargosContenedor)).setVisibility(LinearLayout.GONE);
                }

                //bonificaciones
                if(usuario.getBonTotal()!=null){
                    ((TextView)findViewById(R.id.promoTotal)).setText("$"+AES.decrypt(usuario.getBonTotal()));
                    String total=AES.decrypt(usuario.getBonTotal());
                    if(Double.parseDouble(total)==0.00){
                        ((LinearLayout)findViewById(R.id.contenedorPromo)).setVisibility(TextView.GONE);
                    }else{
                        otros=true;
                    }
                }else{
                    ((LinearLayout)findViewById(R.id.contenedorPromo)).setVisibility(LinearLayout.GONE);
                }


            }catch(Exception e){ }

        }else{
            // tomar la fecha de billdate y la de due date del otro servicio  detail
            ((LinearLayout)findViewById(R.id.detail)).setVisibility(LinearLayout.GONE);
            ((TextView)findViewById(R.id.detalleText)).setVisibility(TextView.VISIBLE);

            ((LinearLayout)findViewById(R.id.contenedorPromo)).setVisibility(LinearLayout.GONE);
            ((LinearLayout)findViewById(R.id.cargosContenedor)).setVisibility(LinearLayout.GONE);
            ((TextView)findViewById(R.id.mesanterior)).setVisibility(TextView.GONE);
            ((TextView)findViewById(R.id.mesanteriordata)).setVisibility(TextView.GONE);
            try {
                String lastBalance = usuario.getCvLastBalance() != null ? AES.decrypt(usuario.getCvLastBalance()) : "0.00";
                String fecha = usuario.getFechaLimite() != null ? AES.decrypt(usuario.getFechaLimite()) : null;
                String fechaFactura = usuario.getFechaFactura() != null ? AES.decrypt(usuario.getFechaFactura()) : null;
                ((TextView)findViewById(R.id.totalmes)).setText("$"+lastBalance);
                ((TextView)findViewById(R.id.paqname)).setText(AES.decrypt(usuario.getPaqName()));

                if (fecha != null) {
                    if (!fecha.isEmpty() && !fecha.equals("0")) {
                        Date fechaLimiteDate = sdff.parse(fecha);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.dueFactura)).setText(mediumFormat.format(fechaLimiteDate));
                    }else if(fechaFactura!=null){
                        if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                            Date fechaLimiteDate = sdff.parse(fechaFactura);
                            DateFormat mediumFormat = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es","MX"));
                            ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                            ((TextView) findViewById(R.id.dueFactura)).setText(mediumFormat.format(fechaLimiteDate));
                        }
                    }
                }
                if(fechaFactura!=null){
                    if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                        Date fechaLimiteDate = sdff.parse(fechaFactura);
                        DateFormat mediumFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                        ((TextView) findViewById(R.id.mesFactura)).setText(mediumFormat.format(fechaLimiteDate));

                    }else{
                        ((TextView) findViewById(R.id.mesFactura)).setText("No disponible");
                    }
                }else{
                    ((TextView) findViewById(R.id.mesFactura)).setText("No disponible");
                }

            }catch(Exception e){

            }

        }


    }
public void aclaraciones(View v){
    Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
    String telefono="";
    if(info.isLegacy()){
        telefono="51699699";
    }else{
        telefono="018001205000";
        System.out.println("Es izzi");
    }

    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" + telefono));
    startActivity(callIntent);
}
    public void closeView(View v){
        this.finish();
    }
}
