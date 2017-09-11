package telecom.televisa.com.izzi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.zxing.BarcodeFormat;
import com.kissmetrics.sdk.KISSmetricsAPI;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.StreamHandler;

import televisa.telecom.com.model.Card;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AndroidMultiPartEntity;
import televisa.telecom.com.util.AsyncLoginUpdate;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.CodeBarGenerator;
import televisa.telecom.com.util.FileCache;
import televisa.telecom.com.util.ImageLoader;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.MobileStatusResponse;
import televisa.telecom.com.util.PulltoRefreshAsyncResponse;
import televisa.telecom.com.util.izziEdoCuentaResponse;
import televisa.telecom.com.util.izziLoginResponse;
import televisa.telecom.com.ws.IzziWS;


public class UserActivity extends MenuActivity implements IzziRespondable, SwipeRefreshLayout.OnRefreshListener{
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);

    Usuario usrin=null;
    Usuario info;
    NumberFormat baseFormat = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "726810758992";
    Bitmap img=null;
    SwipeRefreshLayout swipeLayout;
    public static izziEdoCuentaResponse estado;
    boolean service=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_user);
        super.onCreate(savedInstanceState);
        AsyncLoginUpdate.refresca=this;
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        if(info.isEsNegocios()) {
            ((ImageView) findViewById(R.id.splash_logo)).setImageResource(R.drawable.negocios);
        }
        FileCache fc=new FileCache(this);
        fc.clear();

        init();
        new LongOperation().execute();
        KISSmetricsAPI.sharedAPI().record("Primer Login Android", KISSmetricsAPI.RecordCondition.RECORD_ONCE_PER_INSTALL);
        KISSmetricsAPI.sharedAPI().record("Uso en Android");
    }

public void swUsr(View v){
    Intent i=new Intent(this,SwitchUserActivity.class);
    startActivity(i);
}

    private class LongOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String msg="";
            try {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                regid = gcm.register(PROJECT_NUMBER);
                Map<String, String> mapa=new HashMap<>();
                mapa.put("device",regid);
                mapa.put("account",info.getCvNumberAccount());
                mapa.put("type",AES.encrypt("2"));
                IzziWS.callWebService(mapa,"push/register");
                msg=("Device registered, registration ID=" + regid);
                return msg;
            }catch (Exception e){
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    public void refresh(){
            init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    void init(){
        final UserActivity act=this;

         info=((IzziMovilApplication)getApplication()).getCurrentUser();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greating="";
        if(hour>=6&&hour<12)
            greating="BUENOS DIAS";
        else if(hour >=12&&hour<20)
            greating="BUENAS TARDES";
        else if(hour>=20 &&hour<24)
            greating="BUENAS NOCHES";
        else
            greating="¿AÚN DESPIERTO?";

        ((TextView)findViewById(R.id.greating)).setText(greating);
        if(info.isEsNegocios())
            ((ImageView) findViewById(R.id.splash_logo)).setImageResource(R.drawable.negocios);
        try {
            ((RelativeLayout)findViewById(R.id.wifiNOOK)).setVisibility(RelativeLayout.GONE);
            ((ImageView)findViewById(R.id.wifilogo)).setImageResource(R.drawable.miwifi);
            ((RelativeLayout)findViewById(R.id.wifiOFFLINE)).setVisibility(RelativeLayout.GONE);
            ((RelativeLayout)findViewById(R.id.noDexter)).setVisibility(RelativeLayout.GONE);
            ((RelativeLayout)findViewById(R.id.wifiinfo)).setVisibility(RelativeLayout.VISIBLE);
            ((TextView) findViewById(R.id.ssid_peers)).setVisibility(TextView.VISIBLE);
            ((TextView)findViewById(R.id.conectados)).setText("Dispositivos conectados:");
            if(info.isDisplayWifiInfo()) {
                if(!info.isRouterOffline()&&info.isWifiStatus()) {
                    ((TextView) findViewById(R.id.ssid)).setText(AES.decrypt(info.getWifiName()));
                    ((TextView) findViewById(R.id.ssid_pass)).setText(AES.decrypt(info.getWifiPass()));
                    ((TextView) findViewById(R.id.ssid_peers)).setText(AES.decrypt(info.getWifiPeers()));
                    //modificiar
                }if(!info.isWifiStatus()){
                    ((TextView)findViewById(R.id.conectados)).setText("Tu red WiFi está apagada");
                    ((TextView) findViewById(R.id.ssid)).setText(AES.decrypt(info.getWifiName()));
                    ((TextView) findViewById(R.id.ssid_pass)).setText(AES.decrypt(info.getWifiPass()));
                    ((ImageView)findViewById(R.id.wifilogo)).setImageResource(R.drawable.wifioff);
                    ((TextView) findViewById(R.id.ssid_peers)).setVisibility(TextView.INVISIBLE);
                }
                if(info.isRouterOffline()){
                    ((RelativeLayout)findViewById(R.id.wifiOFFLINE)).setVisibility(RelativeLayout.VISIBLE);
                    ((ImageView)findViewById(R.id.wifilogo)).setImageResource(R.drawable.offlinewifi);
                }
                if(info.isDexterDead()){
                    ((RelativeLayout)findViewById(R.id.noDexter)).setVisibility(RelativeLayout.VISIBLE);
                    ((ImageView)findViewById(R.id.wifilogo)).setImageResource(R.drawable.nodexter);
                }

            }else{
                ((RelativeLayout)findViewById(R.id.wifiinfo)).setVisibility(RelativeLayout.GONE);
            }
            String count=AES.decrypt(info.getCountNotifications());
            int noti=Integer.parseInt(count);
            if(noti>0){
                ((TextView)findViewById(R.id.notificationCount)).setText(count);
            }else{
                ((TextView)findViewById(R.id.notificationCount)).setVisibility(TextView.GONE);
            }
            String barcode_data = AES.decrypt(info.getBarcode());
            Bitmap bitmap = null;
         //   ImageView iv = (ImageView) findViewById(R.id.codebar);
           // bitmap = CodeBarGenerator.encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            //iv.setImageBitmap(bitmap);
            //((TextView)findViewById(R.id.codetext)).setText(barcode_data);
            ((TextView) findViewById(R.id.nameText)).setText(info.getNombreContacto() != null ? AES.decrypt(info.getNombreContacto()).split(" ")[0] +" "+AES.decrypt(info.getApellidoPaterno()): "");
            ((TextView) findViewById(R.id.phoneText)).setText(info.getTelefonoPrincipal() != null ? AES.decrypt(info.getTelefonoPrincipal()):"");
            ((TextView) findViewById(R.id.accountText)).setText(info.getCvNumberAccount() != null ? AES.decrypt(info.getCvNumberAccount()): "");
            ((TextView)findViewById(R.id.referenceText)).setText(barcode_data);
            String paquete=info.getPaquete() != null ? AES.decrypt(info.getPaquete()): "No disponible";
            ((TextView) findViewById(R.id.paqueteText)).setText(paquete);
            ((TextView) findViewById(R.id.totalText)).setText(info.getCvLastBalance() != null ? "$"+AES.decrypt(info.getCvLastBalance()): "0");
            String lastBalance=info.getCvLastBalance() != null ? AES.decrypt(info.getCvLastBalance()): "0";
            Double tot= Double.parseDouble(lastBalance);
            ((TextView)findViewById(R.id.totalText)).setTextColor(0xff666666);
            ((TextView) findViewById(R.id.leyenda1Text)).setText("Pagar antes de:");
            ((TextView) findViewById(R.id.fechaText)).setVisibility(TextView.VISIBLE);
            if(tot<=0){
                ((TextView) findViewById(R.id.leyenda1Text)).setText("Estás al corriente gracias por tu pago");
                ((TextView) findViewById(R.id.fechaText)).setVisibility(TextView.GONE);
            }
            double saldo=Double.parseDouble(lastBalance);
            lastBalance="$"+lastBalance;
            ((TextView) findViewById(R.id.totalText)).setText(lastBalance);
            String fecha=info.getFechaLimite() != null ? AES.decrypt(info.getFechaLimite()): null;
            String fechaFactura=info.getFechaFactura() != null ? AES.decrypt(info.getFechaFactura()): null;
            try {
                if (fecha != null) {
                    if (!fecha.isEmpty() && !fecha.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fecha);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() < cal.getTime().getTime()&&saldo>0) {
                            ((TextView)findViewById(R.id.totalText)).setTextColor(0xff666666);
                            ((TextView) findViewById(R.id.fechaText)).setText("Inmediato");
                            if(info.isIzziStatus()){
                                ((TextView) findViewById(R.id.leyenda1Text)).setText("Tu servicio se encuentra suspendido");
                                ((TextView) findViewById(R.id.fechaText)).setVisibility(TextView.GONE);
                            }
                        }
                    }else if(fechaFactura!=null){
                        if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                            Date fechaLimiteDate = sdf.parse(fechaFactura);
                            DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                            ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                            Calendar cal = Calendar.getInstance();
                            if (fechaLimiteDate.getTime() > cal.getTime().getTime()&&saldo>0) {
                                //TODO hacer el truco que quieren si tiene pago vencido
                                ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                             /*   TextView myText = (TextView) findViewById(R.id.totalText );

                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(50); //You can manage the time of the blink with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                myText.startAnimation(anim);*/
                            }
                        }
                    }
                }else if(fechaFactura!=null){
                    if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fechaFactura);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() > cal.getTime().getTime()&&saldo>0) {
                            ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                           /* TextView myText = (TextView) findViewById(R.id.totalText );

                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(50); //You can manage the time of the blink with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            myText.startAnimation(anim);*/
                        }
                    }else{
                        ((TextView) findViewById(R.id.fechaText)).setText("No disponible");
                    }
                }else{
                    ((TextView) findViewById(R.id.fechaText)).setText("No disponible");
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            ImageLoader loader;
            loader=new ImageLoader(this);
            if(info.getFotoPerfil()!=null) {
                String image=AES.decrypt(info.getFotoPerfil());
                if(!image.isEmpty())
                    loader.DisplayImage(image, (ImageView) findViewById(R.id.imageView1));
            }
            if(MainActivity.facebookImg!=null)
                    loader.DisplayImage(MainActivity.facebookImg, (ImageView) findViewById(R.id.imageView1));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void servicios(View v){


        Map<String,String> mp=new HashMap<>();
        try {
            mp.put("METHOD", "mobileUpdateService/checkStatus");
            mp.put("account", AES.decrypt(info.getCvNumberAccount()));
            service = true;
            new AsyncResponse(this, true).execute(mp);
        }catch(Exception e){

        }


    }


    public void foto(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 57);
    }

    public void wifiManager(View v){
        //call new activity for wifi management
        Intent myIntent = new Intent(this, WifiManagementActivity.class);
        startActivityForResult(myIntent, 0);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 57&& resultCode == RESULT_OK) {
            ImageView mImageView=(ImageView) findViewById(R.id.imageView1);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            img=imageBitmap;
            new UploadFileToServer().execute();

        }

       }

    public void callPay(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
         String telefono="";
        if(info.isLegacy()){
            telefono="018001205000";
        }else{
            telefono="018001205000";
            System.out.println("Es izzi");
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono));
        startActivity(callIntent);
    }

    @Override
    public void notifyChanges(Object response) {
        //vemos el pedo del estado de cuenta
        swipeLayout.setRefreshing(false);
        if(response==null){
            return;
        }
        if(!service) {
            izziLoginResponse rs = (izziLoginResponse) response;

            if (!rs.getIzziErrorCode().isEmpty()) {
                return;
            }

            info.setDisplayWifiInfo(rs.getResponse().isDisplayWifiInfo());
            info.setWifiName(rs.getResponse().getWifiName());
            info.setWifiPass(rs.getResponse().getWifiPass());
            info.setWifiPeers(rs.getResponse().getWifiPeers());
            info.setWifiStatus(rs.getResponse().isWifiStatus());
            info.setRouterOffline(rs.getResponse().isRouterOffline());
            info.setDexterDead(rs.getResponse().isDexterDead());
            info.save();
            ((IzziMovilApplication)getApplication()).setCurrentUser(info);
            init();
            swipeLayout.setRefreshing(false);
            return;
        }else{
            service=false;
            if(response==null){
                Intent myIntent = new Intent(this, ServiciosActivity.class);
                startActivityForResult(myIntent, 0);
                return;
            }
            MobileStatusResponse rs=(MobileStatusResponse)response;
            if (!rs.getIzziErrorCode().isEmpty()) {
                showError("Tu solicitud esta siendo procesada, muchas gracias.",6666);
                return;
            }
            Intent myIntent = new Intent(this, ServiciosActivity.class);
            startActivityForResult(myIntent, 0);
        }

    }

    @Override
    public void slowInternet() {
        //showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }
    public void edocuenta(View v){
        Intent i=new Intent(getApplicationContext(),EdoCuentaActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    public void ayudaa(View v){
        Intent i=new Intent(getApplicationContext(),AyudaActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    public void notifications(View v){
        Intent i=new Intent(getApplicationContext(),PushNotificationCenterActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }
    public void establecimientos(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Intent myIntent;
        myIntent = new Intent(this, MediosDePagoActivity.class);
        startActivityForResult(myIntent, 0);
        overridePendingTransition( R.transition.slide_in_up, R.transition.slide_out_up );

    }
    public void ajustes(View v){
        Intent myIntent = new Intent(this, EditAccountActivity.class);
        startActivityForResult(myIntent, 0);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    public void pagos(View v){
        Intent myIntent = new Intent(this, ListaPagosActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void pay(View v){
            Intent myIntent = new Intent(this, PagosMainActivity.class);
            startActivityForResult(myIntent, 0);
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         }
        
        @Override
        protected void onProgressUpdate(Integer... progress) {
            
        }
        
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
         }

        private String uploadFile() {
            String responseString = null;
                    try {
                  String metodo="profile/upload";

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    options.inPurgeable = true;
                    Bitmap bm = Bitmap.createBitmap(img);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    bm.compress(Bitmap.CompressFormat.PNG, 80, baos);

                    byte[] byteImage_photo = baos.toByteArray();

                    //generate base64 string of image

                    String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
                        System.out.println(encodedImage);
                        Map<String,String> ma=new HashMap<>();
                        ma.put("img",encodedImage);
                        ma.put("account",usrin.getCvNumberAccount());
                        ma.put("user",AES.encrypt(usrin.getUserName()));
                        Object response =  IzziWS.callWebService(ma,metodo);
                        String str="Hola";
                        str=str+str;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return responseString;
            
            }
        
                @Override
        protected void onPostExecute(String result) {
            
            super.onPostExecute(result);
            }
        
    }
    public void refresh(View v){
        onRefresh();
    }
    @Override
    public void onRefresh() {
        try {
            Map<String, String> ma = new HashMap<>();
            ma.put("METHOD", "refresh/account");
            ma.put("password", AES.encrypt(info.getPassword()));
            ma.put("account", info.getCvNumberAccount());
            ma.put("user", AES.encrypt(info.getUserName()));
            new PulltoRefreshAsyncResponse(this,false).execute(ma);
        }catch(Exception e){

        }
    }
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if(resultCode==666)
            finish();
    }

}
