package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        String text="<font color=#000000>Recibe ayuda </font><font color=#FFA807>24/7</font>" +
                "<font color=#000000> para resolver problemas o dudas sobre </font><font color=#FFA807>equipos de cómputo</font>" +
                "<font color=#000000>, </font><font color=#FFA807>redes </font><font color=#000000>y </font><font color=#FFA807>dispositivos</font>" +
                "<font color=#000000>, así como soporte remoto y a domicilio. </font>";
        ((TextView)findViewById(R.id.disc)).setText(Html.fromHtml(text));
    }


    public void closeView(View v){
        this.finish();
    }

    public void chatNow(View v){
        JSONObject mapa = new JSONObject();
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            String name = AES.decrypt(info.getNombreContacto());
            mapa.put("Nombre", name );
            mapa.put("name", name);
            mapa.put("Apellido",AES.decrypt(info.apellidoPaterno));
            mapa.put("Teléfono",AES.decrypt(info.getTelefonoPrincipal()));
            mapa.put("Mensaje",((TextView)findViewById(R.id.commentario)).getText());
            String url=URLEncoder.encode(mapa.toString(),"UTF-8");
            url=url.replace("Tel%C3%A9fono","Tel%E9fono");
            String base64 = Base64.encodeToString(url.getBytes("UTF-8"), Base64.NO_WRAP);
           WebView wv= ((WebView)findViewById(R.id.webview));
            System.out.println("http://eficasia.s1gateway.com/webchat/s1chat.php?autosubmit=1&cpgid=5012678&fdata=" + base64);
            wv.loadUrl("http://eficasia.s1gateway.com/webchat/s1chat.php?autosubmit=1&cpgid=5012678&fdata="+base64);
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
        }catch (Exception c){
c.printStackTrace();
        }

    }
    public void callDrWifi(View v){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String phoneNumber = "018002221234";
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }
}
