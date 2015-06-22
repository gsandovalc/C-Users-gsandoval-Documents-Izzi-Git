package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;


public class MediosDePagoActivity extends Activity {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        setContentView(R.layout.activity_medios_de_pago);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en establecimientos");
        //lo obtenemos desde el servicio
        try {
            String barcode_data = AES.decrypt(info.getBarcode());
            ((TextView)findViewById(R.id.codetext)).setText(barcode_data);
            // barcode image
            Bitmap bitmap = null;
            ImageView iv = (ImageView) findViewById(R.id.codebar);
            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }

            try {

                String lastBalance = info.getCvLastBalance() != null ? AES.decrypt(info.getCvLastBalance()) : "0.00";
                double saldo=Double.parseDouble(lastBalance);
                lastBalance=baseFormat.format(saldo);
                String fecha = info.getFechaLimite() != null ? AES.decrypt(info.getFechaLimite()) : null;
                String fechaFactura = info.getFechaFactura() != null ? AES.decrypt(info.getFechaFactura()) : null;
                ((TextView) findViewById(R.id.totalText)).setText(lastBalance);
                if (fecha != null) {
                    if (!fecha.isEmpty() && !fecha.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fecha);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es", "MX"));
                        DateFormat shortFormat = new SimpleDateFormat("MMMM", new Locale("es", "MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        ((TextView) findViewById(R.id.fechaTextMonth)).setText(shortFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                            //TODO hacer el truco que quieren si tiene pago vencido
                        }
                    } else if (fechaFactura != null) {
                        if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                            Date fechaLimiteDate = sdf.parse(fechaFactura);
                            DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es", "MX"));
                            ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                            DateFormat shortFormat = new SimpleDateFormat("MMMM", new Locale("es", "MX"));
                            ((TextView) findViewById(R.id.fechaTextMonth)).setText(shortFormat.format(fechaLimiteDate));
                            Calendar cal = Calendar.getInstance();
                            ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                            if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                                //TODO hacer el truco que quieren si tiene pago vencido
                            }
                        }
                    }
                } else if (fechaFactura != null) {
                    if (!fechaFactura.isEmpty() && !fechaFactura.equals("0")) {
                        Date fechaLimiteDate = sdf.parse(fechaFactura);
                        DateFormat mediumFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("es", "MX"));
                        ((TextView) findViewById(R.id.fechaText)).setText(mediumFormat.format(fechaLimiteDate));
                        DateFormat shortFormat = new SimpleDateFormat("MMMM", new Locale("es", "MX"));
                        ((TextView) findViewById(R.id.fechaTextMonth)).setText(shortFormat.format(fechaLimiteDate));
                        Calendar cal = Calendar.getInstance();
                        ((TextView) findViewById(R.id.leyenda1Text)).setText("Fecha de facturación");
                        if (fechaLimiteDate.getTime() > cal.getTime().getTime()) {
                            //TODO hacer el truco que quieren si tiene pago vencido
                        }
                    } else {
                        ((TextView) findViewById(R.id.fechaText)).setText("No disponible");
                    }
                } else {
                    ((TextView) findViewById(R.id.fechaText)).setText("No disponible");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
    }


    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
    public void closeView(View v){
        this.finish();
    }
    public void showLugares(View v){
        finish();
        Intent i=new Intent(getApplicationContext(),PagoEstablecimientosActivity.class);
        startActivity(i);
    }

}
