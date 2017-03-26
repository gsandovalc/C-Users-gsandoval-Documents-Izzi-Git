package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.ImageLoader;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziPassResponse;
import televisa.telecom.com.ws.IzziWS;

public class EditAccountActivity extends IzziActivity implements IzziRespondable{
    Usuario info;
    Bitmap img=null;
    int step=0;
    int step2=0;
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");
    private final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");
    private String nwps;
    private String oldpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ((TextView)findViewById(R.id.h_title)).setText("Ajustes");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        info = ((IzziMovilApplication) getApplication()).getCurrentUser();
        init();
    }
    public void foto(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 57);
    }

    public void prevStep(View v){
        if(step==1){
            ((LinearLayout)findViewById(R.id.pass1)).animate().x(0).setDuration(500).start();
            ((LinearLayout)findViewById(R.id.prev2)).setVisibility(LinearLayout.GONE);
            ((ImageView)findViewById(R.id.next)).setImageResource(R.drawable.adelante);
            step=0;
        }else if(step==2){
            ((LinearLayout)findViewById(R.id.pass2)).animate().x(0).setDuration(500).start();
            step=1;
        }
    }
    public void nextStep(View v){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int ht = displaymetrics.heightPixels;
        int wt = displaymetrics.widthPixels;
        if(step==0) {
            ((LinearLayout)findViewById(R.id.pass1)).animate().x(-wt).setDuration(500).start();
            step=1;
            ((LinearLayout)findViewById(R.id.prev2)).setVisibility(LinearLayout.VISIBLE);
            ((ImageView)findViewById(R.id.next)).setImageResource(R.drawable.adelante);
            ((ImageView)findViewById(R.id.back)).setImageResource(R.drawable.regresar);
        }else if(step==1){
            String passnew=((EditText)findViewById(R.id.passwd)).getText().toString();
            String result=validateNewPass(passnew);

            if(!result.equals("Ok")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage(result).setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else{
                ((LinearLayout)findViewById(R.id.pass2)).animate().x(-wt).setDuration(500).start();
                nwps=passnew;
                step=2;
            }
        }else if(step==2){
            String passold=((EditText)findViewById(R.id.passwd2)).getText().toString();
            String error="";
            oldpass=passold;
            if (passold.isEmpty()) {
                error= "La contraseña esta vacia";
            }
            if(!error.isEmpty()){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage(error).setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else{
                Map<String,String> mp=new HashMap<>();
                mp.put("METHOD", "profile/updatePassword");
                mp.put("user", info.userName);
                mp.put("passactual", passold);
                mp.put("newpass", nwps+"");
                new AsyncResponse(this,true).execute(mp);
            }
        }
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
    public void init(){
        try {
            ((TextView) findViewById(R.id.nombre)).setText(AES.decrypt(info.getCvNameAccount()));
            ((TextView) findViewById(R.id.cuenta)).setText(AES.decrypt(info.getCvNumberAccount()));
            ((TextView) findViewById(R.id.mail)).setText(AES.decrypt(info.getCorreoContacto()));
            ((TextView) findViewById(R.id.movil)).setText(AES.decrypt(info.getTelefonoPrincipal()));
            ((TextView) findViewById(R.id.address)).setText(AES.decrypt(info.getCvMailAddres()));
            ImageLoader loader;
            loader=new ImageLoader(this);
            if(info.getFotoPerfil()!=null) {
                String image=AES.decrypt(info.getFotoPerfil());
                if(!image.isEmpty())
                    loader.DisplayImage(image, (ImageView) findViewById(R.id.imageView1));
            }
            if(MainActivity.facebookImg!=null)
                loader.DisplayImage(MainActivity.facebookImg, (ImageView) findViewById(R.id.imageView1));

        }catch(Exception e){

        }
    }
    public void showMenu(View v){
        finish();
    }

    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",999);
            return;
        }
        izziPassResponse ipr=(izziPassResponse)response;
        try {
           if(ipr.getResponse().getStatus().equals("OK")){
               showError("Tu contraseña se cambio con exito",0);
               //Actualizar
               info.setPassword(oldpass);
               info.save();
               ((EditText)findViewById(R.id.passwd)).setText("");
               ((EditText)findViewById(R.id.passwd2)).setText("");
               ((LinearLayout)findViewById(R.id.pass1)).animate().x(0).setDuration(500).start();
               ((LinearLayout)findViewById(R.id.pass2)).animate().x(0).setDuration(500).start();
               step=0;
               ((LinearLayout)findViewById(R.id.prev2)).setVisibility(LinearLayout.GONE);
               ((ImageView)findViewById(R.id.next)).setImageResource(R.drawable.adelante);
               ((ImageView)findViewById(R.id.back)).setImageResource(R.drawable.regresar);

           }else{
               if(ipr.getIzziErrorCode().equals("666")){
                   showError("Tu contraseña anterior es incorrecta",0);
               }else{
                   showError("Ocurrio un error inesperado",999);
               }
           }
        }catch(Exception e){
            showError("Ocurrio un error inesperado",999);
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu internet esta muy lento, por favor intenta de nuevo",3);
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
                ma.put("account",info.getCvNumberAccount());
                ma.put("user",AES.encrypt(info.getUserName()));
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

    public String validateNewPass(String pass1) {
        if (pass1 == null) {
            return "La contraseña esta vacia";
        }

        StringBuilder retVal = new StringBuilder();

        if (pass1.isEmpty()) {
           return "La contraseña esta vacia";
        }
        if (pass1.length() < 8) {

            return "La contraseña debe tener al menos 8 caracteres";
        }

        if (!hasNumber.matcher(pass1).find()) {
            return "La contraseña debe de tener al menos un número";
        }
        if(pass1.contains("ñ")||pass1.contains("Ñ")){
            return "La contraseña no debe de contener la letra ñ";
        }
        return "Ok";
    }
}
