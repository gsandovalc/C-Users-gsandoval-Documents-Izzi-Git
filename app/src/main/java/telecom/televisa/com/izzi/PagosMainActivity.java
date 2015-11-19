package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import televisa.telecom.com.model.Card;
import televisa.telecom.com.model.Tokens;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.AsyncResponse;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.Util;
import televisa.telecom.com.util.izziPaymentResponse;
import televisa.telecom.com.util.izziTokenResponse;


public class PagosMainActivity extends Activity implements IzziRespondable{
    boolean togleRadio=false;
    boolean deleteCard=false;
    boolean getCards=false;
    Tokens selectedCard=null;
    List<View> lsta;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
    public static Map<String,String> parametros;

    NumberFormat baseFormat = NumberFormat.getCurrencyInstance(new Locale("es","MX"));

    IzziRespondable acti=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_main);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en línea");
        LayoutInflater inflater = LayoutInflater.from(this);
        if (((IzziMovilApplication)getApplication()).getCurrentUser()==null){
            finish();
            Intent i= new Intent(getApplicationContext(),BtfLanding.class);
            startActivity(i);
            return;
        }

        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        Map<String,String> parametross=new HashMap<>();
        parametross.put("METHOD","payments/getTokens");
        parametross.put("token",info.getToken());
        parametross.put("account",info.getCvNumberAccount());
        getCards=true;
        new AsyncResponse(this,false).execute(parametross);

        try {
            String lastBalance=info.getCvLastBalance() != null ? AES.decrypt(info.getCvLastBalance()): "0.00";
            double saldo=Double.parseDouble(lastBalance);
            lastBalance=baseFormat.format(saldo);
            ((LinearLayout)findViewById(R.id.pagggar)).setVisibility(LinearLayout.VISIBLE);
            if(((int)saldo)==0){
                ((LinearLayout)findViewById(R.id.pagggar)).setVisibility(LinearLayout.GONE);
            }
            String fecha=info.getFechaLimite() != null ? AES.decrypt(info.getFechaLimite()): null;
            String fechaFactura=info.getFechaFactura() != null ? AES.decrypt(info.getFechaFactura()): null;


            ((TextView) findViewById(R.id.totalText)).setText(lastBalance);
            try {
                if (fecha != null) {
                    if (!fecha.isEmpty() && !fecha.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fecha);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es","MX"));
                        DateFormat shortFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(AES.decrypt(info.getFechaLimit()));
                        ((TextView) findViewById(R.id.fechaTextMonth)).setText(AES.decrypt(info.getMesFactura()));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                            //TODO hacer el truco que quieren si tiene pago vencido
                        }
                    }else if(fechaFactura!=null){
                        if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                            Date fechaLimiteDate = sdf.parse(fechaFactura);
                            ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                            DateFormat mediumFormat = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es","MX"));
                            ((TextView) findViewById(R.id.fechaText)).setText(AES.decrypt(info.getFechaLimit()));
                            DateFormat shortFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                            ((TextView) findViewById(R.id.fechaTextMonth)).setText(AES.decrypt(info.getMesFactura()));
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
                        ((TextView) findViewById(R.id.fechaText)).setText(AES.decrypt(info.getFechaLimit()));
                        DateFormat shortFormat = new SimpleDateFormat("MMMMM", new Locale("es","MX"));
                        ((TextView) findViewById(R.id.fechaTextMonth)).setText(AES.decrypt(info.getMesFactura()));
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
        Intent myIntent = new Intent(this, AddCardActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void payAccount(View v){
       final Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            final Dialog popup = new Dialog(this,android.R.style.Theme_Translucent);
            popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popup.setCancelable(true);
            popup.setContentView(R.layout.popupcode);
            WindowManager.LayoutParams lp = popup.getWindow().getAttributes();
            lp.dimAmount=0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
            popup.getWindow().setAttributes(lp);
            popup.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            popup.show();
            LinearLayout llo=(LinearLayout)popup.findViewById(R.id.listo);
            llo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cvv="";
                    TextView tv= (TextView)popup.findViewById(R.id.pop_error);
                    tv.setVisibility(TextView.GONE);
                    try {
                        cvv = ((EditText) popup.findViewById(R.id.code)).getText().toString();
                        Integer.parseInt(cvv);
                    }catch(Exception e){
                        tv.setVisibility(TextView.VISIBLE);
                        return;
                    }
                    int tipot=Integer.parseInt(selectedCard.getCardType());
                    if(tipot==3&&cvv.length()!=4){
                        tv.setVisibility(TextView.VISIBLE);
                        return;
                    }else if(tipot!=3&&cvv.length()!=3){
                        tv.setVisibility(TextView.VISIBLE);
                        return;
                    }
                    Map<String,String> mp=new HashMap<>();

                    mp.put("METHOD","payments/payToken");
                    mp.put("token","inxnsinNSnimcennJISjijxmskomxnIMSm==");
                    String user;
                    try {
                        user = AES.encrypt(info.getUserName());
                        mp.put("account", info.getCvNumberAccount());
                        mp.put("code", AES.encrypt(cvv));
                        mp.put("subId", selectedCard.getSubscriptionId());
                        mp.put("ammount", info.getCvLastBalance());
                        mp.put("user", user);
                        new AsyncResponse(acti, false).execute(mp);
                    }catch(Exception e){

                    }
                    popup.dismiss();
                }
            });
            LinearLayout llos=(LinearLayout)popup.findViewById(R.id.nop);
            llos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });
            System.out.println(AES.decrypt(info.getCorreoContacto()));
           /*
            parametros=new HashMap<>();
            parametros.put("METHOD","payments/domicilia");

            parametros.put("token",info.getToken());

            user=AES.encrypt(info.getUserName());
            parametros.put("account",info.getCvNumberAccount());
            parametros.put("name", remove1(selectedCard.getName()));
            parametros.put("number",selectedCard.getNumber());
           date=AES.decrypt(selectedCard.getExpMonth())+"/"+AES.decrypt(selectedCard.getExpYear());
            parametros.put("date",AES.encrypt(date));
            parametros.put("code",selectedCard.getCvv());
            parametros.put("type",selectedCard.getType());
            parametros.put("ammount",info.getCvLastBalance());

            parametros.put("user",user);*/


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
// cambiar por error
            if(getCards){
                getCards=false;
                return;
            }
            if(deleteCard){
                deleteCard=false;
                return;
            }
            Intent i=new Intent(getApplicationContext(),PaymentFailActivity.class);
            startActivity(i);

            return;
        }
        getCards=false;
        if(response instanceof String){
            Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
            Map<String,String> parametross=new HashMap<>();
            parametross.put("METHOD","payments/getTokens");
            parametross.put("token",info.getToken());
            parametross.put("account",info.getCvNumberAccount());
            new AsyncResponse(this,false).execute(parametross);
            return;
        }
        if(response instanceof izziTokenResponse){
            ((LinearLayout) findViewById(R.id.contenedortjt)).removeAllViews();
            final List<Tokens> tjts=((izziTokenResponse) response).getResponse().getPidt();
            if(tjts.size()==0){
                Intent i= new Intent(getApplicationContext(),AddCardActivity.class);
                startActivity(i);
                finish();
                return;
            }
            if(tjts.size()>=3){
                ((RelativeLayout)findViewById(R.id.lastButton)).setVisibility(RelativeLayout.GONE);
            }else{
                ((RelativeLayout)findViewById(R.id.lastButton)).setVisibility(RelativeLayout.VISIBLE);
            }
            selectedCard=tjts.get(0);
            lsta=new ArrayList<>();
            for(int i=0;i<tjts.size();i++) {
                LayoutInflater inflater = LayoutInflater.from(this);
                final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.card_list_item_pay, null, false);
                lsta.add(layout);
                if(i==0)
                layout.findViewById(R.id.radioon).setVisibility(LinearLayout.VISIBLE);

                layout.findViewById(R.id.remove).setClickable(true);
                layout.findViewById(R.id.remove).setTag(tjts.get(i));
                layout.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tokens t=(Tokens)v.getTag();
                        try{

                        Map<String,String> map = new HashMap<String, String>();
                        map.put("METHOD","payments/deleteToken");
                        map.put("subId",t.getSubscriptionId());
                        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
                        map.put("token",info.getToken());
                        map.put("account",AES.encrypt(t.getAccountNumber()));
                         new AsyncResponse(acti,false).execute(map);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                layout.setClickable(true);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(View vi:lsta){
                             vi.findViewById(R.id.radioon).setVisibility(LinearLayout.GONE);
                        }
                        int index=lsta.indexOf(v);
                        lsta.get(index).findViewById(R.id.radioon).setVisibility(LinearLayout.VISIBLE);
                        selectedCard=tjts.get(index);
                    }
                });
                String type = tjts.get(i).getCardType();
                String typeName = "";
                int tipo=Integer.parseInt(type);
                switch (tipo) {
                    case 3:
                        typeName = "Amex/Vigente";
                        break;
                    case 1:
                        typeName = "Visa/Vigente";
                        break;
                    case 2:
                        typeName = "MasterCard/Vigente";
                        break;
                }
                ((TextView) layout.findViewById(R.id.vendortjt)).setText(typeName);
                String number = tjts.get(i).getCardDigits();
                String maskedNumber = "●●●● ●●●● ●●●● " + number;
                ((TextView) layout.findViewById(R.id.tjtnumber)).setText(maskedNumber);
                ((LinearLayout) findViewById(R.id.contenedortjt)).addView(layout, -1, (int) Util.dpToPx(this, 70));
            }
            //Acaba lo de la tarjeta me robo lo de abajo para el oncreate

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
