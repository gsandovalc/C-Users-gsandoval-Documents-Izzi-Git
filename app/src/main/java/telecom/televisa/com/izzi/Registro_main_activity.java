package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.facebook.AccessToken;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziValidateResponse;


public class Registro_main_activity extends IzziActivity implements IzziRespondable{
    Map<String,String> mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_main_activity);
        final TextView txtview2 = (TextView)findViewById(R.id.mostrar);
        final EditText txtview = (EditText)findViewById(R.id.password);
        txtview2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        txtview.setTransformationMethod(null);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        txtview.setTransformationMethod(new PasswordTransformationMethod());

                        break;
                }
                return false;            }
        });

        txtview.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String pass="";
                if(s==null) {
                    pass = "";
                }else{
                    pass=s.toString();
                }
                if(pass.isEmpty()){
                    findViewById(R.id.mostrar).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.mostrar).setVisibility(View.VISIBLE);
                }
                int rating=getRating(pass);
                switch(rating){
                    case 0:
                        ((LinearLayout)findViewById(R.id.str1)).setBackgroundColor(0x00df0522);
                        ((LinearLayout)findViewById(R.id.str2)).setBackgroundColor(0x00df0522);
                        ((LinearLayout)findViewById(R.id.str3)).setBackgroundColor(0x00df0522);
                        ((LinearLayout)findViewById(R.id.str4)).setBackgroundColor(0x00df0522);
                        break;
                    case 1:
                        ((LinearLayout)findViewById(R.id.str1)).setBackgroundColor(0xffdf0522);
                        ((LinearLayout)findViewById(R.id.str2)).setBackgroundColor(0x22df0522);
                        ((LinearLayout)findViewById(R.id.str3)).setBackgroundColor(0x22df0522);
                        ((LinearLayout)findViewById(R.id.str4)).setBackgroundColor(0x22df0522);
                        break;
                    case 2:
                        ((LinearLayout)findViewById(R.id.str1)).setBackgroundColor(0xfffcd116);
                        ((LinearLayout)findViewById(R.id.str2)).setBackgroundColor(0xfffcd116);
                        ((LinearLayout)findViewById(R.id.str3)).setBackgroundColor(0x22fcd116);
                        ((LinearLayout)findViewById(R.id.str4)).setBackgroundColor(0x22fcd116);
                        break;
                    case 3:
                        ((LinearLayout)findViewById(R.id.str1)).setBackgroundColor(0xfffcd116);
                        ((LinearLayout)findViewById(R.id.str2)).setBackgroundColor(0xfffcd116);
                        ((LinearLayout)findViewById(R.id.str3)).setBackgroundColor(0xfffcd116);
                        ((LinearLayout)findViewById(R.id.str4)).setBackgroundColor(0x22fcd116);
                        break;
                    case 4:
                        ((LinearLayout)findViewById(R.id.str1)).setBackgroundColor(0xff4a9c40);
                        ((LinearLayout)findViewById(R.id.str2)).setBackgroundColor(0xff4a9c40);
                        ((LinearLayout)findViewById(R.id.str3)).setBackgroundColor(0xff4a9c40);
                        ((LinearLayout)findViewById(R.id.str4)).setBackgroundColor(0xff4a9c40);
                        break;

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    private int getRating(String password) throws IllegalArgumentException {
        if (password == null) {return 0;}
        int passwordStrength = 0;
        if (password.length() > 8) {passwordStrength++;} // minimal pw length of 6
        if (password.toLowerCase()!= password) {passwordStrength++;} // lower and upper case
        if (password.length() > 9) {passwordStrength++;} // good pw length of 9+
        int numDigits= getNumberDigits(password);
        if (numDigits > 0 && numDigits != password.length()) {passwordStrength++;} // contains digits and non-digits
        return passwordStrength;
    }
    private int getNumberDigits(String inString){
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits= 0;
        int length= inString.length();
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }

    private boolean isEmpty(String inString) {
        return inString == null || inString.length() == 0;
    }
    public void closeView(View v){
        Intent i = new Intent(this, BtfLanding.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        finish();
    }
    public void register(View v){
        String user="";
        String password="";
        String contrato="";
        user=((EditText)findViewById(R.id.user)).getText().toString();
        password=((EditText)findViewById(R.id.password)).getText().toString();
        contrato=((EditText)findViewById(R.id.contrato)).getText().toString();
        if(contrato.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ingresa tu número de cuenta")
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
        if(contrato.length()<8) {
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Tu número de cuenta es incorrecto")
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

        if(user.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ingresa tu usuario")
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
        if(password.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ingresa tu contraseña")
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
        if(password.length()<8){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("La contraseña debe de ser de almenos 8 caracteres")
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
        if(!isEmailValid(user)){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("El usuario no esta en el formato correcto")
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
        mp=new HashMap<>();
        mp.put("METHOD","registro/validate");
        mp.put("user", user);
        mp.put("pass", password);
        mp.put("account", contrato);
        new AsyncResponse(this,true).execute(mp);
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

    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            showError("Ocurrio un error inesperado",1);
            return;
        }
        izziValidateResponse resp=(izziValidateResponse)response;
        if(!resp.getIzziErrorCode().isEmpty()){
            showError(resp.getIzziError(),resp.getIzziError().contains("ines")?1:0);
            return;
        }
        Intent i=new Intent(this,RegistroStep2Activity.class);
        try {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("registro paso 1").putContentType("formulario").putCustomAttribute("account", AES.decrypt(mp.get("account"))));
        }catch(Exception e){

        }
        i.putExtra("response",resp);
        i.putExtra("user",(HashMap)mp);
        startActivityForResult(i,20);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==207)
        finish();
    }

    @Override
    public void slowInternet() {
        showError("Tu conexión esta muy lenta\n Por favor, intenta de nuevo",3);

    }
}
