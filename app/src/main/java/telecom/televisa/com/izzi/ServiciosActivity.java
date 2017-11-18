package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import televisa.telecom.com.model.ExtrasInt;
import televisa.telecom.com.model.ExtrasTv;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.Util;


public class ServiciosActivity extends IzziActivity {
    public static List<ExtrasInt> complementosINT;
    public static List<ExtrasTv> complementosTV;
    private List<LinearLayout> itemsInt;
    private List<LinearLayout> itemsTV;
    Usuario info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);
        info=((IzziMovilApplication)getApplication()).getCurrentUser();
       final ServiciosActivity activity=this;
        //llenamos obtenemnos los campos de texto

        try {
            String pac=info.getServiceName() != null ? AES.decrypt(info.getServiceName()): "No disponible";
            pac = pac.replaceAll("\\+", "\n\\+\n");
            System.out.println(pac);
            ((TextView) findViewById(R.id.textServicio)).setText(pac);
             LinearLayout internet=(LinearLayout)findViewById(R.id.extasint);
            if (info.isExtrasInternet()) {
                for (String complemento:info.getDataExtrasInternet()) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.complemento_item_int, null, false);
                    ((TextView)layout.findViewById(R.id.descriptionC)).setText(complemento);

                    internet.addView(layout, -1, (int)(int)Util.dpToPx(this,15));
                }
            }else
                internet.setVisibility(LinearLayout.GONE);

            LinearLayout tv=(LinearLayout)findViewById(R.id.extastv);
            if (info.isExtrasVideo()) {
                for (String complemento:info.getDataExtrasVideo()) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.complemento_item_tv, null, false);
                    ((TextView)layout.findViewById(R.id.descriptionC)).setText(complemento);

                    tv.addView(layout, -1, (int)Util.dpToPx(this,15));
                }
            }else
                tv.setVisibility(LinearLayout.GONE);

            if(!info.isExtrasInternet()&&!info.isExtrasVideo())
                ((LinearLayout)findViewById(R.id.extrascontainer)).setVisibility(LinearLayout.GONE);

            configInt();
            configTv();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void tvclick(View v){
        int index=itemsTV.indexOf((LinearLayout)v);
        ExtrasTv et=complementosTV.get(index);
        LinearLayout ll=itemsTV.get(index);
        if(et.isSelected()){
            ((ImageView)ll.findViewById(index%2==0?R.id.icon:R.id.icon2)).setImageResource(R.drawable.shopcart);
            et.setSelected(false);
        }else{
            ((ImageView)ll.findViewById(index%2==0?R.id.icon:R.id.icon2)).setImageResource(R.drawable.rcheck);
            et.setSelected(true);
        }
        for(ExtrasTv etv:complementosTV){
            List<String> le=et.getExcludeUtil();
            if(le!=null){
                for(String se:le){
                    if(se.equals(etv.getEx_id())) {
                        etv.setSelected(false);
                        int indx=complementosTV.indexOf(etv);
                        ((ImageView)itemsTV.get(indx).findViewById(indx%2==0?R.id.icon:R.id.icon2)).setImageResource(R.drawable.shopcart);
                    }
                }
            }
        }
        calculatechanges();
    }
    private void calculatechanges(){
        findViewById(R.id.clear).setVisibility(ImageView.GONE);
        LinearLayout ll=(LinearLayout)findViewById(R.id.haschanges);
        ll.setVisibility(LinearLayout.GONE);
        if(complementosINT!=null)
            for(ExtrasInt ei:complementosINT)
                if(ei.isSelected()) {
                    ll.setVisibility(LinearLayout.VISIBLE);
                    findViewById(R.id.clear).setVisibility(ImageView.VISIBLE);
                }
        if(complementosTV!=null)
            for(ExtrasTv ei:complementosTV)
                if(ei.isSelected()) {
                    ll.setVisibility(LinearLayout.VISIBLE);
                    findViewById(R.id.clear).setVisibility(ImageView.VISIBLE);
                }
                else{

                }
    }

    public void sendChanges(View v){
        Intent  i =new Intent(this,PurchaseActivity.class);
        startActivityForResult(i,6);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==6){
            complementosINT=null;
            complementosTV=null;
            finish();
        }
    }

    public void intclick(View v){
        int index=itemsInt.indexOf((LinearLayout)v);
        String s="";
        for(int i=0;i<complementosINT.size();i++){
            ExtrasInt ei=complementosINT.get(i);
            LinearLayout ll=itemsInt.get(i);
            if(i==index){
                if(ei.isSelected()){
                    ei.setSelected(false);
                    ((ImageView)ll.findViewById(R.id.icon)).setImageResource(R.drawable.shopcart);
                }else{
                    ei.setSelected(true);
                    ((ImageView)ll.findViewById(R.id.icon)).setImageResource(R.drawable.rcheck);
                }
                    continue;
            }
            ei.setSelected(false);
            ((ImageView)ll.findViewById(R.id.icon)).setImageResource(R.drawable.shopcart);
        }
calculatechanges();
    }
    private void configInt(){
     complementosINT=new Select().from(ExtrasInt.class).execute();
        if(complementosINT!=null){
            if(complementosINT.size()>0){
                LayoutInflater inflater = LayoutInflater.from(this);
                itemsInt=new ArrayList<>();
                LinearLayout parent=(LinearLayout)findViewById(R.id.posIntelem);
                int size=(int)Util.dpToPx(this,130);
                for(ExtrasInt ei:complementosINT){
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.complemento_internet, null, false);
                    ((TextView)layout.findViewById(R.id.intextname)).setText(ei.getName());
                    ((TextView)layout.findViewById(R.id.extprice)).setText("$"+ei.getPrice()+" al mes");
                    parent.addView(layout,size,size);
                    if(ei.isTienePromo()){
                        ( (TextView)layout.findViewById(R.id.promo)).setVisibility(View.VISIBLE);
                        ( (TextView)layout.findViewById(R.id.promo)).setText(ei.getPromo());
                    }else{
                        ( (TextView)layout.findViewById(R.id.promo)).setVisibility(View.GONE);
                    }
                    itemsInt.add(layout);
                }
            }else{
                ((LinearLayout)(findViewById(R.id.posintcontainer))).setVisibility(LinearLayout.GONE);
            }
        }else{
            ((LinearLayout)(findViewById(R.id.posintcontainer))).setVisibility(LinearLayout.GONE);
        }
    }
    private void configTv(){
        complementosTV=new Select().from(ExtrasTv.class).execute();
        if(complementosTV!=null){
            if(complementosTV.size()>0){
                LayoutInflater inflater = LayoutInflater.from(this);
                itemsTV=new ArrayList<>();
                LinearLayout parent=(LinearLayout)findViewById(R.id.posTvelem);
                LinearLayout layout=null;
                int contador=0;
                for(int i=0;i<complementosTV.size();i++){
                    contador=i;
                    ExtrasTv et=complementosTV.get(i);
                    if(i%2==0) {
                        layout = (LinearLayout) inflater.inflate(R.layout.complemento_tv, null, false);
                        ((TextView) layout.findViewById(R.id.intextname)).setText(et.getName());
                        ((TextView) layout.findViewById(R.id.extprice)).setText("$" + et.getPrice() + " al mes");
                        LinearLayout ll=((LinearLayout)layout.findViewById(R.id.container1));
                        ll.setTag(i);
                        if(et.isTienePromo())
                            ((TextView)layout.findViewById(R.id.promo1)).setText(et.getPromo());
                        else
                            ((TextView)layout.findViewById(R.id.promo1)).setText("");
                        ((TextView)layout.findViewById(R.id.descrip1)).setText(et.getDescription());

                        parent.addView(layout,-1,(int)Util.dpToPx(this,170));
                        itemsTV.add(ll);
                    }
                    else{
                        ((TextView) layout.findViewById(R.id.intextname2)).setText(et.getName());
                        ((TextView) layout.findViewById(R.id.extprice2)).setText("$" + et.getPrice() + " al mes");
                        LinearLayout ll=((LinearLayout)layout.findViewById(R.id.container2));
                        ll.setVisibility(LinearLayout.VISIBLE);
                        ll.setTag(i);
                        if(et.isTienePromo())
                            ((TextView)layout.findViewById(R.id.promo2)).setText(et.getPromo());
                        else
                            ((TextView)layout.findViewById(R.id.promo2)).setText("");
                        ((TextView)layout.findViewById(R.id.descrip2)).setText(et.getDescription());
                        itemsTV.add(ll);
                    }
                }
            }else{
                ((LinearLayout)(findViewById(R.id.postvcontainer))).setVisibility(LinearLayout.GONE);
            }
        }else{
            ((LinearLayout)(findViewById(R.id.postvcontainer))).setVisibility(LinearLayout.GONE);
        }
    }
    public void clear(View v){
        ImageView ll=(ImageView)findViewById(R.id.clear);
        ll.setVisibility(LinearLayout.GONE);
        if(complementosINT!=null)
            for(int i=0;i<complementosINT.size();i++) {
                complementosINT.get(i).setSelected(false);
                ((ImageView)itemsInt.get(i).findViewById(R.id.icon)).setImageResource(R.drawable.shopcart);
            }


        if(complementosTV!=null)
            for(int i=0;i<complementosTV.size();i++) {
                complementosTV.get(i).setSelected(false);
                ((ImageView)itemsTV.get(i).findViewById(i%2==0?R.id.icon:R.id.icon2)).setImageResource(R.drawable.shopcart);
            }
    calculatechanges();
    }
   public void showMenu(View v){
       this.finish();
   }


}
