package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.Util;


public class ServiciosActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);
       final Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
       final ServiciosActivity activity=this;
        //llenamos obtenemnos los campos de texto
        ((TextView)findViewById(R.id.h_title)).setText("Servicios contratados");
        try {
            String pac=info.getPaquete() != null ? AES.decrypt(info.getPaquete()): "No disponible";
            pac = pac.replaceAll("\\+", "\n\\+\n");
            System.out.println(pac);
            ((TextView) findViewById(R.id.textServicio)).setText(pac);
             LinearLayout telefono=(LinearLayout)findViewById(R.id.telefonia);
            if (info.isExtrasTelefono()) {
                for (String complemento:info.getDataExtrasTelefono()) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.complemento_item, null, false);
                    ((TextView)layout.findViewById(R.id.descriptionC)).setText(complemento);
                    telefono.addView(layout, -1, (int)Util.dpToPx(this,20));
                }
            }else
                telefono.setVisibility(LinearLayout.GONE);
             LinearLayout internet=(LinearLayout)findViewById(R.id.internet);
            if (info.isExtrasInternet()) {
                for (String complemento:info.getDataExtrasInternet()) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.complemento_item_int, null, false);
                    ((TextView)layout.findViewById(R.id.descriptionC)).setText(complemento);
                    internet.addView(layout, -1, (int)(int)Util.dpToPx(this,20));
                }
            }else
                internet.setVisibility(LinearLayout.GONE);

            LinearLayout tv=(LinearLayout)findViewById(R.id.video);
            if (info.isExtrasVideo()) {
                for (String complemento:info.getDataExtrasVideo()) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.complemento_item_tv, null, false);
                    ((TextView)layout.findViewById(R.id.descriptionC)).setText(complemento);
                    tv.addView(layout, -1, (int)Util.dpToPx(this,20));
                }
            }else
                tv.setVisibility(LinearLayout.GONE);

            if(!info.isExtrasInternet()&&!info.isExtrasVideo()&&!info.isExtrasTelefono())
                ((LinearLayout)findViewById(R.id.vacio)).setVisibility(LinearLayout.VISIBLE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


   public void closeView(View v){
       this.finish();
   }


}
