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

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import televisa.telecom.com.model.Card;
import televisa.telecom.com.util.AES;
import televisa.telecom.com.util.Util;


public class EditCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        ((TextView)findViewById(R.id.h_title)).setText("Editar tarjeta");
        LayoutInflater inflater = LayoutInflater.from(this);
        final List<Card> tjts=new Select().from(Card.class).where("user=?", ((IzziMovilApplication)getApplication()).getCurrentUser().getUserName()).execute();
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.card_edit_item, null, false);
        final LinearLayout vst=((LinearLayout) findViewById(R.id.vista));
        final RelativeLayout rrt=(RelativeLayout)vst.getParent();
        LinearLayout remove=(LinearLayout)layout.findViewById(R.id.remove);
        remove.setClickable(true);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vst.removeView(layout);
                new Delete().from(Card.class).where("user=?", ((IzziMovilApplication)getApplication()).getCurrentUser().getUserName()).execute();
                rrt.findViewById(R.id.lastButton).setVisibility(LinearLayout.VISIBLE);
                vst.findViewById(R.id.nocards).setVisibility(TextView.VISIBLE);
            }
        });
        try {
            String type = AES.decrypt(tjts.get(0).getType());
            String typeName = "";
            switch (type) {
                case "1":
                    typeName = "Amex/Vigente";
                    break;
                case "2":
                    typeName = "Visa/Vigente";
                    break;
                case "4":
                    typeName = "MasterCard/Vigente";
                    break;
            }
            ((TextView) layout.findViewById(R.id.vendortjt)).setText(typeName);
            String number = AES.decrypt(tjts.get(0).getNumber());
            String maskedNumber = "●●●● ●●●● ●●●● " + number.substring(12);
            ((TextView) layout.findViewById(R.id.tjtnumber)).setText(maskedNumber);
            ((LinearLayout) findViewById(R.id.vista)).addView(layout, -1, (int) Util.dpToPx(this, 70));

        }catch (Exception e){

        }
    }
    public void closeView(View v){
        this.finish();
    }

    public void addCard(View v){
        finish();
        Intent myIntent = new Intent(this, AddCardActivity.class);
        startActivityForResult(myIntent, 0);
    }
}
