package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziRecuperaResponse;


public class RecuperaPass extends IzziActivity  implements IzziRespondable {
    String account;
    String tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_pass);
        ((TextView)findViewById(R.id.h_title)).setText("Recuperar contraseña");

    }
    public void closeView(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        finish();
    }
    public void email(View v){
        sendRequest("1");
    }
    public void sms(View v){
       sendRequest("2");
    }
    private void sendRequest(String type){
        Map<String,String>mp=new HashMap<>();
         account=((EditText)findViewById(R.id.account)).getText()+"";
        try {
            int num = Integer.parseInt(account);
        }catch(Exception e){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("El número de cuenta no esta en el formato correcto")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        if(account.length()!=8){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("El número de cuenta no esta en el formato correcto")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        try {
            tipo=type;
            mp.put("METHOD", "recupera");
            mp.put("account", AES.encrypt(account));
            mp.put("type", AES.encrypt(type));
            new AsyncResponse(this, false).execute(mp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",1);
            return;
        }
try {
    izziRecuperaResponse rs = (izziRecuperaResponse) response;
    if (rs.getIzziError().length() != 0) {
        showError(rs.getIzziError());
        return;
    }
    Intent i = new Intent(this, RecuperaFinActivity.class);
    i.putExtra("account", account);
    i.putExtra("type", tipo);
    i.putExtra("response", AES.decrypt(rs.getResponse().getInfo()));
    startActivityForResult(i, 233);
}catch(Exception e){
    showError("Ocurrio un error inesperado",1);
}
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==233){
            finish();
        }
    }

    void showError(String error){
       new AlertDialog.Builder(this)
               .setTitle("izzi")
               .setMessage(error==null?"Revisa tu conexion a internet":error)
               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       // continue with delete
                       dialog.dismiss();
                   }
               })
               .setIcon(android.R.drawable.ic_dialog_alert)
               .show();
   }
}
