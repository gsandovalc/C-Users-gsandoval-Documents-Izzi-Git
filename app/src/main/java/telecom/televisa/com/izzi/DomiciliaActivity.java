package telecom.televisa.com.izzi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import televisa.telecom.com.controls.TwoDigitsCardTextWatcher;
import televisa.telecom.com.util.IzziRespondable;

public class DomiciliaActivity extends IzziActivity implements IzziRespondable {
    String cardType="";
    Activity actv=this;
    EditText exp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domicilia);

        ((TextView)findViewById(R.id.h_title)).setText("Agregar una tarjeta");
        ((ImageView)findViewById(R.id.show_menu)).setImageResource(R.drawable.regresar);
        exp=(EditText)findViewById(R.id.cardExp);
        exp.addTextChangedListener(new TwoDigitsCardTextWatcher(exp));
    }

    @Override
    public void notifyChanges(Object response) {


    }

    @Override
    public void slowInternet() {

    }
}
