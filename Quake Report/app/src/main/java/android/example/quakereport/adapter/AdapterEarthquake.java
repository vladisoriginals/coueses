package android.example.quakereport.adapter;

import android.content.Context;
import android.example.quakereport.data.Earthquake;
import android.example.quakereport.Listener;
import android.example.quakereport.R;
import android.example.quakereport.utils.QueryUtils;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterEarthquake extends RecyclerView.Adapter<AdapterEarthquake.HolderEarthquake> {

    private List<Earthquake> earthquakes;
    private Context context;
    private Listener listener;

   public AdapterEarthquake(List<Earthquake> earthquakes,Context context, Listener listener){
        this.earthquakes = earthquakes;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public HolderEarthquake onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_earthquake,parent,false);
        return new HolderEarthquake(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEarthquake holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        if (earthquakes == null) return 0;
        return earthquakes.size();
    }

    private Earthquake getItem(int position){
        return earthquakes.get(position);
    }

    class HolderEarthquake extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mag;
        TextView place;
        TextView locationOffset;
        TextView date;
        TextView time;
        HolderEarthquake(@NonNull View itemView) {
            super(itemView);
            mag = itemView.findViewById(R.id.tv_mag);
            place = itemView.findViewById(R.id.tv_place);
            locationOffset = itemView.findViewById(R.id.location_offset);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(this);
        }

        void bind(Earthquake earthquake){
            mag.setText(String.valueOf(earthquake.getMag()));
            GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
            int magnitudeColor = ContextCompat.getColor(context, QueryUtils.getMagnitudeColor(Double.parseDouble(earthquake.getMag())));
            magnitudeCircle.setColor(magnitudeColor);
            place.setText(earthquake.getPlace());
            locationOffset.setText(earthquake.getLocationOffset());
            date.setText(earthquake.getData());
            time.setText(earthquake.getTime());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onClick(earthquakes.get(position).getUrlDetails());
        }
    }

}
