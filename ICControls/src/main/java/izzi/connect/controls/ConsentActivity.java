package izzi.connect.controls;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;


public class ConsentActivity extends Activity {
    public static String CLIENT_ID="client_id";
    public static String CLIENT_SECRET="client_secret";
    String client_id;
    String client_secret;
    String pass;
    String usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        ((TextView)findViewById(R.id.infoprivacidad)).setText(getResources().getString(R.string.app_name)+" recibirá la siguiente información:");
        client_id=getIntent().getStringExtra(IzziConnectActivity.CLIENT_ID);
        client_secret=getIntent().getStringExtra(IzziConnectActivity.CLIENT_SECRET);
        pass=getIntent().getStringExtra("PASSWORD");
        usr=getIntent().getStringExtra("USER");
    }
    public void permitir(View v){
        new AsyncResponse(this,client_id,client_secret,usr,pass).execute();
    }
    public void cancelar(View v){
        setResult(100);
        finish();
    }

    private void fireAction(Object response){
        if(response==null){
            setResult(303);
            finish();
            return;
        }
       if(response instanceof  String){
           setResult(111);
           finish();
           return;
       }
    }
    private class AsyncResponse extends AsyncTask<Map<String,String>, Object, Object> {
        ConsentActivity act;
        String client_id;
        String client_secret;
        String user;
        String pass;
        public AsyncResponse(ConsentActivity respondTo,String st1,String st2,String st3,String st4) {
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
                String url=(Constantes.endpoint + "/ms_oauth/oauth2/endpoints/oauthservice/tokens?" + "grant_type=password&username=" + user + "&password=" + pass + "&scope=ConsentManagement.retrieve+ConsentManagement.grant+ConsentManagement.revoke" );
                HttpClient client =new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                request.setHeader("Authorization","Basic "+ Base64.encodeToString((client_id + ":" + client_secret).getBytes(), Base64.NO_WRAP));
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

                HttpClient client2 =new DefaultHttpClient();

                HttpPost requestRes = new HttpPost(Constantes.endpoint +"/ms_oauth/resources/consentmanagement/grant?scope=UserProfile.me&client_id="+client_id+"&oracle_user_id="+usr+"&lang=en");
                requestRes.setHeader("Authorization",rs.getAccess_token().toString());
                requestRes.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                HttpResponse response2 = client2.execute(requestRes);
                result = new StringBuffer();
                rd = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
                line = "";
                while ((line = rd.readLine()) != null)
                    result.append(line);
                System.out.println(result);
                if(response2.getStatusLine().getStatusCode()==200){
                    return "OK";
                }else{
                    return null;
                }
                //si llego aca armar objeto de respuesta bien

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
