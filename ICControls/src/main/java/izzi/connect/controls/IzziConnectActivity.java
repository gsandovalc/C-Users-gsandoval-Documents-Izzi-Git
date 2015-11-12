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
    String client_id;
    String client_secret;
    String pass;
    String usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ic_activity_izzi_connect);
         client_id=getIntent().getStringExtra(IzziConnectActivity.CLIENT_ID);
         client_secret=getIntent().getStringExtra(IzziConnectActivity.CLIENT_SECRET);
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
        new AsyncResponse(this,client_id,client_secret,user,password).execute();
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
            i.putExtra(IzziConnectActivity.OAUTH_RESULT,(Serializable)rs);
            setResult(234,i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==111){
            new AsyncResponse(this,client_id,client_secret,usr,pass).execute();
            return;
        }
    }

    private class AsyncResponse extends AsyncTask<Map<String,String>, Object, Object> {
    IzziConnectActivity act;
    String client_id;
    String client_secret;
        String user;
        String pass;
        public AsyncResponse(IzziConnectActivity respondTo,String st1,String st2,String st3,String st4) {
            this.act = respondTo;
            // hacer algo generico
            client_id=st1;
            client_secret=st2;
            user=st3;
            pass=st4;

        }

        @Override
        protected Object doInBackground(Map<String, String>... params) {
            try {

               // HttpPost request = new HttpPost(Constantes.endpoint + "/ms_oauth/oauth2/endpoints/oauthservice/tokens?" + "grant_type=password&username=" + user + "&password=" + pass + "&scope=UserProfile.me");
                String url=(Constantes.endpoint + "/ms_oauth/oauth2/endpoints/oauthservice/tokens?" + "grant_type=password&username=" + user + "&password=" + pass + "&scope=UserProfile.me" );
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
                while ((line = rd.readLine()) != null)
                    result.append(line);
                System.out.println(result);
                //si llego aca armar objeto de respuesta bien
                IzziConnectResponse fin=gson.fromJson(result.toString(),IzziConnectResponse.class);
                fin.setAccess_token(rs.getAccess_token());
                return fin;

            }catch (Exception e){
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
