package com.example.projectmonitoring.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.master.MasterDetailKaryawan;
import com.example.projectmonitoring.master.MasterTampilSemuaKaryawan;
import com.example.projectmonitoring.server.ServerKaryawan;
import com.example.projectmonitoring.server.ServerKendala;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TransaksiTampilSemuaKendala extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_kendala;
    ArrayList<HashMap<String, String>> list_data_kendala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_tampil_semua_kendala);

        list_kendala = (ListView) findViewById(R.id.list_kendala);

        requestQueue = Volley.newRequestQueue(TransaksiTampilSemuaKendala.this);

        list_data_kendala = new ArrayList<HashMap<String, String>>();

        tampilListKendala();
    }
    private void tampilListKendala() {
        stringRequest = new StringRequest(Request.Method.GET, ServerKendala.URL_TAMPIL_LIST_KENDALA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("kendala");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_kendala", json.getString("id_kendala"));
                        map.put("deskripsi_kendala", json.getString("deskripsi_kendala"));
                        map.put("deskripsi_solusi", json.getString("deskripsi_solusi"));
                        map.put("judul_proyek", json.getString("judul_proyek"));
                        list_data_kendala.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TransaksiTampilSemuaKendala.this, list_data_kendala, R.layout.list_semua_transaksi_kendala,
                        new String[]{"deskripsi_kendala", "deskripsi_solusi", "judul_proyek"},
                        new int[]{R.id.txt_kendala, R.id.txt_solusi, R.id.txt_judul_proyek});

                list_kendala.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiTampilSemuaKendala.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        list_kendala.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(TransaksiTampilSemuaKendala.this, TransaksiDetailKendala.class);
                intent.putExtra(ServerKendala.KENDALA_ID,map.get(ServerKendala.TAG_ID_KENDALA).toString());
                intent.putExtra(ServerKendala.TAG_JUDUL_PROYEK,map.get(ServerKendala.TAG_JUDUL_PROYEK).toString());
                intent.putExtra(ServerKendala.TAG_DESKRIPSI_KENDALA,map.get(ServerKendala.TAG_DESKRIPSI_KENDALA).toString());
                intent.putExtra(ServerKendala.TAG_DESKRIPSI_SOLUSI,map.get(ServerKendala.TAG_DESKRIPSI_SOLUSI).toString());
                startActivity(intent);
            }
        });
        requestQueue.add(stringRequest);
    }
}
