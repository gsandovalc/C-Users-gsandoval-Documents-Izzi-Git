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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import televisa.telecom.com.model.Card;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.izziLoginResponse;
import televisa.telecom.com.util.izziTokenResponse;



public class AddCardActivity extends Activity implements IzziRespondable {

    String cardType="";
    Activity actv=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ((TextView)findViewById(R.id.h_title)).setText("Agregar una tarjeta");
        ((EditText)findViewById(R.id.cardNumber)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String numberc=((EditText)v).getText().toString();
                    if(numberc!=null)
                        if(numberc.length()<=17){
                            switch (CardType.detect(numberc)){
                                case VISA:
                                    cardType="visa";
                                    ((ImageView)findViewById(R.id.imageView5)).setAlpha(255);
                                    ((ImageView)findViewById(R.id.imageView6)).setAlpha(85);
                                    ((ImageView)findViewById(R.id.imageView7)).setAlpha(85);
                                    break;
                                case MASTERCARD:
                                    cardType="mastercard";
                                    ((ImageView)findViewById(R.id.imageView5)).setAlpha(85);
                                    ((ImageView)findViewById(R.id.imageView6)).setAlpha(255);
                                    ((ImageView)findViewById(R.id.imageView7)).setAlpha(85);
                                   break;
                                case AMERICAN_EXPRESS:
                                    cardType="amex";
                                    ((ImageView)findViewById(R.id.imageView5)).setAlpha(85);
                                    ((ImageView)findViewById(R.id.imageView6)).setAlpha(85);
                                    ((ImageView)findViewById(R.id.imageView7)).setAlpha(255);
                                    break;

                            }
                        }
                }
            }
        });
    }
    public void closeView(View v){
        this.finish();
    }

    public void saveCard(View v){
        String mes="";
        String ano="";
        String numero="";
        String name="";
        String type="";

        String errorText="";
        boolean error=false;
        mes=((EditText)findViewById(R.id.cardMonth)).getText().toString();
        ano=((EditText)findViewById(R.id.cardYear)).getText().toString();
        numero=((EditText)findViewById(R.id.cardNumber)).getText().toString();
        name=((EditText)findViewById(R.id.cardName)).getText().toString();
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

        else if(numero.length()!=16&&(type.equals("2")||type.equals("4"))){
            errorText="Ingresa un número de tarjeta valido";
            error=true;
        }else if(numero.length()!=15&&(type.equals("1"))){
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
        }else if(name.trim().length()==0){
            errorText="Ingresa el nombre que aparece en la tarjeta";
            error=true;
        }
        if(error){
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage(errorText)
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
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Map<String,String> mp=new HashMap<>();
        mp.put("METHOD","payments/insertToken");
        mp.put("token", info.getToken());
        mp.put("account",AES.encrypt(info.getCvNumberAccount()));
        mp.put("name", AES.encrypt(remove1(name)));
        mp.put("number",AES.encrypt(numero));
        String date=mes+"/"+ano;
        mp.put("tipo",AES.encrypt(type));
        mp.put("date",AES.encrypt(date));
        mp.put("user",AES.encrypt(info.getUserName()));
        new AsyncResponse(this,false).execute(mp);
    }catch(Exception e){

    }
    }
    public void scanCard(View v){
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
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
                ((TextView)findViewById(R.id.cardNumber)).setText(scanResult.cardNumber);


                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    ((TextView)findViewById(R.id.cardMonth)).setText(String.format("%02d", scanResult.expiryMonth)+"");
                    ((TextView)findViewById(R.id.cardYear)).setText((scanResult.expiryYear+"").substring(2));
                }
                cardType=scanResult.getCardType().name.toLowerCase();
                switch (cardType){
                    case "visa":
                        ((ImageView)findViewById(R.id.imageView5)).setAlpha(255);
                        ((ImageView)findViewById(R.id.imageView6)).setAlpha(85);
                        ((ImageView)findViewById(R.id.imageView7)).setAlpha(85);
                        break;
                    case "mastercard":
                        ((ImageView)findViewById(R.id.imageView5)).setAlpha(85);
                        ((ImageView)findViewById(R.id.imageView6)).setAlpha(255);
                        ((ImageView)findViewById(R.id.imageView7)).setAlpha(85);
                        break;
                    case "amex":
                        ((ImageView)findViewById(R.id.imageView5)).setAlpha(85);
                        ((ImageView)findViewById(R.id.imageView6)).setAlpha(85);
                        ((ImageView)findViewById(R.id.imageView7)).setAlpha(255);
                        break;

                }
                if (scanResult.cvv != null) {
                    ((LinearLayout)findViewById(R.id.hidescan)).setVisibility(LinearLayout.GONE);
                    ((TextView)findViewById(R.id.cardCVV)).setText(scanResult.cvv+"");

                }


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
    }//remove1
    @Override
    public void notifyChanges(Object response) {
            if(response!=null){
                izziTokenResponse tkn=(izziTokenResponse)response;
                if(tkn.getIzziErrorCode().equals("")){
                    new AlertDialog.Builder(this)
                            .setTitle("izzi")
                            .setMessage("Se agregó de forma correcta tu tarjeta")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.dismiss();
                                    actv.finish();
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            actv.finish();
                        }
                    })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("izzi")
                        .setMessage("Ocurrio un error al intentar guardar tu tarjeta./n Intenta nuevamente")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();

                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();

                    }
                })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }
    }

    public enum CardType {

        UNKNOWN,
        VISA("^4[0-9]{12}(?:[0-9]{3})?$"),
        MASTERCARD("^5[1-5][0-9]{14}$"),
        AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
        DINERS_CLUB("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
        DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$"),
        JCB("^(?:2131|1800|35\\d{3})\\d{11}$");

        private Pattern pattern;

        CardType() {
            this.pattern = null;
        }

        CardType(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public static CardType detect(String cardNumber) {

            for (CardType cardType : CardType.values()) {
                if (null == cardType.pattern) continue;
                if (cardType.pattern.matcher(cardNumber).matches()) return cardType;
            }
            if(cardNumber.length()==15&&cardNumber.charAt(0)=='3'){
                return AMERICAN_EXPRESS;
            }
            return UNKNOWN;
        }

    }
}
