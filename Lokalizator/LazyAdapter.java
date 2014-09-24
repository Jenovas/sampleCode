package projekt.lokalizacja;

import java.util.ArrayList;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<TreeMap<String, String>> data;
    private static LayoutInflater inflater=null; 
    
    public LazyAdapter(Activity a, ArrayList<TreeMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
    	return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);

        TextView id=(TextView)vi.findViewById(R.id.id);
        TextView text=(TextView)vi.findViewById(R.id.text);
        ImageLoader imageLoader = new ImageLoader(activity.getApplicationContext());
        
        TreeMap<String, String> map = new TreeMap<String, String>();
        map = data.get(position);

        ImageView img = (ImageView) vi.findViewById(R.id.image);
        
        String url = "https://chart.googleapis.com/chart?chst=d_simple_text_icon_below&chld=|0|000|"+map.get("Icon")+"|24|fff|888";
        String new_url = url.replaceAll("#", "");
        System.out.println(new_url);
		imageLoader.DisplayImage(new_url, img);
        
        float away = Float.parseFloat(map.get("Distance").toString());
        String.format("%.2f", away);
        id.setText(map.get("Address"));
        text.setText(String.format("%.2f", away)+" km");
        
        // Set background  
        vi.setBackgroundColor(position % 2 == 0 ? Color.WHITE : Color.rgb(238, 238, 238));
       
        return vi;
    }
}