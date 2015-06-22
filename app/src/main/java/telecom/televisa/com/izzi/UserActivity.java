package telecom.televisa.com.izzi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Config;
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
import com.google.android.gms.gcm.GoogleCloudMessaging;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import televisa.telecom.com.model.Card;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AndroidMultiPartEntity;
import televisa.telecom.com.util.FileCache;
import televisa.telecom.com.util.ImageLoader;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.ws.IzziWS;


public class UserActivity extends MenuActivity implements IzziRespondable{
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
    Bitmap img=null;
    Usuario usrin=null;
    NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "726810758992";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        layout=(R.layout.activity_user);
        super.onCreate(savedInstanceState);
        FileCache fc=new FileCache(this);
        fc.clear();
        init();
        new LongOperation().execute();
        KISSmetricsAPI.sharedAPI().record("Login Primera Vez en Apps", KISSmetricsAPI.RecordCondition.RECORD_ONCE_PER_INSTALL);
        KISSmetricsAPI.sharedAPI().record("Login en Apps");
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
                mapa.put("account",usrin.getCvNumberAccount());
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
    @Override
    protected void onResume() {
        super.onResume();
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        //llenamos obtenemnos los campos de texto
        usrin=info;
        try {
            String ahhh=AES.decrypt(info.getPaquete());
            System.out.println(ahhh);
            ((TextView) findViewById(R.id.nameText)).setText(info.getNombreContacto() != null ? AES.decrypt(info.getNombreContacto()).split(" ")[0] : "");
            ((TextView) findViewById(R.id.phoneText)).setText(info.getTelefonoPrincipal() != null ? AES.decrypt(info.getTelefonoPrincipal()).replaceAll("(.{2})(?!$)", "$1 ") : "");
            ((TextView) findViewById(R.id.accountText)).setText(info.getCvNumberAccount() != null ? AES.decrypt(info.getCvNumberAccount()): "");
            ((TextView) findViewById(R.id.paqueteText)).setText(info.getPaquete() != null ? AES.decrypt(info.getPaquete()): "No disponible");
            ((TextView) findViewById(R.id.totalText)).setText(info.getCvLastBalance() != null ? "$"+AES.decrypt(info.getCvLastBalance()): "0.00");

            String lastBalance=info.getCvLastBalance() != null ? AES.decrypt(info.getCvLastBalance()): "0.00";
            Double tot= Double.parseDouble(lastBalance);
            if(tot<=0){
                //debemos cambiar el mensaje del pago
                ((TextView) findViewById(R.id.leyenda1Text)).setText("Estás al corriente gracias por tu pago");
                ((TextView) findViewById(R.id.fechaText)).setVisibility(TextView.GONE);
            }
            double saldo=Double.parseDouble(lastBalance);
            lastBalance=baseFormat.format(saldo);
            ((TextView) findViewById(R.id.totalText)).setText(lastBalance);
            //saldo=baseFormat.format(lastBalance);
            String fecha=info.getFechaLimite() != null ? AES.decrypt(info.getFechaLimite()): null;
            String fechaFactura=info.getFechaFactura() != null ? AES.decrypt(info.getFechaFactura()): null;
            ((TextView) findViewById(R.id.leyenda1Text)).setTextColor(0xff000000);
            try {
                if (fecha != null) {
                    if (!fecha.isEmpty() && !fecha.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fecha);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() < cal.getTime().getTime()&&saldo>0) {
                            TextView myText = (TextView) findViewById(R.id.totalText );
                            ((TextView) findViewById(R.id.leyenda1Text)).setText("Saldo vencido pagar de inmediato ");
                            ((TextView) findViewById(R.id.leyenda1Text)).setTextColor(0xffff0000);
                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(50); //You can manage the time of the blink with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            myText.startAnimation(anim);
                        }
                    }else if(fechaFactura!=null){
                        if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                            Date fechaLimiteDate = sdf.parse(fechaFactura);
                            DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                            String datee=mediumFormat.format(fechaLimiteDate);
                            ((TextView) findViewById(R.id.fechaText)).setText(datee);
                            Calendar cal = Calendar.getInstance();
                            if (fechaLimiteDate.getTime() < cal.getTime().getTime()&&saldo>0) {
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
                        if (fechaLimiteDate.getTime() < cal.getTime().getTime()&&saldo>0) {
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


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void init(){
        final RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.cuadroParent);
        final UserActivity act=this;


        myLayout.post(new Runnable()
        {

            @Override
            public void run()
            {
                System.out.println("TEST Layout width : "+ myLayout.getWidth());
                ((LinearLayout)findViewById(R.id.cuadroInfo1)).getLayoutParams().width=(myLayout.getWidth()-televisa.telecom.com.util.Util.dpToPx(act,30))/2;
                ((LinearLayout)findViewById(R.id.cuadroInfo2)).getLayoutParams().width=(myLayout.getWidth()-televisa.telecom.com.util.Util.dpToPx(act,30))/2;
                ((RelativeLayout)findViewById(R.id.cuadroInfo3)).getLayoutParams().width=(myLayout.getWidth()-televisa.telecom.com.util.Util.dpToPx(act,30))/2-televisa.telecom.com.util.Util.dpToPx(act,3);
                ((RelativeLayout)findViewById(R.id.cuadroInfo4)).getLayoutParams().width=(myLayout.getWidth()-televisa.telecom.com.util.Util.dpToPx(act,30))/2-televisa.telecom.com.util.Util.dpToPx(act,3);
            }
        });

        final ScrollView sLayout = (ScrollView) findViewById(R.id.scrollV);
        sLayout.post(new Runnable()
        {

            @Override
            public void run()
            {
                int alto=((LinearLayout)act.findViewById(R.id.inerscroll)).getHeight();
                System.out.println(alto+"fue el altoo y el del scroll es" +sLayout.getHeight());
                if(alto<=sLayout.getHeight()) {
                    ((LinearLayout) act.findViewById(R.id.inerscroll)).getLayoutParams().height = sLayout.getHeight();
                    ((LinearLayout.LayoutParams)((LinearLayout)act.findViewById(R.id.lastButton)).getLayoutParams()).topMargin=((LinearLayout.LayoutParams)((LinearLayout)act.findViewById(R.id.lastButton)).getLayoutParams()).topMargin+(sLayout.getHeight()-alto-20);
                }

            }
        });

        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        //llenamos obtenemnos los campos de texto
        try {

            ((TextView) findViewById(R.id.nameText)).setText(info.getNombreContacto() != null ? AES.decrypt(info.getNombreContacto()).split(" ")[0] : "");
            ((TextView) findViewById(R.id.phoneText)).setText(info.getTelefonoPrincipal() != null ? AES.decrypt(info.getTelefonoPrincipal()).replaceAll("(.{2})(?!$)", "$1 ") : "");
            ((TextView) findViewById(R.id.accountText)).setText(info.getCvNumberAccount() != null ? AES.decrypt(info.getCvNumberAccount()): "");
            ((TextView) findViewById(R.id.paqueteText)).setText(info.getPaquete() != null ? AES.decrypt(info.getPaquete()): "No disponible");
            ((TextView) findViewById(R.id.totalText)).setText(info.getCvLastBalance() != null ? "$"+AES.decrypt(info.getCvLastBalance()): "0.00");

            String lastBalance=info.getCvLastBalance() != null ? AES.decrypt(info.getCvLastBalance()): "0.00";
            Double tot= Double.parseDouble(lastBalance);
            if(tot<=0){
                //debemos cambiar el mensaje del pago
                ((TextView) findViewById(R.id.leyenda1Text)).setText("Estás al corriente gracias por tu pago");
                ((TextView) findViewById(R.id.fechaText)).setVisibility(TextView.GONE);
            }
            double saldo=Double.parseDouble(lastBalance);
            lastBalance=baseFormat.format(saldo);
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
                            TextView myText = (TextView) findViewById(R.id.totalText );

                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(50); //You can manage the time of the blink with this parameter
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            myText.startAnimation(anim);
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
                if(!image.isEmpty()) {
                    loader.DisplayImage(image, (ImageView) findViewById(R.id.imageView1));
                }
            }
            if(MainActivity.facebookImg!=null){
                    loader.DisplayImage(MainActivity.facebookImg, (ImageView) findViewById(R.id.imageView1));
                    System.out.println(MainActivity.facebookImg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void servicios(View v){
        Intent myIntent = new Intent(this, ServiciosActivity.class);
        startActivityForResult(myIntent, 0);
    }
    public void estadoCuenta(View v){
        Intent myIntent = new Intent(this, EdoCuentaActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void foto(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 57);
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
            telefono="51699699";
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

    }
    public void establecimientos(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();

        Intent myIntent;
        if(!info.isLegacy())
            myIntent = new Intent(this, MediosDePagoActivity.class);
        else
            myIntent = new Intent(this, PagoEstablecimientosActivity.class);
        startActivityForResult(myIntent, 0);
    }
    public void pagos(View v){
        Intent myIntent = new Intent(this, ListaPagosActivity.class);
        startActivityForResult(myIntent, 0);

    }

    public void pay(View v){
        List<Card> tjts=new Select().from(Card.class).where("user=?", ((IzziMovilApplication)getApplication()).getCurrentUser().getUserName()).execute();
        if(tjts==null) {
            Intent myIntent = new Intent(this, AddCardActivity.class);
            startActivityForResult(myIntent, 0);
        }else if(tjts.size()<=0){
            Intent myIntent = new Intent(this, AddCardActivity.class);
            startActivityForResult(myIntent, 0);
        }else{
            Intent myIntent = new Intent(this, PagosMainActivity.class);
            startActivityForResult(myIntent, 0);
        }
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

}
