package telecom.televisa.com.izzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class BtfLanding extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btf_landing);

    }


    public void goToLogin(View v) {
        if(((IzziMovilApplication)getApplication()).isLogged()){
            Intent myIntent = new Intent(this, UserActivity.class);
            startActivity(myIntent);
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            finish();
            return;
        }
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        finish();
    }
    public void recupera(View v){
        Intent i = new Intent(this, RecuperaPass.class);
        startActivity(i);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        finish();
    }


}
