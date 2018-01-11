package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.devmarvel.creditcardentry.library.CreditCardForm;

import java.util.HashMap;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import televisa.telecom.com.controls.TwoDigitsCardTextWatcher;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;

public class DomiciliaActivity extends IzziActivity implements IzziRespondable {
    String cardType="";
    Activity actv=this;
    EditText exp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domicilia);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            Answers.getInstance().logCustom(new CustomEvent("Domicilia").putCustomAttribute("user", info.getUserName()).putCustomAttribute("account", AES.decrypt(info.getCvNumberAccount())));
        }catch (Exception e){

        }
        ((TextView)findViewById(R.id.h_title)).setText("Agregar una tarjeta");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        exp=(EditText)findViewById(R.id.cardExp);
        exp.addTextChangedListener(new TwoDigitsCardTextWatcher(exp));
    }
    public void saveCard(View v){
        String mes="";
        String ano="";
        String numero="";
        String name="";
        String type="";

        String errorText="";
        CreditCardForm form = (CreditCardForm)findViewById(R.id.cardNumber);
        com.devmarvel.creditcardentry.library.CreditCard cc=form.getCreditCard();

        boolean error=false;
        if(((EditText)findViewById(R.id.cardExp)).getText().toString().isEmpty()){
            showError("Revisa la fecha de expiración",0);
            return;
        }
        mes=((EditText)findViewById(R.id.cardExp)).getText().toString().split("/")[0];
        ano=((EditText)findViewById(R.id.cardExp)).getText().toString().split("/")[1];
        numero=cc.getCardNumber().replace(" ","");
        try {
            cardType = cc.getCardType().name().toLowerCase();
        }catch(Exception e){
            cardType="";
        }

        //csv=((EditText)findViewById(R.id.cardCVV)).getText().toString();
        switch (cardType){
            case "visa":
                type="2";
                break;
            case "mastercard":
                type="4";
                break;
            case "amex":
                type="1";
                break;

        }
        int month=0;
        try {
            month = Integer.parseInt(mes);
        }catch(Exception e){
            month=0;
        }
        if (month == 0 || month > 12) {
            errorText = "Ingresa un mes valido";
            error = true;
        }

        if(!form.isCreditCardValid()){
            errorText="Ingresa un número de tarjeta valido";
            error=true;
        }
        else if(ano.length()!=2){
            errorText="El año de la tarjeta no es valido";
            error=true;
        }
        else if(mes.length()!=2){
            errorText="El mes de la tarjeta no es valido";
            error=true;
        }
      /*  else if(csv.length()!=3&&(type.equals("2")||type.equals("4"))){
            errorText="El codigo de seguridad de la tarjeta no es valido";
            error=true;
        }
        else if(csv.length()!=4&&(type.equals("1"))){
            errorText="El codigo de seguridad de la tarjeta no es valido";
            error=true;
        }*/
        else if(type.isEmpty()){
            errorText="Revisa el número de la tarjeta";
            error=true;
        }
        if(error){
            showError(errorText,0);
            return;
        }

        try {
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","payments/domicilia");
            mp.put("token", info.getToken());
            mp.put("account",info.getCvNumberAccount());
            mp.put("name", info.getCvNameAccount());
            mp.put("number",AES.encrypt(numero));
            String date=mes+"/"+ano;
            mp.put("type",AES.encrypt(type));
            mp.put("date",AES.encrypt(date));
            mp.put("code",AES.encrypt("123"));
            mp.put("ammount",AES.encrypt("123"));
            mp.put("user",AES.encrypt(info.getUserName()));
            new AsyncResponse(this,false).execute(mp);
        }catch(Exception e){

        }
    }

    public void scanCard(View v){
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                ((CreditCardForm)findViewById(R.id.cardNumber)).setCardNumber(scanResult.cardNumber,false);
                //((TextView)findViewById(R.id.cardNumber)).setText(scanResult.cardNumber);
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
    public String remove1(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }
    @Override
    public void notifyChanges(Object response) {

        showError("Servicio no disponible por el momento",0);
    }

    @Override
    public void slowInternet() {

    }
}
