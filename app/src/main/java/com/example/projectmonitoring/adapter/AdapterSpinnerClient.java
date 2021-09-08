package com.example.projectmonitoring.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.data.DataClient;

import java.util.List;

public class AdapterSpinnerClient extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataClient> item;

    public AdapterSpinnerClient(Activity activity, List<DataClient> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_client_spinner, null);

        TextView nama_client = (TextView) convertView.findViewById(R.id.nama_client);

        DataClient data;
        data = item.get(position);

        nama_client.setText(data.getNamaClient());

        return convertView;
    }
}
