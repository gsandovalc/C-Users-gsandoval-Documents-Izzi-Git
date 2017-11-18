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
import android.widget.ImageView;
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
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        String asunto=getIntent().getStringExtra("asunto")==null?"Session de Chat":getIntent().getStringExtra("asunto");
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Builder builder = new Builder();
        try {

            //izzi.custhelp.com/app/chat/chat_landing/Incident.Subject/Sesion de Chat/Contact.Name.First/CARLOS/Contact.Name.Last/VELEZ/Contact.Address.Country/4/Contact.Address.StateOrProvince/235/Contact.Email.0.Address/cevelez@izzi.mx/Incident.CustomFields.c.tst_mat/16
            builder.scheme("https")
                    .authority("izzi.custhelp.com")
                    .appendPath("app")
                    .appendPath("chat")
                    .appendPath("chat_landing").appendPath("Incident.Subject").appendPath(asunto).appendPath("Contact.Name.First").appendPath(AES.decrypt(info.getCvNameAccount()).split(" ")[0]).appendPath("Contact.Name.Last")
                    .appendPath(AES.decrypt(info.getApellidoPaterno())).appendPath("Contact.Address.Country").appendPath("4").appendPath("Contact.Address.StateOrProvince").appendPath("235").appendPath("Contact.Email.0.Address")
                    .appendPath(info.getUserName()).appendPath("Incident.CustomFields.c.tst_mat").appendPath(AES.decrypt(info.getCvNumberAccount()));

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
    public void showMenu(View v){
        this.finish();
    }
}
