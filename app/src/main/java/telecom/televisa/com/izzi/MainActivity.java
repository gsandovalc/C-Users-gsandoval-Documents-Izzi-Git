package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.activeandroid.query.Delete;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import televisa.telecom.com.model.Cuentas;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziLoginResponse;


public class MainActivity extends Activity implements IzziRespondable {
    CallbackManager callbackManager;
    MainActivity actividad=this;
    boolean fromfb=false;
    boolean fromfbFail=false;

   public static String facebookImg=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        facebookImg=null;
        if(((IzziMovilApplication)getApplication()).isLogged()){
            Intent myIntent = new Intent(this, UserActivity.class);
            startActivityForResult(myIntent, 0);
            this.finish();
            return;
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "telecom.televisa.com.izzi",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    public void test(View v){
      //   customize these values to suit your needs.
      //  Intent izziOauth = new Intent(this, IzziConnectActivity.class);
        //izziOauth.putExtra(IzziConnectActivity.CLIENT_ID, "35ae4f9aa9574e1a86a9319e32f318d31");
        //izziOauth.putExtra(IzziConnectActivity.CLIENT_SECRET, "Y6XyZSibOpDsy9L00WBK");
        //izziOauth.putExtra(IzziConnectActivity.FULL_RESPONSE,true);

        //startActivityForResult(izziOauth, 201);
    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 234) {
            String result = data.getStringExtra(IzziConnectActivity.OAUTH_RESULT);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle("Respuesta");

            // set dialog message
            alertDialogBuilder
                    .setMessage(result)
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }
*/
    public void login(View v){
        String user="";
        String password="";
        user=((EditText)findViewById(R.id.user)).getText().toString();
        password=((EditText)findViewById(R.id.password)).getText().toString();
        if(user.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ingresa tu usuario")
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
        if(password.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ingresa tu contraseña")
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
        if(!isEmailValid(user)){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("El usuario no esta en el formato correcto")
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

        if(((LinearLayout)findViewById(R.id.bfblg)).getVisibility()==LinearLayout.VISIBLE) {
            Map<String, String> mp = new HashMap<>();
            mp.put("METHOD", "login/");
            mp.put("user", user);
            mp.put("password", password);
            new AsyncResponse(this, true).execute(mp);
        }else{
            if(AccessToken.getCurrentAccessToken()!=null){
                try {
                    Map<String, String> mp = new HashMap<>();
                    mp.put("METHOD", "login/fbreg");
                    mp.put("user", AES.encrypt(user));
                    mp.put("password", AES.encrypt(password));
                    mp.put("fbusrid", AES.encrypt(AccessToken.getCurrentAccessToken().getUserId()));
                    mp.put("fbimgurl", "https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large");
                    mp.put("fbiprourl", "https://www.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId());
                    mp.put("fbexptoken", AccessToken.getCurrentAccessToken().getExpires().getTime() + "");
                    mp.put("fbtoken", AccessToken.getCurrentAccessToken().getToken());
                    new AsyncResponse(actividad, false).execute(mp);
                   fromfbFail=false;

                    return;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Map<String, String> mp = new HashMap<>();
                mp.put("METHOD", "login/");
                mp.put("user", user);
                mp.put("password", password);
                new AsyncResponse(this, true).execute(mp);
            }
        }
    }
    public void registro(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/registro"));
        startActivity(browserIntent);
    }

    public void olvideMiPass(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/home?log=in"));
        startActivity(browserIntent);

    }
    public void closeView(View v){
        Intent i = new Intent(this, BtfLanding.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    public void fbLogin(View v){
        if(AccessToken.getCurrentAccessToken()!=null){
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","login/facebook");
            mp.put("userID",AccessToken.getCurrentAccessToken().getUserId());
            new AsyncResponse(actividad,true).execute(mp);
            fromfb=true;
            return;
        }
        com.facebook.login.widget.LoginButton btn = new LoginButton(this);
        btn.performClick();
        btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Map<String,String>mp=new HashMap<>();
                System.out.println("Holaaaaa"+ loginResult.getAccessToken().getUserId());
                facebookImg="https://graph.facebook.com/"+loginResult.getAccessToken().getUserId()  + "/picture?type=large";
                mp.put("METHOD","login/facebook");
                mp.put("userID",loginResult.getAccessToken().getUserId());
                new AsyncResponse(actividad,true).execute(mp);
                fromfb=true;
            }

            @Override
            public void onCancel() {

                System.out.println("Holaaaaa canceladooooo");
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("Holaaaaa" + e.getMessage());
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

        new Delete().from(Usuario.class).execute();
        new Delete().from(PagosList.class).execute();
        new Delete().from(Cuentas.class).execute();
        if(((izziLoginResponse)response).getIzziErrorCode().isEmpty()){
            Usuario sr=((izziLoginResponse)response).getResponse();
            String user="";
            String password="";
            user=((EditText)findViewById(R.id.user)).getText().toString();
            password=((EditText)findViewById(R.id.password)).getText().toString();
            if(fromfb){
                try {
                    sr.setUserName(AES.decrypt(sr.getExtra1()));
                    sr.setPassword(AES.decrypt(sr.getExtra2()));
                    sr.setFotoPerfil(AES.encrypt(facebookImg));
                }catch (Exception e){}
            }else {
                sr.setUserName(user);
                sr.setPassword(password);
            }
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
            Intent myIntent = new Intent(this, UserActivity.class);
            startActivityForResult(myIntent, 0);
        }else{
            // mostrar mensaje rosa de usuario o contraseña invalido si es el caso
            //si no popup
            if(((izziLoginResponse)response).getIzziErrorCode().equals("103")){
                ((izziLoginResponse)response).setIzziError("Tu cuenta de facebook no se encuentra vinculada con izzi, ayudanos a vincularla iniciando sesión");
                ((LinearLayout)findViewById(R.id.bfblg)).setVisibility(LinearLayout.GONE);
            }
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage(((izziLoginResponse)response).getIzziError())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }

    }


    public  boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
