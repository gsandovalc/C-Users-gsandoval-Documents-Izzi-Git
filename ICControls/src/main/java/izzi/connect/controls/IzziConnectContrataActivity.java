package izzi.connect.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class IzziConnectContrataActivity extends Activity {
    Dialog loadingOverlay;
    IzziConnectContrataActivity iacti=this;
    String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izzi_connect_contrata);
        token=getIntent().getStringExtra("ac_token");
    }

    public void cancelar(View v){
        setResult(100);
        finish();
    }

    public void contrata(View v){
        new AsyncResponse(this,"").execute(new HashMap<String, String>());
    }
      public void fireAction(Object o){
           if(o!=null){
               String s=o.toString();
              s= s.toLowerCase().trim().replace(" ","");
               if(s.contains("\"success\":\"true\"")){
                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                           this);
                   final Activity ac=this;
                   // set title
                   alertDialogBuilder.setTitle("izzi");

                   // set dialog message
                   alertDialogBuilder
                           .setMessage("Tu contratación se ha realizado con éxito")
                           .setCancelable(false)
                           .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog,int id) {

                                   iacti.setResult(711);
                                   iacti.finish();

                               }
                           });

                   // create alert dialog
                   AlertDialog alertDialog = alertDialogBuilder.create();

                   // show it
                   alertDialog.show();
               }else{
                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                           this);
                   final Activity ac=this;
                   // set title
                   alertDialogBuilder.setTitle("izzi");
                   final String  ress=o.toString();
                   // set dialog message
                   alertDialogBuilder
                           .setMessage("No fue posible contratar el servicio en este momento \n Por favor comunicate al 018002005000")
                           .setCancelable(false)
                           .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog,int id) {


                               }
                           });

                   // create alert dialog
                   AlertDialog alertDialog = alertDialogBuilder.create();

                   // show it
                   alertDialog.show();
               }
           }
       }
    private class AsyncResponse extends AsyncTask<Map<String,String>, Object, Object> {
        IzziConnectContrataActivity act;
        String client_id;
        String client_secret;
        String user;
        String pass;

        public AsyncResponse(IzziConnectContrataActivity respondTo, String st1) {
            this.act = respondTo;
            // hacer algo generico
            client_id = st1;


        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingOverlay = new Dialog((Context)act,android.R.style.Theme_Translucent);
            loadingOverlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingOverlay.setCancelable(true);
            loadingOverlay.setContentView(R.layout.ic_loading);
            WindowManager.LayoutParams lp = loadingOverlay.getWindow().getAttributes();
            lp.dimAmount=0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
            loadingOverlay.getWindow().setAttributes(lp);
            loadingOverlay.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loadingOverlay.show();
            LinearLayout cuadro1=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro1);
            LinearLayout cuadro2=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro2);
            LinearLayout cuadro3=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro3);
            LinearLayout cuadro4=(LinearLayout)loadingOverlay.findViewById(R.id.cuadro4);
            AnimationSet animationSet = new AnimationSet(true);
            AnimationSet animationSet2 = new AnimationSet(true);
            AnimationSet animationSet3 = new AnimationSet(true);
            AnimationSet animationSet4 = new AnimationSet(true);
            Animation an = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            Animation an2 = new  RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            Animation an3 = new  RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            Animation an4 = new  RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            an.setDuration(2000);
            an.setRepeatCount(-1);
            an2.setDuration(2000);
            an2.setRepeatCount(-1);
            an3.setRepeatCount(-1);
            an4.setRepeatCount(-1);
            an3.setDuration(2000);
            an4.setDuration(2000);
            an.setStartOffset(700);
            an2.setStartOffset(700);
            an3.setStartOffset(700);
            an4.setStartOffset(700);
            an.setFillAfter(true);
            an2.setFillAfter(true);
            an3.setFillAfter(true);
            an4.setFillAfter(true);


            TranslateAnimation a = new TranslateAnimation(0,0, 0,(Util.dpToPx((Activity)act,100)));
            a.setDuration(500);
            a.setFillAfter(true);
            TranslateAnimation a1 = new TranslateAnimation(0,0, 0,-(Util.dpToPx((Activity)act,100)));
            TranslateAnimation a2 = new TranslateAnimation(0,(Util.dpToPx((Activity)act,100)), 0,0);
            TranslateAnimation a3 = new TranslateAnimation(0,-(Util.dpToPx((Activity)act,100)), 0,0);
            a1.setDuration(500);
            a1.setFillAfter(true); //HERE
            a2.setDuration(500);
            a2.setFillAfter(true); //HERE
            a3.setDuration(500);
            a3.setFillAfter(true); //HERE
            animationSet.addAnimation(a);
            animationSet2.addAnimation(a1);
            animationSet3.addAnimation(a2);
            animationSet4.addAnimation(a3);
            animationSet.addAnimation(an);
            animationSet2.addAnimation(an2);
            animationSet3.addAnimation(an3);
            animationSet4.addAnimation(an4);
            cuadro1.startAnimation(animationSet);
            cuadro2.startAnimation(animationSet2);
            cuadro3.startAnimation(animationSet3);
            cuadro4.startAnimation(animationSet4);


        }
        @Override
        protected Object doInBackground(Map<String, String>... params) {
            try {
              return IzziConnectContrata.contrata(token);

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
            try{
                loadingOverlay.dismiss();
                act.fireAction(response);
            }catch(Exception e){}

        }
    }

}
