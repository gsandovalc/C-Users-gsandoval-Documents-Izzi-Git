package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LegalesActivity extends Activity {
    boolean wvVisible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legales);
        ((TextView)findViewById(R.id.h_title)).setText("Avisos legales");
    }

     public void legalesIzzi(View v){
        /* WebView wv= ((WebView)findViewById(R.id.webview));
         wv.setVisibility(WebView.VISIBLE);
         wv.getSettings().setJavaScriptEnabled(true);
         wv.loadUrl("https://www.izzi.mx/pdf?file=/legales/terminos-condiciones-izzi");

         wvVisible=true;
         wv.getSettings().setDomStorageEnabled(true);*/
         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/pdf?file=/legales/terminos-condiciones-izzi"));
         startActivity(browserIntent);
     }

    public void privIzzi(View v){
        /* WebView wv= ((WebView)findViewById(R.id.webview));
         wv.setVisibility(WebView.VISIBLE);
         wv.getSettings().setJavaScriptEnabled(true);
         wv.loadUrl("https://www.izzi.mx/pdf?file=/legales/terminos-condiciones-izzi");

         wvVisible=true;
         wv.getSettings().setDomStorageEnabled(true);*/
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/pdf?file=/legales/aviso-privacidad-izzi"));
        startActivity(browserIntent);
    }//legalesIzziApp

    public void legalesIzziApp(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.izzi.mx/pdf?file=/legales/terminos-condiciones-izzi-app"));
        startActivity(browserIntent);
    }

    public void closeView(View v){
        if(wvVisible){
            WebView wv= ((WebView)findViewById(R.id.webview));
            wv.setVisibility(WebView.GONE);
            wvVisible=false;
        }else
            this.finish();
    }


}
