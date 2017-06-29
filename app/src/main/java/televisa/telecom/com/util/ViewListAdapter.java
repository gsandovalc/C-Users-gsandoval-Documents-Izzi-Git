package televisa.telecom.com.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import telecom.televisa.com.izzi.IzziMovilApplication;
import telecom.televisa.com.izzi.R;

/**
 * Created by cevelez on 11/05/2015.
 */
public class ViewListAdapter extends ArrayAdapter<izziGuideResponse.TVStation> {
    int [] ids=new int[]{R.id.fnd,R.id.fnd1,R.id.fnd2,R.id.fnd3,R.id.fnd4,R.id.fnd5,R.id.fnd6,R.id.fnd7,R.id.fnd8,R.id.fnd9,R.id.fnd10,R.id.fnd11,R.id.fnd12,R.id.fnd13,R.id.fnd14,R.id.fnd15,R.id.fnd16,R.id.fnd17,R.id.fnd18,R.id.fnd19,R.id.fnd20,R.id.fnd21,
            R.id.fnd22,R.id.fnd23,R.id.fnd24,R.id.fnd25,R.id.fnd26,R.id.fnd27,R.id.fnd28,R.id.fnd29,R.id.fnd30,R.id.fnd31,R.id.fnd32,R.id.fnd33,R.id.fnd34,R.id.fnd35};
    public Long startdate;
    private List<izziGuideResponse.TVStation> objects;
    Context context;
    int anchoCelda=200;
    Calendar cl= Calendar.getInstance();
    Calendar cl2=Calendar.getInstance();
    int filter=0;
    SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public ViewListAdapter(Context context, int textViewResourceId, List<izziGuideResponse.TVStation> objects,Activity act, int filter) {
        super(context, textViewResourceId, objects);
        anchoCelda=(int)Util.dpToPx(act, 150);
        this.objects = objects;
        List<izziGuideResponse.TVStation> objectsAux=new ArrayList<>();
        if(filter>0){
            for (int i=0;i<objects.size();i++) {
                int canall = Integer.parseInt(objects.get(i).getCanal());
                if (filter == 1 && canall > 500 && canall < 600)
                    objectsAux.add(objects.get(i));
                if (filter == 2 && canall > 200 && canall < 300)
                    objectsAux.add(objects.get(i));
                if (filter == 3 && canall > 300 && canall < 400)
                    objectsAux.add(objects.get(i));
            }
            this.objects=objectsAux;
        }
        this.context=context;
        this.filter=filter;
    }

    @Override
    public int getCount() {
        return objects==null?0:objects.size();
    }

    @Override
    public izziGuideResponse.TVStation getItem(int index) {
        return objects.get(index);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        try {
            if(row == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.shows, parent, false);
                AbsListView.LayoutParams paramsp =  (android.widget.AbsListView.LayoutParams) row.getLayoutParams();
                paramsp.width=anchoCelda*12;
            }

            final List<izziGuideResponse.StationRecord> program = getItem(position).getProgramas();
            final izziGuideResponse.TVStation programm=getItem(position);
            final Calendar cl=Calendar.getInstance();
           final Calendar cl3=Calendar.getInstance();
            int min=cl2.get(Calendar.MINUTE);
            min=min>=30?30:0;
            cl2.set(Calendar.MINUTE, min);
            cl2.set(Calendar.SECOND, 0);
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            int contador=0;

            for(int i=0;i<program.size();i++){
                final izziGuideResponse.StationRecord programa=program.get(i);
                if(program.get(i).getDescripcion()==null||program.get(i).getDescripcion().equals("null"))
                    program.get(i).setDescripcion("Descripción no disponibe");
                cl.setTime(new Date(Long.parseLong(program.get(i).getHorario().getEnd())));
                cl3.setTime(new Date(Long.parseLong(program.get(i).getHorario().getInicial())));
                contador++;
                if(startdate>=cl.getTimeInMillis()){
                    ((LinearLayout)row.findViewById(ids[i])).setVisibility(LinearLayout.GONE);
                    continue;
                }
                ((LinearLayout)row.findViewById(ids[i])).setVisibility(LinearLayout.VISIBLE);

                ((TextView)row.findViewById(ids[i]).findViewById(R.id.description)).setText(program.get(i).getDescripcion());
                ((TextView)row.findViewById(ids[i]).findViewById(R.id.startDate)).setText(String.valueOf(program.get(i).getHorario().getInicial()));
                ((TextView)row.findViewById(ids[i]).findViewById(R.id.endDate)).setText(String.valueOf(program.get(i).getHorario().getEnd()));
                //((TextView)row.findViewById(ids[i]).findViewById(R.id.filter)).setText(program.get(i).getProGenresDesc());
                ((TextView)row.findViewById(ids[i]).findViewById(R.id.channel_name)).setText(program.get(i).getTitulo());
                float scale = context.getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (10*scale + 0.5f);
                ((LinearLayout)(row.findViewById(ids[i]).findViewById(R.id.description)).getParent()).setPadding(dpAsPixels, 0, 0, 0);
                ((LinearLayout)row.findViewById(ids[i])).setClickable(true);
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                ((LinearLayout.LayoutParams)(((TextView)row.findViewById(ids[i]).findViewById(R.id.channel_name))).getLayoutParams()).leftMargin=15;
                ((LinearLayout.LayoutParams)(((TextView)row.findViewById(ids[i]).findViewById(R.id.hours))).getLayoutParams()).leftMargin=15;

                //((TextView)row.findViewById(ids[i]).findViewById(R.id.hours)).setText(""+sdf.format(formatoDelTexto.parse(program.get(i).getSkeAirDateTime()))+" - "+sdf.format(formatoDelTexto.parse(program.get(i).getSkeEndDateTime())));
               // CharSequence fils=((TextView)row.findViewById(ids[i]).findViewById(R.id.filter)).getText();

                ((LinearLayout)row.findViewById(ids[i])).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        final Dialog  popup = new Dialog(context,android.R.style.Theme_Translucent);
                        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        popup.setCancelable(true);
                        popup.setContentView(R.layout.program_popup);
                        WindowManager.LayoutParams lp = popup.getWindow().getAttributes();
                        lp.dimAmount=0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
                        popup.getWindow().setAttributes(lp);
                        popup.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        popup.show();
                        String text="<font color=#000000>A partir de hoy recibirás tu estado de cuenta por correo electrónico. </font></br>" +
                                "<font color=#000000> Además, podrás consultarlo cuando quieras desde nuestra aplicación y pagina de internet</font>";
                       // ((TextView)popup.findViewById(R.id.pop_text)).setText(Html.fromHtml(text));
                       // LinearLayout llo=(LinearLayout)popup.findViewById(R.id.listo);
                        ImageView cerrar=(ImageView)popup.findViewById(R.id.btn_close);
                        cerrar.setClickable(true);
                        cerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();
                            }
                        });
                        ((TextView)popup.findViewById(R.id.pop_title)).setText(programa.getTitulo());
                        try {
                            //Mejor calculamos la categoria del programa por intervalo de canal
                            int canal =Integer.parseInt(programm.getCanal());
                            String categoria="";
                            if(canal<200&&canal>100)
                                categoria="TV Abierta";
                            else if(canal<300&&canal>200)
                                categoria="Entretenimiento";
                            else if(canal<400&&canal>300)
                                categoria="Infantil";
                            else if(canal<600&&canal>500)
                                categoria="Deportes";
                            else if(canal<700&&canal>600)
                                categoria="Peliculas";
                            else if(canal<800&&canal>700)
                                categoria="Información";
                            else if(canal<1000&&canal>900)
                                categoria="Alta definición";
                            ((TextView) popup.findViewById(R.id.pop_cat)).setText(categoria);
                            ((TextView) popup.findViewById(R.id.pop_cat)).setVisibility(TextView.VISIBLE);
                        }catch(Exception e){
                            ((TextView) popup.findViewById(R.id.pop_cat)).setVisibility(TextView.GONE);
                        }
                        ((TextView)popup.findViewById(R.id.pop_desc)).setText(programa.getDescripcion());
                        SimpleDateFormat sdd=new SimpleDateFormat("HH:mm");
                        Calendar cal=Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(programa.getHorario().getInicial()));
                        Calendar cal2=Calendar.getInstance();
                        cal2.setTimeInMillis(Long.parseLong(programa.getHorario().getEnd()));

                        String inicio=sdd.format(cal.getTime());
                        String fin=sdd.format(cal2.getTime());
                        ((TextView)popup.findViewById(R.id.pop_hra)).setText(inicio + " - "+  fin);
                    }
                });
                if(!(cl3.getTimeInMillis()<startdate)){
                    int ancho=Integer.parseInt(program.get(i).getDuracion())*anchoCelda/30;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) row.findViewById(ids[i]).getLayoutParams();
                    System.out.println("El ancho del canal "+getItem(position).getCanal()+" es "+ancho);
                    if(ancho==0){
                        ((LinearLayout) row.findViewById(ids[i])).setVisibility(LinearLayout.GONE);
                    }
                    else{
                        ((LinearLayout) row.findViewById(ids[i])).setVisibility(LinearLayout.VISIBLE);
                    }
                    params.width=ancho;

                }else if((cl3.getTimeInMillis()<startdate)){
                    int j=(int) ((cl.getTimeInMillis())-(startdate))/1000;
                    int minutos=j/60;
                    int ancho=minutos*anchoCelda/30;

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) row.findViewById(ids[i]).getLayoutParams();
                    params.width=ancho;
                    if(ancho==0){
                        ((LinearLayout) row.findViewById(ids[i])).setVisibility(LinearLayout.GONE);
                    }
                    else{
                        ((LinearLayout) row.findViewById(ids[i])).setVisibility(LinearLayout.VISIBLE);
                    }
                }
                if(i==35){
                    break;
                }
            }
            for(int j=contador;j<ids.length;j++){
                ((LinearLayout)row.findViewById(ids[j])).setVisibility(LinearLayout.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
        return row;
    }
}
