package televisa.telecom.com.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import telecom.televisa.com.izzi.R;
import televisa.telecom.com.model.PagosList;

/**
 * Created by cevelez on 28/04/2015.
 */
public class PagosListAdapter extends ArrayAdapter<PagosList>{
    private List<PagosList> pagos;
    Context context;
    public PagosListAdapter(Context context, int textViewResourceId,
                             List<PagosList> pagos, int type) {
        super(context, textViewResourceId, pagos);
        this.pagos = pagos;
        this.context=context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public PagosList getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.pagos_list_item, parent, false);
        }
        TextView fecha=(TextView)row.findViewById(R.id.fechaL);
        TextView monto=(TextView)row.findViewById(R.id.montoL);
        TextView lugar=(TextView)row.findViewById(R.id.formaL);
        PagosList cent=pagos.get(position);
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 =new SimpleDateFormat("dd MMM yyyy");
        try {
            fecha.setText(sdf2.format(sdf.parse(AES.decrypt(cent.getPinfldauthdate()).split(" ")[0])));
            monto.setText("$ "+AES.decrypt(cent.getPinfldamount()));
            lugar.setText(AES.decrypt(cent.getPinflddescr()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return row;
    }
}
