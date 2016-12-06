package org.mobilitychoices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.mobilitychoices.entities.Route;

import java.util.ArrayList;

public class RouteListViewAdapter extends ArrayAdapter<Route> {

    private Context context;
    private int resource;
    private ArrayList<Route> routes;

    public RouteListViewAdapter(Context context, int resource, int id, ArrayList<Route> routes) {
        super(context, resource, id, routes);
        this.context = context;
        this.resource = resource;
        this.routes = routes;
    }

    private static class ViewHolder {
        TextView time;
        TableRow row;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Route route = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.row = (TableRow) convertView.findViewById(R.id.tableRow);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.time.setText(route.getTime());

        for (int i = route.getModes().size() - 1; i >= 0; i--) {
            String s = route.getModes().get(i);
            ImageView imgView = new ImageView(getContext());
            imgView.setScaleType(ImageView.ScaleType.FIT_START);

            switch (s) {
                case "driving": {
                    imgView.setImageResource(R.drawable.ic_directions_car_black_36dp);
                    break;
                }
                case "bicycling": {
                    imgView.setImageResource(R.drawable.ic_directions_bike_black_36dp);
                    break;
                }
                case "walking": {
                    imgView.setImageResource(R.drawable.ic_directions_walk_black_36dp);
                    break;
                }
                case "transit": {
                    imgView.setImageResource(R.drawable.ic_directions_transit_black_36dp);
                    break;
                }
            }
            viewHolder.row.addView(imgView, 0);
        }

        return convertView;

    }
}
