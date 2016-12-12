package org.mobilitychoices.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mobilitychoices.R;
import org.mobilitychoices.entities.Track;

import java.util.ArrayList;

public class AllRoutesListViewAdapter extends ArrayAdapter<Track> {

    private Context context;
    private int resource;
    private ArrayList<Track> tracks;

    public AllRoutesListViewAdapter(Context context, int resource, int id, ArrayList<Track> tracks) {
        super(context, resource, id, tracks);
        this.context = context;
        this.resource = resource;
        this.tracks = tracks;
    }

    private static class ViewHolder {
        TextView time;
        LinearLayout fromTo;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Track track = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.fromTo = (LinearLayout) convertView.findViewById(R.id.linearLayoutIcons);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TextView textViewStart = new TextView(getContext());
        textViewStart.setText(track.getStart().getAddress());
        TextView textViewEnd = new TextView(getContext());
        textViewStart.setText(track.getEnd().getAddress());

        viewHolder.fromTo.addView(textViewStart);
        viewHolder.fromTo.addView(textViewEnd);
        viewHolder.time.setText(track.getTime());

        return convertView;

    }
}
