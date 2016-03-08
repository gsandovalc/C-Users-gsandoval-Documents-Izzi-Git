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
import java.util.Locale;

import telecom.televisa.com.izzi.R;
import televisa.telecom.com.model.PagosList;

/**
 * Created by cevelez on 28/04/2015.
 */
public class PagosListAdapter extends ArrayAdapter<PagosList>{
    private List<PagosList> pagos;
    Context context;
    String mesActual="";
    int contador=0;
    int[] colores=new int[]{0xAA00C1B5,0xAAFFA807,0xAAD60270,0xAAFCD116};
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
        ImageView img=(ImageView)row.findViewById(R.id.imageIcon);
        TextView mesView=(TextView)row.findViewById(R.id.textMes);
        PagosList cent=pagos.get(position);
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 =new SimpleDateFormat("dd MMM yyyy",new Locale("es","MX"));
        SimpleDateFormat sdf3 =new SimpleDateFormat("MMMM",new Locale("es","MX"));
        try {
            fecha.setText(sdf2.format(sdf.parse(AES.decrypt(cent.getPinfldauthdate()).split(" ")[0])));
            monto.setText("$ "+AES.decrypt(cent.getPinfldamount()));
            String lugarCadena=AES.decrypt(cent.getPinflddescr());

            String month=sdf3.format(sdf.parse(AES.decrypt(cent.getPinfldauthdate()).split(" ")[0]));
            if(month.equals(mesActual)) {
                mesView.setVisibility(View.GONE);
            }else{
                mesView.setVisibility(View.VISIBLE);
                mesActual=month;
                mesView.setText(mesActual.toUpperCase());
                if(contador>3)
                    contador=0;
                mesView.setTextColor(colores[contador++]);
            }
            if(lugarCadena.toLowerCase().contains("app")){
                img.setImageResource(R.drawable.pagol);
            }else if(lugarCadena.toLowerCase().contains("call")){
                img.setImageResource(R.drawable.pagot);
            }else if(lugarCadena.toLowerCase().contains("mx")){
                img.setImageResource(R.drawable.pagow);
            }else if(lugarCadena.toLowerCase().contains("sucursal")){
                img.setImageResource(R.drawable.pagoc);
            }else if(lugarCadena.toLowerCase().contains("tdc")){
                img.setImageResource(R.drawable.card);
                lugarCadena="Recurrente";
            }else {
                img.setImageResource(R.drawable.pagoe);
            }
            lugar.setText(lugarCadena);
        }catch(Exception e){
            e.printStackTrace();
        }
        return row;
    }
}
