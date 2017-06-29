package izzi.connect.controls;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cevelez on 17/12/2015.
 */
public class IzziConnectContrata {

    public static String contrata(String contrata){
        try {
            String url = Constantes.portal+"/webApps/servicios/contratar/complemento";

            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("token", contrata));
            postParameters.add(new BasicNameValuePair("complemento", "Blim"));
            request.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpResponse response = client.execute(request);
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);
            return result.toString();
            //System.out.println(result);

        }catch(Exception e){
          return "error";
        }

    }
}
