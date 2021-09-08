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
import com.example.projectmonitoring.master.MasterDetailClient;
import com.example.projectmonitoring.master.MasterTampilSemuaClient;
import com.example.projectmonitoring.server.ServerClient;
import com.example.projectmonitoring.server.ServerSPP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TransaksiListButuhBarang extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_barang;
    ArrayList<HashMap<String, String>> list_butuh_barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_list_butuh_barang);

        list_barang = (ListView) findViewById(R.id.list_butuh_barang);

        requestQueue = Volley.newRequestQueue(TransaksiListButuhBarang.this);

        list_butuh_barang = new ArrayList<HashMap<String, String>>();

        tampilListBarang();
    }

    private void tampilListBarang() {
        stringRequest = new StringRequest(Request.Method.GET, ServerSPP.URL_TAMPIL_LIST_BUTUH_BARANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("butuh");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_spp", json.getString("id_spp"));
                        map.put("id_barang", json.getString("id_barang"));
                        map.put("nama_barang", json.getString("nama_barang"));
                        map.put("harga_satuanbrg", json.getString("harga_satuanbrg"));
                        list_butuh_barang.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TransaksiListButuhBarang.this, list_butuh_barang, R.layout.list_butuh_barang,
                        new String[]{"nama_barang", "harga_satuanbrg"},
                        new int[]{R.id.txt_nama_barang, R.id.txt_harga_satuan_barang});

                list_barang.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiListButuhBarang.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
