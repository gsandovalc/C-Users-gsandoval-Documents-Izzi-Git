package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;


public class DrWifiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_wifi);
        ((TextView)findViewById(R.id.h_title)).setText("izzi Dr. Wifi");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Uri.Builder builder = new Uri.Builder();
        try {
            builder.scheme("https")
                    .authority("drwifi.custhelp.com")
                    .appendPath("app")
                    .appendPath("chat")
                    .appendPath("chat_launch")
                    .appendQueryParameter("name", AES.decrypt(info.getNombreContacto()))
                    .appendQueryParameter("last", AES.decrypt(info.getApellidoPaterno()))
                    .appendQueryParameter("mail", info.getUserName());
            String myUrl = builder.build().toString();

            final   WebView wv= ((WebView)findViewById(R.id.webview));
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    wv.setVisibility(WebView.VISIBLE);

                }
            };

            handler.postDelayed(runnable, 4000);

            wv.getSettings().setJavaScriptEnabled(true);
            wv.setVisibility(WebView.VISIBLE);
            wv.setWebViewClient(new WebViewClient());
            wv.requestFocus(View.FOCUS_DOWN);
            wv.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_UP:
                            if (!v.hasFocus())
                            {
                                v.requestFocus();
                            }
                            break;
                    }
                    return false;
                }
            });
            wv.loadUrl(myUrl);
        }catch(Exception e){

        }

    }


    public void showMenu(View v){
        this.finish();
    }



}
