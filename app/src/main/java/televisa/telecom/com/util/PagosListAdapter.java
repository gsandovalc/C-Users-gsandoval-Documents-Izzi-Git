package televisa.telecom.com.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import telecom.televisa.com.izzi.IzziMovilApplication;
import telecom.televisa.com.izzi.R;
import telecom.televisa.com.izzi.UserActivity;
import televisa.telecom.com.model.PagosList;
import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 28/04/2015.
 */
public class PagosListAdapter extends ArrayAdapter<PagosList>{
    private List<PagosList> pagos;
    Context context;
    String mesActual="";
    Activity ativi=null;
    int contador=0;
    PagosListAdapter act=this;
    Map<String,Integer> months=new HashMap<>();
    int[] colores=new int[]{0xFF00C1B5,0xFFFFA807,0xFFD60270,0xFFFCD116};
    public PagosListAdapter(Context context, int textViewResourceId,
                             List<PagosList> pagos, int type, Activity activi) {
        super(context, textViewResourceId, pagos);
        this.pagos = pagos;
        this.context=context;
        this.ativi=activi;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
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
        else
        return row;
        TextView fecha=(TextView)row.findViewById(R.id.fechaL);
        TextView monto=(TextView)row.findViewById(R.id.montoL);
        TextView lugar=(TextView)row.findViewById(R.id.formaL);
        ImageView img=(ImageView)row.findViewById(R.id.imageIcon);
        TextView mesView=(TextView)row.findViewById(R.id.textMes);
        TextView monthedo=(TextView)row.findViewById(R.id.monthedo);
        final RelativeLayout monthedoC=(RelativeLayout)row.findViewById(R.id.monthedoC);
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
                monthedoC.setVisibility(View.GONE);
            }else{
                mesView.setVisibility(View.VISIBLE);
                monthedoC.setVisibility(View.VISIBLE);
                mesActual=month;
                mesView.setText(mesActual.toUpperCase());
                if(contador>3)
                    contador=0;
                mesView.setTextColor(colores[contador++]);
                monthedoC.setBackgroundColor(colores[contador - 1]);
                monthedoC.setClickable(true);
                monthedoC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String mes= ((TextView)monthedoC.findViewById(R.id.monthedo)).getText().toString();
                        Usuario usuario=((IzziMovilApplication)context.getApplicationContext()).getCurrentUser();
                        Map <String,String> mp=new HashMap<>();
                        Map <String,String> meses=new HashMap<>();
                        meses.put("ENERO","1");
                        meses.put("FEBRERO","2");
                        meses.put("MARZO","3");
                        meses.put("ABRIL","4");
                        meses.put("MAYO","5");
                        meses.put("JUNIO","6");
                        meses.put("JULIO","7");
                        meses.put("AGOSTO","8");
                        meses.put("SEPTIEMBRE","9");
                        meses.put("OCTUBRE","10");
                        meses.put("NOVIEMBRE","11");
                        meses.put("DICIEMBRE","12");

                        try {
                            mp.put("METHOD", "estado/send");
                            mp.put("user", usuario.getUserName());
                            mp.put("cuenta", AES.decrypt(usuario.getCvNumberAccount()));
                            mp.put("month", meses.get(mes));
                            String ano= Calendar.getInstance().get(Calendar.YEAR)+"";
                            if(Integer.parseInt(meses.get(mes))==12){
                                if(Calendar.getInstance().get(Calendar.MONTH)==0){
                                    ano=(Calendar.getInstance().get(Calendar.YEAR)-1)+"";
                                }
                            }
                            mp.put("year",ano);
                            new AsyncResponse((IzziRespondable)ativi,true).execute(mp);

                        }catch(Exception e){
                            ((IzziRespondable)ativi).notifyChanges(null);
                        }
                    }
                });
                months.put(mesActual.toUpperCase(),position);
                monthedo.setText(mesActual.toUpperCase());
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
