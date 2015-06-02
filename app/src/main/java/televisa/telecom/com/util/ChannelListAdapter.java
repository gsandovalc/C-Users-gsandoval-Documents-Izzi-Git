package televisa.telecom.com.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import telecom.televisa.com.izzi.R;

/**
 * Created by cevelez on 11/05/2015.
 */
public class ChannelListAdapter extends ArrayAdapter<izziGuideResponse.TVStation> {
    private List<izziGuideResponse.TVStation> lineUp;
    ImageLoader loader;
    Context context;
    int filter=0;
    public ChannelListAdapter(Context context, int textViewResourceId, List<izziGuideResponse.TVStation> objects, int filter) {
        super(context, textViewResourceId, objects);
        this.lineUp = objects;
        loader =new ImageLoader(context);
        this.context=context;
        this.filter=filter;
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
            lineUp=objectsAux;
        }
    }
    @Override
    public int getCount() {
        try {
            return lineUp.size();
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public izziGuideResponse.TVStation getItem(int index) {
        try {
            return lineUp.get(index);
        }catch (Exception e){
            return null;
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ListView lv=(ListView) parent;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.channel_list_item, parent, false);
        }

        final izziGuideResponse.TVStation channel = getItem(position);
        String canal =channel.getCanal()+"";
        ((TextView)row.findViewById(R.id.channel_number)).setText(channel.getCanal() + "");
        ImageView image = (ImageView)row.findViewById(R.id.channel_icon);
        ((TextView)row.findViewById(R.id.channel_number)).setVisibility(TextView.VISIBLE);
        loader.DisplayImage("https://www.izzi.mx"+channel.getThumb(), image);

        return row;
    }

}
