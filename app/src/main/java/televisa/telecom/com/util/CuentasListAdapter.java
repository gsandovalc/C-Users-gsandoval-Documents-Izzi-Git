package televisa.telecom.com.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import telecom.televisa.com.izzi.R;
import televisa.telecom.com.model.Cuentas;
import televisa.telecom.com.model.PagosList;

/**
 * Created by cevelez on 28/04/2015.
 */
public class CuentasListAdapter extends ArrayAdapter<Cuentas>{
    private List<Cuentas> cuentas;
    Context context;

    public CuentasListAdapter(Context context, int textViewResourceId,
                              List<Cuentas> cuentas, int type) {
        super(context, textViewResourceId, cuentas);
        this.cuentas = cuentas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Cuentas getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.cuenta_list_item, parent, false);
        }
        TextView nombre=(TextView)row.findViewById(R.id.name);
        TextView cuenta=(TextView)row.findViewById(R.id.cuenta);
        ImageView lugar=(ImageView)row.findViewById(R.id.icon);
        Cuentas cuent=cuentas.get(position);

        try {
            nombre.setText(AES.decrypt(cuent.getCuentaNombre()));
            cuenta.setText(AES.decrypt(cuent.getCuentaNumero()));
            lugar.setImageResource(cuent.isCuentaTipo()?R.drawable.izzinegocos:R.drawable.residencial);
        }catch(Exception e){
            e.printStackTrace();
        }
        return row;
    }
}
