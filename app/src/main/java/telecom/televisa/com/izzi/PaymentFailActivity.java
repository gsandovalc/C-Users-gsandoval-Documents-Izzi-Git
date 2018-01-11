package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import televisa.telecom.com.model.Usuario;
import televisa.telecom.com.util.AES;


public class PaymentFailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_fail);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en línea");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        Usuario info=((IzziMovilApplication)getApplication()).getCurrentUser();
        try {
            Answers.getInstance().logCustom(new CustomEvent("pago declinado").putCustomAttribute("user", info.getUserName()).putCustomAttribute("account", AES.decrypt(info.getCvNumberAccount())).putCustomAttribute("ammount",AES.decrypt(info.getCvLastBalance())));
        }catch (Exception e){

        }


    }

    public void showMenu(View v){
        this.finish();
    }

    public void finishPayment(View v){
        finish();
    }
}
