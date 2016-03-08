package televisa.telecom.com.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import telecom.televisa.com.izzi.R;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Push;

/**
 * Created by cevelez on 28/04/2015.
 */
public class PushListAdapter extends ArrayAdapter<Push>{
    private List<Push> push;
    Context context;
    String mesActual="";
    int contador=0;
    int[] colores=new int[]{0xAA00C1B5,0xAAFFA807,0xAAD60270,0xAAFCD116};
    public PushListAdapter(Context context, int textViewResourceId,
                           List<Push> push, int type) {
        super(context, textViewResourceId, push);
        this.push = push;
        this.context=context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Push getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.push_list_item, parent, false);
        }
        TextView fecha=(TextView)row.findViewById(R.id.fechaL);
        TextView monto=(TextView)row.findViewById(R.id.montoL);
        TextView mes=(TextView)row.findViewById(R.id.formaL);
        ImageView img=(ImageView)row.findViewById(R.id.imageIcon);
        TextView mesView=(TextView)row.findViewById(R.id.textMes);
        Push cent=push.get(position);
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 =new SimpleDateFormat("dd MMM yyyy",new Locale("es","MX"));
        SimpleDateFormat sdf3 =new SimpleDateFormat("MMMM",new Locale("es","MX"));
        try {
            fecha.setText(sdf.format(new Date(Long.parseLong(cent.getFecha()))));
            String lugarCadena=cent.getMessage();

            if(lugarCadena.toLowerCase().contains("cta izzi")){
                img.setImageResource(R.drawable.limit);
                mesView.setTextColor(colores[0]);
                mesView.setText("Edo cuenta:Facturación");
            }else if(lugarCadena.toLowerCase().contains("ultimo dia")){
                img.setImageResource(R.drawable.edo);
                mesView.setTextColor(colores[1]);
                mesView.setText("Fecha de pago");
            }else {
                img.setImageResource(R.drawable.recuerda);
                mesView.setTextColor(colores[2]);
                mesView.setText("Aviso");
            }
            mes.setText(lugarCadena);
        }catch(Exception e){
            e.printStackTrace();
        }
        return row;
    }
}
