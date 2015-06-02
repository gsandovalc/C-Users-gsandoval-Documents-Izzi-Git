package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;


public class PagoEstablecimientosActivity extends Activity {
    int[]bancos=new int[]{R.id.bankName1,R.id.bankName2,R.id.bankName3,R.id.bankName4,R.id.bankName5,R.id.bankName6};
    int[]botonB=new int[]{R.id.geolocBank1,R.id.geolocBank2,R.id.geolocBank3,R.id.geolocBank4,R.id.geolocBank5,R.id.geolocBank6};
    int[]referencias=new int[]{R.id.bankRef1,R.id.bankRef2,R.id.bankRef3,R.id.bankRef4,R.id.bankRef5,R.id.bankRef6};
    int[] tiendas=new int[]{R.id.tiendaName1,R.id.tiendaName2,R.id.tiendaName3,R.id.tiendaName4,R.id.tiendaName5,R.id.tiendaName6,R.id.tiendaName7,R.id.tiendaName8,R.id.tiendaName9};
    String[] tiendaName1=new String[]{"Elektra","7ELEVEN","Walmart","Bodega Aurrera","Bodega Aurrera Express","Superama","Sam´s Club","Sanborns","Sears"};
    String[] tiendaName2=new String[]{"Elektra","OXXO","Walmart","Bodega Aurrera","Bodega Aurrera Express","Superama","Sam´s Club","Telecom Telégrafos","Coppel"};
    int[]tiendaB=new int[]{R.id.geolocTienda1,R.id.geolocTienda2,R.id.geolocTienda3,R.id.geolocTienda4,R.id.geolocTienda5,R.id.geolocTienda6,R.id.geolocTienda7,R.id.geolocTienda8,R.id.geolocTienda9};
    String[] nombresB=new String[]{"Banamex","BBVA Bancomer","HSBC","Inbursa","Scotiabank","Santander"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_establecimientos);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en establecimientos");
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        String rrpt=null;
        if(info.getBankLine1()==null){
            for(int i=0;i<referencias.length;i++){
                ((TextView)findViewById(referencias[i])).setVisibility(TextView.GONE);
            }
        }else{
            try {
                ((TextView) findViewById(R.id.bankRef1)).setText(AES.decrypt(info.getBankLineN1()));
                ((TextView) findViewById(R.id.bankRef2)).setText(AES.decrypt(info.getBankLineN2()));
                ((TextView) findViewById(R.id.bankRef3)).setText(AES.decrypt(info.getBankLineN3()));
                ((TextView) findViewById(R.id.bankRef4)).setText(AES.decrypt(info.getBankLineN4()));
                ((TextView) findViewById(R.id.bankRef5)).setText(AES.decrypt(info.getBankLineN5()));
                ((TextView) findViewById(R.id.bankRef6)).setText(AES.decrypt(info.getBankLineN6()));
            }catch(Exception e){}
        }
        try{

            rrpt= AES.decrypt(info.getRpt());
        }catch(Exception e){
            e.printStackTrace();
        }
        for(int i=0;i<bancos.length;i++) {
            findViewById(botonB[i]).setTag("Banco " + nombresB[i]);
            ((TextView)findViewById(bancos[i])).setText(nombresB[i]);
        }
        int rpt=rrpt==null?0:Integer.parseInt(rrpt);
        System.out.println("el rpt es "+rpt);
        String[] changarrosGrandes;
        if(rpt==0){
            changarrosGrandes=tiendaName1;
        }else{
            changarrosGrandes=tiendaName2;
        }
        for(int i=0;i<changarrosGrandes.length;i++) {
            findViewById(tiendaB[i]).setTag(changarrosGrandes[i]);
            ((TextView)findViewById(tiendas[i])).setText(changarrosGrandes[i]);
        }
    }
    public void closeView(View v){
        this.finish();
    }

    public void findBank(View v){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+v.getTag());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
