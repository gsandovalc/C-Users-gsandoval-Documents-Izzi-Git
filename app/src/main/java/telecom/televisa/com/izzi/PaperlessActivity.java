package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;


public class PaperlessActivity extends IzziActivity implements IzziRespondable {
    Dialog popup;
    Activity actvty=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paperless);
        ((TextView)findViewById(R.id.h_title)).setText("Paperless");

    }


    public void closeView(View v){
        this.finish();
    }

    public void paperlesUp(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            System.out.println(AES.decrypt(info.getCorreoContacto()));
            Map<String,String>mp=new HashMap<>();
            mp.put("METHOD","paperless");
            mp.put("token",info.getToken());
            String user;
            if(info.getUserName()==null)
                user=info.getCorreoContacto();
            else
                user=AES.encrypt(info.getUserName());
            mp.put("email",user);
            mp.put("account",info.getCvNumberAccount());
            mp.put("name", info.getCvNameAccount());
            mp.put("user",user);
            new AsyncResponse(this,false).execute(mp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void notifyChanges(Object response) {
        if(response!=null){
            popup = new Dialog(this,android.R.style.Theme_Translucent);
            popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popup.setCancelable(true);
            popup.setContentView(R.layout.popup);
            WindowManager.LayoutParams lp = popup.getWindow().getAttributes();
            lp.dimAmount=0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
            popup.getWindow().setAttributes(lp);
            popup.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            popup.show();
            popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            String text="<font color=#000000>A partir de hoy recibirás tu estado de cuenta por correo electrónico. </font></br>" +
                    "<font color=#000000> Además, podrás consultarlo cuando quieras desde nuestra aplicación y pagina de internet</font>";
            ((TextView)popup.findViewById(R.id.pop_text)).setText(Html.fromHtml(text));
            LinearLayout llo=(LinearLayout)popup.findViewById(R.id.listo);
            llo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((IzziMovilApplication)actvty.getApplication()).getCurrentUser().setPaperless(true);
                    popup.dismiss();
                }
            });
        }
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);

    }

    public  boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
