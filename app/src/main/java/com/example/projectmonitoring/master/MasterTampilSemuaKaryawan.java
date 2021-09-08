package com.example.projectmonitoring.master;

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
import com.example.projectmonitoring.server.ServerKaryawan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterTampilSemuaKaryawan extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_karyawan;
    ArrayList<HashMap<String, String>> list_data_karyawan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_tampil_semua_karyawan);

        list_karyawan = (ListView) findViewById(R.id.list_karyawan);

        requestQueue = Volley.newRequestQueue(MasterTampilSemuaKaryawan.this);

        list_data_karyawan = new ArrayList<HashMap<String, String>>();

        tampilListKaryawan();
    }

    private void tampilListKaryawan() {
        stringRequest = new StringRequest(Request.Method.GET, ServerKaryawan.URL_TAMPIL_LIST_KARYAWAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("karyawan");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_karyawan", json.getString("id_karyawan"));
                        map.put("name", json.getString("name"));
                        map.put("jabatan", json.getString("jabatan"));
                        map.put("notelp", json.getString("notelp"));
                        map.put("tgl_lhr", json.getString("tgl_lhr"));
                        map.put("tmp_lhr", json.getString("tmp_lhr"));
                        map.put("alamat", json.getString("alamat"));
                        list_data_karyawan.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        MasterTampilSemuaKaryawan.this, list_data_karyawan, R.layout.list_karyawan,
                        new String[]{"name", "jabatan", "notelp"},
                        new int[]{R.id.txt_nama_karyawan, R.id.txt_jabatan, R.id.txt_nohp_karyawan});

                list_karyawan.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MasterTampilSemuaKaryawan.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        list_karyawan.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MasterTampilSemuaKaryawan.this, MasterDetailKaryawan.class);
                intent.putExtra(ServerKaryawan.KARYAWAN_ID,map.get(ServerKaryawan.TAG_ID_KARYAWAN).toString());
                intent.putExtra(ServerKaryawan.TAG_NAME,map.get(ServerKaryawan.TAG_NAME).toString());
                intent.putExtra(ServerKaryawan.TAG_NOTELP,map.get(ServerKaryawan.TAG_NOTELP).toString());
                intent.putExtra(ServerKaryawan.TAG_TMP_LHR,map.get(ServerKaryawan.TAG_TMP_LHR).toString());
                intent.putExtra(ServerKaryawan.TAG_TGL_LHR,map.get(ServerKaryawan.TAG_TGL_LHR).toString());
                intent.putExtra(ServerKaryawan.TAG_JABATAN,map.get(ServerKaryawan.TAG_JABATAN).toString());
                intent.putExtra(ServerKaryawan.TAG_ALAMAT,map.get(ServerKaryawan.TAG_ALAMAT).toString());
                startActivity(intent);
            }
        });
        requestQueue.add(stringRequest);
    }
}
