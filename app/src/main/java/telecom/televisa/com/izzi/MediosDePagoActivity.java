package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import televisa.telecom.com.controls.PopoverView;
import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.CodeBarGenerator;


public class MediosDePagoActivity extends Activity  {
    private static final int WHITE = 0x00FFFFFF;
    private static final int BLACK = 0xFFb1b1b1;
    NumberFormat baseFormat = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        setContentView(R.layout.activity_medios_de_pago);
        ((TextView)findViewById(R.id.h_title)).setText("Código de barras");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        //lo obtenemos desde el servicio
        try {
            String barcode_data = AES.decrypt(info.getBarcode());
            ((TextView)findViewById(R.id.codetext)).setText(barcode_data);
            Bitmap bitmap = null;
            ImageView iv = (ImageView) findViewById(R.id.codebar);
            bitmap = CodeBarGenerator.encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showMenu(View v){
        closeView(v);
    }

    public void closeView(View v){
        this.finish();
        overridePendingTransition( R.transition.slide_out_up, R.transition.slide_out_up );

    }
    public void showLugares(View v){
        finish();
        Intent i=new Intent(getApplicationContext(),PagoEstablecimientosActivity.class);
        startActivity(i);
    }

}
