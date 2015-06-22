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

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kissmetrics.sdk.KISSmetricsAPI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.GetGuide;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziLoginResponse;


public class MainActivity extends Activity implements IzziRespondable {
    CallbackManager callbackManager;
    MainActivity actividad=this;
    boolean fromfb=false;
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
            LoginManager mLoginManager = LoginManager.getInstance();
            mLoginManager.logOut();

        }catch(Exception e){}

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
        Map<String,String>mp=new HashMap<>();
        mp.put("METHOD","login");
        mp.put("user",user);
        mp.put("password",password);
        new AsyncResponse(this,true).execute(mp);
    }
    public void registro(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/registro"));
        startActivity(browserIntent);
    }

    public void olvideMiPass(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/home?log=in"));
        startActivity(browserIntent);

    }

    public void fbLogin(View v){
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
                System.out.println("Holaaaaa" + e);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
