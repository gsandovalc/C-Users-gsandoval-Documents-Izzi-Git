package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import televisa.telecom.com.model.Card;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.Util;
import televisa.telecom.com.util.izziPaymentResponse;


public class PagosMainActivity extends Activity implements IzziRespondable{
    boolean togleRadio=false;
    Card selectedCard=null;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_main);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en línea");
        LayoutInflater inflater = LayoutInflater.from(this);
        final List<Card> tjts=new Select().from(Card.class).where("user=?", ((IzziMovilApplication)getApplication()).getCurrentUser().getUserName()).execute();
       final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.card_list_item_pay, null, false);
        layout.findViewById(R.id.radioon).setVisibility(LinearLayout.VISIBLE);
        selectedCard=tjts.get(0);
       /*  layout.setClickable(true);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.findViewById(R.id.radioon).setVisibility(togleRadio?LinearLayout.GONE:LinearLayout.VISIBLE);
                togleRadio=!togleRadio;

                selectedCard=selectedCard==null?tjts.get(0):null;
            }
        });
        */
        try {
            String type = AES.decrypt(tjts.get(0).getType());
            String typeName="";
            switch(type){
                case "1":
                    typeName="Amex/Vigente";
                    break;
                case "2":
                    typeName="Visa/Vigente";
                    break;
                case "4":
                     typeName="MasterCard/Vigente";
                break;
            }
            ((TextView)layout.findViewById(R.id.vendortjt)).setText(typeName);
            String number=AES.decrypt(tjts.get(0).getNumber());
            String maskedNumber="●●●● ●●●● ●●●● "+number.substring(12);
            ((TextView)layout.findViewById(R.id.tjtnumber)).setText(maskedNumber);
            ((LinearLayout) findViewById(R.id.vista)).addView(layout, -1, (int) Util.dpToPx(this, 70));
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            String lastBalance=info.getCvLastBalance() != null ? AES.decrypt(info.getCvLastBalance()): "0.00";
            String fecha=info.getFechaLimite() != null ? AES.decrypt(info.getFechaLimite()): null;
            String fechaFactura=info.getFechaFactura() != null ? AES.decrypt(info.getFechaFactura()): null;
            ((TextView) findViewById(R.id.totalText)).setText(info.getCvLastBalance() != null ? "$"+AES.decrypt(info.getCvLastBalance()): "0.00");
            try {
                if (fecha != null) {
                    if (!fecha.isEmpty() && !fecha.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fecha);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                        DateFormat shortFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        ((TextView) findViewById(R.id.fechaTextMonth)).setText(shortFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                            //TODO hacer el truco que quieren si tiene pago vencido
                        }
                    }else if(fechaFactura!=null){
                        if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                            Date fechaLimiteDate = sdf.parse(fechaFactura);
                            ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                            DateFormat mediumFormat = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es","MX"));
                            ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                            DateFormat shortFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                            ((TextView) findViewById(R.id.fechaTextMonth)).setText(shortFormat.format(fechaLimiteDate));
                            Calendar cal = Calendar.getInstance();
                            if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                                //TODO hacer el truco que quieren si tiene pago vencido
                            }
                        }
                    }
                }else if(fechaFactura!=null){
                    if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fechaFactura);
                        ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        DateFormat shortFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaTextMonth)).setText(shortFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                            //TODO hacer el truco que quieren si tiene pago vencido
                        }
                    }else{
                        ((TextView) findViewById(R.id.fechaText)).setText("No disponible");
                    }
                }else{
                    ((TextView) findViewById(R.id.fechaText)).setText("No disponible");
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void editCard(View v) {
        finish();
        Intent myIntent = new Intent(this, EditCardActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void payAccount(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            System.out.println(AES.decrypt(info.getCorreoContacto()));
            Map<String,String> mp=new HashMap<>();
            mp.put("METHOD","payments");
            mp.put("token",info.getToken());
            String user;
            if(info.getUserName()==null)
                user=info.getCorreoContacto();
            else
                user=AES.encrypt(info.getUserName());
            mp.put("account",info.getCvNumberAccount());
            mp.put("name", remove1(selectedCard.getName()));
            mp.put("number",selectedCard.getNumber());
            String date=AES.decrypt(selectedCard.getExpMonth())+"/"+AES.decrypt(selectedCard.getExpYear());
            mp.put("date",AES.encrypt(date));
            mp.put("code",selectedCard.getCvv());
            mp.put("type",selectedCard.getType());
            mp.put("ammount",info.getCvLastBalance());

            mp.put("user",user);
            new AsyncResponse(this,false).execute(mp);
        }catch(Exception e){
            e.printStackTrace();
        }
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
    public void closeView(View v){
        this.finish();
    }

    @Override
    public void notifyChanges(Object response) {
        if(response==null){
            Intent i=new Intent(getApplicationContext(),PaymentFailActivity.class);
            startActivity(i);

            return;
        }
        izziPaymentResponse pago=(izziPaymentResponse)response;
        if(!pago.getIzziErrorCode().isEmpty()){
            Intent i=new Intent(getApplicationContext(),PaymentFailActivity.class);
            startActivity(i);

            return;
        }
       if( pago.getResponse()==null){
           Intent i=new Intent(getApplicationContext(),PaymentFailActivity.class);
           startActivity(i);

           return;
       }
        if(pago.getResponse().getPaymentError()!=null){
            if(!pago.getResponse().getPaymentError().isEmpty()){
                Intent i=new Intent(getApplicationContext(),PaymentFailActivity.class);
                startActivity(i);

                return;
            }
        }
        Intent i = new Intent(this, PaymentOkActivity.class);
        i.putExtra("auth", pago.getResponse().getAutorizacion());
        startActivity(i);
        finish();
    }

}
