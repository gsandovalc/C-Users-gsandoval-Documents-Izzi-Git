package izzi.connect.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;


import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IzziConnectActivity extends Activity {
    public static String CLIENT_ID="client_id";
    public static String CLIENT_SECRET="client_secret";
    public static String OAUTH_RESULT="OAUTH_RESULT";
    public static String FULL_RESPONSE="FULL_RESPONSE";
    String client_id;
    String client_secret;
    String pass;
    String usr;
    IzziConnectResponse rrrr;
    boolean fullResponse=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ic_activity_izzi_connect);
        client_id=getIntent().getStringExtra(IzziConnectActivity.CLIENT_ID);
        client_secret=getIntent().getStringExtra(IzziConnectActivity.CLIENT_SECRET);
        fullResponse=getIntent().getBooleanExtra(IzziConnectActivity.FULL_RESPONSE,true);

        if(client_id==null ||client_secret==null)
        {
            setResult(301);
            finish();
            return;
        }
    }

    public void closeView(View v){
        setResult(300);//cancelado por el usuario
        finish();
    }

    public void registro(View v){
        Intent myIntent = new Intent(this, ic_activity_izzi_registro.class);
        startActivityForResult(myIntent, 600);
    }

    public void login(View v) {
        String user = "";
        String password = "";
        user = ((EditText) findViewById(R.id.ic_user)).getText().toString();
        password = ((EditText) findViewById(R.id.ic_password)).getText().toString();
        if (user.isEmpty()) {
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
        if (password.isEmpty()) {
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
        if (!isEmailValid(user)) {
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
        usr=user;
        pass=password;
        new AsyncResponse(this,client_id,client_secret,user,password,fullResponse).execute();
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
    private void fireAction(Object response){
        if(response==null){
            setResult(303);
            finish();
            return;
        }
        IzziConnectResponse rs=(IzziConnectResponse)response;
        if(rs.access_token.isEmpty()){
            //debemos checar si el error tiene algo de grant
            if(rs.error_description.contains("grant")){
                //vamos a la de consentimiento
                Intent myIntent = new Intent(this, ConsentActivity.class);
                myIntent.putExtra(IzziConnectActivity.CLIENT_ID,client_id);
                myIntent.putExtra(IzziConnectActivity.CLIENT_SECRET,client_secret);
                myIntent.putExtra("PASSWORD",pass);
                myIntent.putExtra("USER",usr);
                startActivityForResult(myIntent, 200);
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("izzi")
                        .setMessage("Usuario o contraseña invalido")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                            }
                        })

                        .show();
                return;
            }
        }else{
            Intent i=new Intent();
            if(rs.getStatus()!=null){
                if(!rs.getStatus().toLowerCase().equals("activo")){
                    String errmsg="Por favor comunicate al servicio de atención a clientes";
                    if(rs.getStatus().toLowerCase().equals("no servicio"))
                        errmsg="Lo sentimos, debes contar con servicio de video";
                    if(rs.getStatus().toLowerCase().equals("inactivo pago"))
                        errmsg="Nuestros registros indican que tu cuenta presenta un adeudo";
                    if(rs.getStatus().toLowerCase().equals("suspendido pago"))
                        errmsg="Nuestros registros indican que tu cuenta presenta un adeudo";
                    new AlertDialog.Builder(this)
                            .setTitle("izzi")
                            .setMessage(errmsg)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.dismiss();
                                    setResult(303);
                                    finish();
                                    return;
                                }
                            })

                            .show();
                    return;
                }
            }
            if(rs.getPaquetes().toLowerCase().contains("blim")) {
                i.putExtra(IzziConnectActivity.OAUTH_RESULT, fullResponse?rs.getToken_type():rs.getUid());
                setResult(234, i);
                finish();
            }else{
                rrrr=rs;
                Intent myIntent = new Intent(this, IzziConnectContrataActivity.class);
                myIntent.putExtra(IzziConnectActivity.CLIENT_ID,client_id);
                myIntent.putExtra(IzziConnectActivity.CLIENT_SECRET,client_secret);
                myIntent.putExtra("PASSWORD",pass);
                myIntent.putExtra("USER",usr);
                myIntent.putExtra(IzziConnectActivity.OAUTH_RESULT, rs.getToken_type());
                myIntent.putExtra("ac_token",rs.getAccess_token());
                startActivityForResult(myIntent, 700);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==111){
            new AsyncResponse(this,client_id,client_secret,usr,pass,fullResponse).execute();///////////mover
            return;
        }else if(resultCode==711){
            Intent i=new Intent();
            i.putExtra(IzziConnectActivity.OAUTH_RESULT, rrrr.getToken_type());
            setResult(234, i);
            finish();
        }
    }

    private class AsyncResponse extends AsyncTask<Map<String,String>, Object, Object> {
    IzziConnectActivity act;
    String client_id;
    String client_secret;
        String user;
        String pass;
        boolean fullResponse;
        public AsyncResponse(IzziConnectActivity respondTo,String st1,String st2,String st3,String st4, boolean full) {
            this.act = respondTo;
            // hacer algo generico
            client_id=st1;
            client_secret=st2;
            user=st3;
            pass=st4;
            fullResponse=full;
        }

        @Override
        protected Object doInBackground(Map<String, String>... params) {
            try {

               // HttpPost request = new HttpPost(Constantes.endpoint + "/ms_oauth/oauth2/endpoints/oauthservice/tokens?" + "grant_type=password&username=" + user + "&password=" + pass + "&scope=UserProfile.me");
                String url=(Constantes.endpoint + "/ms_oauth/oauth2/endpoints/oauthservice/tokens?" + "grant_type=password&username=" + user + "&password=" + pass + "&scope=UserProfile.me" );
                //IzziConnectRefresh.refreshUser("eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCIsImtpZCI6Im9yYWtleSJ9.eyJvcmFjbGUub2F1dGgudXNlcl9vcmlnaW5faWRfdHlwZSI6IkxEQVBfVUlEIiwib3JhY2xlLm9hdXRoLnVzZXJfb3JpZ2luX2lkIjoidm11cmlsbG9AaXp6aS5teCIsImlzcyI6Ind3dy5vcmFjbGUuZXhhbXBsZS5jb20iLCJvcmFjbGUub2F1dGgucnQudHRjIjoicmVzb3VyY2VfYWNjZXNzX3RrIiwib3JhY2xlLm9hdXRoLnN2Y19wX24iOiJPQXV0aFNlcnZpY2VQcm9maWxlIiwiaWF0IjoxNDUwODI2NDY1MDAwLCJvcmFjbGUub2F1dGgudGtfY29udGV4dCI6InJlZnJlc2hfdG9rZW4iLCJleHAiOjE0NTA4NDA4NjUwMDAsInBybiI6bnVsbCwianRpIjoiYTY3ZDgxY2ItOTg5OC00MWFkLWFjM2UtOTMzYzgzOTI3ZjY4Iiwib3JhY2xlLm9hdXRoLmNsaWVudF9vcmlnaW5faWQiOiIxNTE2YjRiY2YyYTU0MzJhYjcxNTk4ODQxMWQwMjk0ZiIsIm9yYWNsZS5vYXV0aC5zY29wZSI6IlVzZXJQcm9maWxlLm1lIiwidXNlci50ZW5hbnQubmFtZSI6IkRlZmF1bHREb21haW4iLCJvcmFjbGUub2F1dGguaWRfZF9pZCI6IjEyMzQ1Njc4LTEyMzQtMTIzNC0xMjM0LTEyMzQ1Njc4OTAxMiJ9.eURv7Iyc2oa0xM48wWLrFtukS-hv2JXEAeZEqzN9A-iCnN2I3hDXcufdaDcD5SI1_iAiL3PV1luvwszgpUw76VfVutaOIzIN9F292PoyJMRC1dXRT02kJyrNrqbnG2avv7JOy_xaW4fq6e8wTKp-I6esP6iMSJKG7wTorakmuU4",client_id,client_secret);
                HttpClient client =new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                request.setHeader("Authorization","Basic "+Base64.encodeToString((client_id + ":" + client_secret).getBytes(), Base64.NO_WRAP));
                // add request header
                HttpResponse response = client.execute(request);
                System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null)
                    result.append(line);
                System.out.println(result);
                String resultadoT=result.toString();
                Gson gson = new Gson();
                IzziConnectResponse rs=gson.fromJson(result.toString(),IzziConnectResponse.class);
                if(!rs.error_description.isEmpty())
                    return rs;

                HttpGet requestRes = new HttpGet(Constantes.endpoint+"/ms_oauth/resources/userprofile/me");
                requestRes.setHeader("Authorization",rs.getAccess_token());
                response = client.execute(requestRes);
                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                result = new StringBuffer();
                line = "";
                Gson datos=new Gson();
                while ((line = rd.readLine()) != null)
                    result.append(line);
                System.out.println(result);
                IzziConnectResponse fin =datos.fromJson(result.toString(),IzziConnectResponse.class);
                //si llego aca armar objeto de respuesta bien
                result=new StringBuffer(result.substring(0,result.length()-1)+",");
                result.append(resultadoT.substring(1));
               // IzziConnectResponse fin=new IzziConnectResponse();//gson.fromJson(result.toString(),IzziConnectResponse.class);
                fin.setAccess_token(rs.getAccess_token());
                fin.setToken_type(result.toString());
                fin.setUid(resultadoT);

                return fin;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }


        }
        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
            try{
            act.fireAction(response);
            }catch(Exception e){}

        }
    }
}
