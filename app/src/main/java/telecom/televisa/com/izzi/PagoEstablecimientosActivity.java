package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.controls.PopoverView;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziDondePagarResponse;


public class PagoEstablecimientosActivity extends Activity implements IzziRespondable, View.OnClickListener, PopoverView.PopoverViewDelegate {
    int[]bancos=new int[]{R.id.bankName1,R.id.bankName2,R.id.bankName3,R.id.bankName4,R.id.bankName5,R.id.bankName6,R.id.bankName7,R.id.bankName8,R.id.bankName9,R.id.bankName10};
    int[]bank=new int[]{R.id.bank1,R.id.bank2,R.id.bank3,R.id.bank4,R.id.bank5,R.id.bank6,R.id.bank7,R.id.bank8,R.id.bank9,R.id.bank10};
    int[]referencias=new int[]{R.id.bankRef1,R.id.bankRef2,R.id.bankRef3,R.id.bankRef4,R.id.bankRef5,R.id.bankRef6,R.id.bankRef7,R.id.bankRef8,R.id.bankRef9,R.id.bankRef10};
    int[]botonB=new int[]{R.id.geolocBank1,R.id.geolocBank2,R.id.geolocBank3,R.id.geolocBank4,R.id.geolocBank5,R.id.geolocBank6,R.id.geolocBank7,R.id.geolocBank8,R.id.geolocBank9,R.id.geolocBank10};
    int[] tiendas=new int[]{R.id.tienda1,R.id.tienda2,R.id.tienda3,R.id.tienda4,R.id.tienda5,R.id.tienda6,R.id.tienda7,R.id.tienda8,R.id.tienda9,R.id.tienda10,R.id.tienda11,R.id.tienda12,R.id.tienda13,R.id.tienda14,R.id.tienda15,R.id.tienda16,R.id.tienda17,R.id.tienda18,R.id.tienda19,R.id.tienda20};
    int[] tiendasN=new int[]{R.id.tiendaName1,R.id.tiendaName2,R.id.tiendaName3,R.id.tiendaName4,R.id.tiendaName5,R.id.tiendaName6,R.id.tiendaName7,R.id.tiendaName8,R.id.tiendaName9,R.id.tiendaName10,R.id.tiendaName11,R.id.tiendaName12,R.id.tiendaName13,R.id.tiendaName14,R.id.tiendaName15,R.id.tiendaName16,R.id.tiendaName17,R.id.tiendaName18,R.id.tiendaName19,R.id.tiendaName20};
    int[]tiendaB=new int[]{R.id.geolocTienda1,R.id.geolocTienda2,R.id.geolocTienda3,R.id.geolocTienda4,R.id.geolocTienda5,R.id.geolocTienda6,R.id.geolocTienda7,R.id.geolocTienda8,R.id.geolocTienda9,R.id.geolocTienda10,R.id.geolocTienda11,R.id.geolocTienda12,R.id.geolocTienda13,R.id.geolocTienda14,R.id.geolocTienda15,R.id.geolocTienda16,R.id.geolocTienda17,R.id.geolocTienda18,R.id.geolocTienda19,R.id.geolocTienda20};
    RelativeLayout rootView;
    PopoverView popoverView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_establecimientos);

        rootView = (RelativeLayout)findViewById(R.id.vista).getParent();
        popoverView = new PopoverView(this, R.layout.popover_showed_view);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en establecimientos");
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        ((LinearLayout)findViewById(R.id.contene)).setVisibility(LinearLayout.GONE);
        if(info.isLegacy()){
findViewById(R.id.qst).setVisibility(View.GONE);

        }else{
            findViewById(R.id.show_exmenu).setVisibility(View.VISIBLE);
            try {
                ((TextView)popoverView.vista.findViewById(R.id.leyenda).findViewById(R.id.tel)).setText(AES.decrypt(info.getTelefonoPrincipal()));
                ((TextView)popoverView.vista.findViewById(R.id.cuenta)).setText(AES.decrypt(info.getCvNumberAccount()));
                ((TextView)popoverView.vista.findViewById(R.id.ref)).setText(AES.decrypt(info.getBarcode()));
                ((TextView) findViewById(R.id.bancoH)).setText("");
                ((TextView) findViewById(R.id.tiendaN)).setText("Establecimientos");
                ((TextView) findViewById(R.id.tiendaH)).setText("");
                ((TextView)findViewById(R.id.leyenda2)).setText("Tu pago se verá reflejado de acuerdo con el lugar de pago. \n\n -Tiendas izzi: inmediatamente\n\n" +
                        " -Bancos y establecimientos:1 hora\n\n" +
                        " -Tiendas grupo Walmart (Walmart, Bodega Aurrera, Superama, SAM's, Suburbia): 1 día hábil\n\n" +
                        " -Banca en línea en horarios inhábiles: 1 día hábil\n\n" +
                        " Algunos establecimientos pueden cobrar comisión.");
            }catch (Exception e){

            }
        }

        try {
            Map<String, String> mp = new HashMap<>();
            mp.put("METHOD", "dondepagar");
            mp.put("legacy", info.isLegacy() ? "1" : "0");
            mp.put("rpt", info.getRpt());
            new AsyncResponse(this,false).execute(mp);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public void closeView(View v){
        this.finish();
    }
    public void help(View v){
        rootView = (RelativeLayout)findViewById(R.id.vista).getParent();
        popoverView = new PopoverView(this, R.layout.popover_showed_view2);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            ((TextView)popoverView.vista.findViewById(R.id.leyenda2)).setText("Tu pago se verá reflejado de acuerdo con el lugar de pago. \n\n -Tiendas izzi: inmediatamente\n\n" +
                    " -Bancos y establecimientos:1 hora\n\n" +
                    " -Tiendas grupo Walmart (Walmart, Bodega Aurrera, Superama, SAM's, Suburbia): 1 día hábil\n\n" +
                    " -Banca en línea en horarios inhábiles: 1 día hábil\n" +
                    " Algunos establecimientos pueden cobrar comisión.");
        }catch(Exception e){}

        popoverView.setContentSizeForViewInPopover(new Point(650, 400));
        popoverView.setDelegate(this);

        popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(v), PopoverView.PopoverArrowDirectionAny, true);
    }

    public void findBank(View v){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+v.getTag());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    public void showModal(View v){
        rootView = (RelativeLayout)findViewById(R.id.vista).getParent();
        popoverView = new PopoverView(this, R.layout.popover_showed_view);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            try {
                ((TextView) popoverView.vista.findViewById(R.id.leyenda).findViewById(R.id.tel)).setText(AES.decrypt(info.getTelefonoPrincipal()));
                ((TextView) popoverView.vista.findViewById(R.id.cuenta)).setText(AES.decrypt(info.getCvNumberAccount()));
                ((TextView) popoverView.vista.findViewById(R.id.ref)).setText(AES.decrypt(info.getBarcode()));
            }catch(Exception e){}

        popoverView.setContentSizeForViewInPopover(new Point(550, 500));
        popoverView.setDelegate(this);

        popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(v), PopoverView.PopoverArrowDirectionAny, true);
    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("No se pudo obtener los lugares de pago")
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

         izziDondePagarResponse rs=   (izziDondePagarResponse)response;
        if(!rs.getIzziErrorCode().isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("No se pudo obtener los lugares de pago")
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
        int contador=0;
        for(izziDondePagarResponse.Bancos b:rs.getResponse().getBancos()){
            if(contador>=10){
                break;
            }
            ((TextView) findViewById(bancos[contador])).setText(b.getNombre());
            ((TextView) findViewById(referencias[contador])).setText(b.getReferencia());
            findViewById(botonB[contador]).setTag("Banco " + b.getNombre());
            contador++;

        }

        for(int i=contador;i<10;i++){
            ((RelativeLayout)findViewById(bank[i])).setVisibility(RelativeLayout.GONE);
        }
         contador=0;
        for(String b:rs.getResponse().getTiendas()){
            if(contador>=20){
                break;
            }
            ((TextView) findViewById(tiendasN[contador])).setText(b);
            findViewById(tiendaB[contador]).setTag( b);
            contador++;

        }

        for(int i=contador;i<20;i++){
            ((RelativeLayout)findViewById(tiendas[i])).setVisibility(RelativeLayout.GONE);
        }
        ((LinearLayout)findViewById(R.id.contene)).setVisibility(LinearLayout.VISIBLE);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void popoverViewWillShow(PopoverView view) {

    }

    @Override
    public void popoverViewDidShow(PopoverView view) {

    }

    @Override
    public void popoverViewWillDismiss(PopoverView view) {

    }

    @Override
    public void popoverViewDidDismiss(PopoverView view) {

    }
}
