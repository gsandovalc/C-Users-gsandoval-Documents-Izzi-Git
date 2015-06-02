package telecom.televisa.com.izzi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import televisa.telecom.com.util.ChannelListAdapter;
import televisa.telecom.com.util.GetGuide;
import televisa.telecom.com.util.HorizontalScrollViewExt;
import televisa.telecom.com.util.IzziRespondable;
import televisa.telecom.com.util.ScrollViewListener;
import televisa.telecom.com.util.Util;
import televisa.telecom.com.util.ViewListAdapter;
import televisa.telecom.com.util.izziGuideResponse;


public class TvGuideActivity extends Activity implements ScrollViewListener,IzziRespondable {
    public Date fechaInicial;
    public int anchoCelda;
    private ListView listViewLeft;
    private ListView listViewRight;
    private List<izziGuideResponse.TVStation> lineUp;
    boolean isLeftListEnabled = true;
    boolean isRightListEnabled = true;
    private SliderLayout mDemoSlider;
  /*
    Calendar current_hour;

    boolean isLeftListEnabled = true;
    boolean isRightListEnabled = true;
    public Date fechaInicial;
    List<ChannelTO> channelLs;
    LinearLayout ListCanales;
    List<Schedule> schedule;
    List<String> chnls;
    static List<List<Schedule>> listadelistas;
    Map<String,Integer> existe=new HashMap<String,Integer>();
    SharedPreferences settings;
    Map<String, Boolean> fav_map = new HashMap<String, Boolean>();
    int overscrolls=0;
    int leftScrollOverCount=0;
    int scrollOverCount=0;
    public static ScheduleActivity cntxt;
    public Date fechami=null;
    private boolean fromscroll=false;*/
    final IzziRespondable act=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_guide);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        ((TextView)findViewById(R.id.h_title)).setText("Guia de programación");
        Calendar cal=Calendar.getInstance();
        int minutos=cal.get(Calendar.MINUTE);
        if(minutos<30)
            minutos=0;
        else
            minutos=30;
        cal.set(Calendar.MINUTE,minutos);
        cal.set(Calendar.SECOND,0);
        fechaInicial=cal.getTime();
        new GetGuide(this,true).execute(new HashMap<String, String>());
        setTimeLine();
        initControls();
    }

    private void initControls(){
        listViewLeft=(ListView)findViewById(R.id.channel_list);
        listViewRight=(ListView)findViewById(R.id.table_programacion);
        HorizontalScrollViewExt hsv2=(HorizontalScrollViewExt)findViewById(R.id.hzScroll2);
        hsv2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }});
        HorizontalScrollViewExt hsv=(HorizontalScrollViewExt)findViewById(R.id.hzScroll);
        hsv.setScrollViewListener(this);
    }
    public void closeView(View v){
        this.finish();
    }

    @Override
    public void onScrollChanged(HorizontalScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        HorizontalScrollViewExt hsv=(HorizontalScrollViewExt)findViewById(R.id.hzScroll2);
        hsv.scrollTo(x, y);
    }
    private void setTimeLine(){
        LinearLayout tw=(LinearLayout)findViewById(R.id.fila_horas);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        Date fecha= (Date) fechaInicial.clone();
        anchoCelda= Util.dpToPx(this,150);
        for(int i=0;i<12;i++){

            TextView rowTextView = new TextView(this);
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.program_hour, tw, false);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) row.getLayoutParams();
            params.width=anchoCelda;
            row.setLayoutParams(params);
            rowTextView=(TextView)row.findViewById(R.id.horario_celda);
            rowTextView.setText(sdf.format(fecha));
            tw.addView(row);
            Calendar cl=Calendar.getInstance();
            cl.setTime(fecha);
            cl.add(Calendar.MINUTE, 30);
            fecha=cl.getTime();
        }
    }
    void setCanales(){
        ListView lv = (ListView)findViewById(R.id.channel_list);
        ChannelListAdapter channelAdapter=null;
        channelAdapter = new ChannelListAdapter(this, R.layout.channel_list_item, lineUp,0);
        lv.setAdapter(channelAdapter);
    }

    void setLineUp(){
        listViewRight.setAdapter(null);
        ListView tl=listViewRight;
        ViewListAdapter vla=new ViewListAdapter(this,R.layout.program_item,lineUp,this,0);
        vla.startdate=fechaInicial.getTime();
        tl.setAdapter(vla);
        listViewLeft.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        listViewRight.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        listViewLeft.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // onScroll will be called and there will be an infinite loop.
                // That's why i set a boolean value
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isRightListEnabled = false;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isRightListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isLeftListEnabled) {
                    listViewRight.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });

        listViewRight.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLeftListEnabled = false;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isLeftListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isRightListEnabled) {
                    listViewLeft.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });
    }
    @Override
    public void notifyChanges(Object response) {
        if(response==null) {
            new AlertDialog.Builder(this)
                    .setTitle("izzi")
                    .setMessage("Ocurrio un error al cargar la información.\n ¿Deseas intentarlo de nuevo?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            new GetGuide(act,true).execute(new HashMap<String, String>());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;

        }
        lineUp=((izziGuideResponse)response).getResponse().getLineUp();
        List<String> bann=((izziGuideResponse)response).getResponse().getBanners();
        Map<String,String> mapa=new HashMap<>();
        int contador=0;
        for(int i=0;i<bann.size();i++){
             mapa.put(i+"",bann.get(i));
        }
        for(String name : mapa.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                     .image(mapa.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setVisibility(SliderLayout.VISIBLE);
        setCanales();
        setLineUp();
    }

    public void filter(View v){
        int filter=0;
        switch(v.getId()){
            case R.id.todos:
filter=0;
                break;
            case R.id.deportes:
filter=1;
                break;
            case R.id.entretenimiento:
        filter=2;
                break;
            case R.id.ninos:
filter=3;
                break;
        }
        ListView lv = (ListView)findViewById(R.id.channel_list);

        lv.setAdapter(null);
        ChannelListAdapter channelAdapter=null;
        channelAdapter = new ChannelListAdapter(this, R.layout.channel_list_item, lineUp,filter);
        lv.setAdapter(channelAdapter);

        ListView tl=listViewRight;
        tl.setAdapter(null);
        ViewListAdapter vla=new ViewListAdapter(this,R.layout.program_item,lineUp,this,filter);
        vla.startdate=fechaInicial.getTime();
        tl.setAdapter(vla);

    }
}
