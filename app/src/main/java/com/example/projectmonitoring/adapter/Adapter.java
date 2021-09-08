package com.example.projectmonitoring.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.data.DataModelKwitansi;

import java.util.List;

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModelKwitansi> item;

    public Adapter(Activity activity, List<DataModelKwitansi> item) {
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
            convertView = inflater.inflate(R.layout.list_item_kwitansi, null);

        TextView txt_nama_proyek = (TextView) convertView.findViewById(R.id.nama_proyek);
        TextView txt_nama_karyawan = (TextView) convertView.findViewById(R.id.txt_nama_client);
        TextView txt_tgl_mulai_proyek = (TextView) convertView.findViewById(R.id.txt_tgl_mulaiproyek);
        TextView txt_tgl_selesai_proyek = (TextView) convertView.findViewById(R.id.txt_tgl_selesaiproyek);

        DataModelKwitansi data;
        data = item.get(position);

        txt_nama_proyek.setText(data.getNamaProyek());
        txt_tgl_mulai_proyek.setText(data.getTanggalMulaiProyek());
        txt_tgl_selesai_proyek.setText(data.getTanggalSelesaiProyek());
        txt_nama_karyawan.setText(data.getNamaClient());

        return convertView;
    }
}