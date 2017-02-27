package edu.svsu.tacops.tacops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tacops.Client;

import java.util.ArrayList;

/**
 * Created by Erik on 2/25/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<Client> listData; // An array of client objects
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<Client> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.playerNameView = (TextView) convertView.findViewById(R.id.player);
            holder.teamNameView = (TextView) convertView.findViewById(R.id.team);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.playerNameView.setText(listData.get(position).getClientName());
        holder.teamNameView.setText(listData.get(position).getTeam());
        return convertView;
    }

    // The views to be grouped together
    static class ViewHolder {
        TextView playerNameView;
        TextView teamNameView;
    }
}
