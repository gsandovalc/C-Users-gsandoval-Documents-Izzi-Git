package televisa.telecom.com.ws;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.util.Constantes;

/**
 * Created by cevelez on 16/04/2015.
 */
public class IzziWS {
    private final String METHOD_TOLOGIN="login";
    public static final String METHOD_ENCRYPT="test/encrypt";
    static int timeoutSocket = 60000;
    public IzziWS() {
    }
    public static Object callWebService(Map<String,String> params,String method) throws Exception{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 60000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost request = new HttpPost(Constantes.endpoint+method);
        System.out.println(Constantes.endpoint+method);
        ArrayList<NameValuePair> postParameters;
        if(!params.isEmpty()){
            postParameters = new ArrayList<NameValuePair>();
            for(Map.Entry<String, String> entry : params.entrySet()) {
                postParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            request.setEntity(new UrlEncodedFormEntity(postParameters));
        }
        BasicResponseHandler handler = new BasicResponseHandler();
        String resultado = httpclient.execute(request, handler);
        httpclient.getConnectionManager().shutdown();

        return resultado;
    }

}

