package izzi.connect.controls;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by cevelez on 22/12/2015.
 */
public class IzziConnectRefresh {

    public static String refreshUser(String refresh_token,String client_id, String client_secret){
        try {
            String url = (Constantes.endpoint + "/ms_oauth/oauth2/endpoints/oauthservice/tokens?" + "grant_type=refresh_token&refresh_token=" + refresh_token);
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            request.setHeader("Authorization", "Basic " + Base64.encodeToString((client_id + ":" + client_secret).getBytes(), Base64.NO_WRAP));
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
            IzziConnectResponse rs = gson.fromJson(result.toString(), IzziConnectResponse.class);
            if (!rs.error_description.isEmpty())
                return "expired";
            HttpGet requestRes = new HttpGet(Constantes.endpoint + "/ms_oauth/resources/userprofile/me");
            requestRes.setHeader("Authorization", rs.getAccess_token());
            response = client.execute(requestRes);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
             result = new StringBuffer();
            line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);
            System.out.println(result);
            //si llego aca armar objeto de respuesta bien
            result=new StringBuffer(result.substring(0,result.length()-1)+",");
            result.append(resultadoT.substring(1));
            return result.toString();
        }catch(Exception e){
            return null;
        }
    }
}
