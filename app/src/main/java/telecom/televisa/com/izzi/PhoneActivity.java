package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PhoneActivity extends IzziActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
    }
    public void problema1(View v){
        Intent i=new Intent(this,PhoneTroubleShootActivity.class);
        i.putExtra("problema","No tengo tono");
        i.putExtra("caso",1);
        startActivity(i);
    }

    public void problema2(View v){
        Intent i=new Intent(this,PhoneTroubleShootActivity.class);
        i.putExtra("problema","No puedo hacer/recibir llamadas");
        i.putExtra("caso",2);
        startActivity(i);
    }
    public void problema3(View v){
        Intent i=new Intent(this,PhoneTroubleShootActivity.class);
        i.putExtra("problema","Mi equipo no enciende");
        i.putExtra("caso",3);
        startActivity(i);
    }
    public void problema4(View v){
        Intent i=new Intent(this,PhoneTroubleShootActivity.class);
        i.putExtra("problema","Otros");
        i.putExtra("caso",4);
        startActivity(i);
    }




}
