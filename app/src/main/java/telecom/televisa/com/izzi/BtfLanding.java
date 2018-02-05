package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.Map;


public class BtfLanding extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_btf_landing);
            WebView wv = ((WebView) findViewById(R.id.wv));
            String url = "https://www.izzi.mx/unity/misc/";
            wv.loadUrl(url);
            wv.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    view.setVisibility(View.VISIBLE);
                }
            });
        }catch(Exception e){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_btf_landing2);
        }
    }


    public void goToLogin(View v) {
        if(((IzziMovilApplication)getApplication()).isLogged()){
            Intent myIntent = new Intent(this, UserActivity.class);
            startActivity(myIntent);
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            finish();
            return;
        }
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

    }

    public void registro(View v){
        Intent i = new Intent(this, Registro_main_activity.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        finish();
    }


}
