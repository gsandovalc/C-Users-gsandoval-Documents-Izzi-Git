package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PaymentFailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_fail);
        ((TextView)findViewById(R.id.h_title)).setText("Pago en línea");
    }

    public void closeView(View v){
        this.finish();
    }

    public void finishPayment(View v){
        finish();
    }
}
