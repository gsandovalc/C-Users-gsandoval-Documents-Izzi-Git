package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import televisa.telecom.com.model.Usuario;

public class AyudaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        ((TextView)findViewById(R.id.h_title)).setText("Ayuda");

    }
    public void showMenu(View v){
        finish();
    }
    public void legal(View v){
       Intent i = new Intent(getApplicationContext(), LegalesActivity.class);
        startActivity(i);
    }

    public void chat(View v){
        Intent  i = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(i);
    }
    public void edocuenta(View v){
        Intent i=new Intent(getApplicationContext(),EdoCuentaActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    public void callPay(View v){
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        String telefono="";
        if(info.isLegacy()){
            telefono="018001205000";
        }else{
            telefono="018001205000";
            System.out.println("Es izzi");
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono));
        startActivity(callIntent);
    }
    public void miizzi(View v){
        Intent i=new Intent(getApplicationContext(),UserActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
