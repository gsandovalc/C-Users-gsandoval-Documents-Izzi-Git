package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by cevelez on 09/03/2016.
 */
public class IzziActivity extends Activity  {
    protected void showError(String error,int type){
        if(type==0) {
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage(error)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        int layout=0;
        if(type==2)
            layout=R.layout.nointernet_error;
        else if(type==3)
            layout=R.layout.slowinternet_error;
        else
            layout=R.layout.unexpected_error;
        final Dialog popup = new Dialog(this,android.R.style.Theme_Translucent);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setCancelable(true);
        popup.setContentView(layout);
        WindowManager.LayoutParams lp = popup.getWindow().getAttributes();
        lp.dimAmount=0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        popup.getWindow().setAttributes(lp);
        popup.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popup.show();
        ((TextView)popup.findViewById(R.id.error_desc)).setText(error);
        ((RelativeLayout)popup.findViewById(R.id.fondoError)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }


}
