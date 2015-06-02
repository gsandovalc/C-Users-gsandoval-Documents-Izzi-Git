package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;

import static android.net.Uri.*;


public class ChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ((TextView)findViewById(R.id.h_title)).setText("Chat");
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Builder builder = new Builder();
        try {
            builder.scheme("https")
                    .authority("izzi.custhelp.com")
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
    public void closeView(View v){
        this.finish();
    }
}
