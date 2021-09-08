package com.example.projectmonitoring;

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
import com.example.projectmonitoring.server.ServerProyek;
import com.example.projectmonitoring.server.ServerSPP;
import com.example.projectmonitoring.transaksi.TransaksiProyek;
import com.example.projectmonitoring.transaksi.TransaksiSPP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListProyek extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_proyek;
    ArrayList<HashMap<String, String>> list_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_proyek);

        list_proyek = (ListView) findViewById(R.id.list_proyek);

        Toast.makeText(getApplicationContext(), "Silahkan pilih proyek...",
                Toast.LENGTH_LONG).show();

        requestQueue = Volley.newRequestQueue(ListProyek.this);

        list_data = new ArrayList<HashMap<String, String>>();

        tampilListProyek();
    }

    private void tampilListProyek() {
        stringRequest = new StringRequest(Request.Method.GET, ServerSPP.URL_TAMPIL_LIST_PROYEK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("suratpermintaanpenawaran");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_proyek", json.getString("id_proyek"));
                        map.put("id_spp", json.getString("id_spp"));
                        map.put("id_client", json.getString("id_client"));
                        map.put("judul_proyek", json.getString("judul_proyek"));
                        map.put("tgl_mulaiproyek", json.getString("tgl_mulaiproyek"));
                        map.put("tgl_selesaiproyek", json.getString("tgl_selesaiproyek"));
                        map.put("desk_pekerjaan", json.getString("desk_pekerjaan"));
                        map.put("id_karyawan", json.getString("id_karyawan"));
                        map.put("nama_client", json.getString("nama_client"));
                        map.put("tgl_penawaran", json.getString("tgl_penawaran"));
                        list_data.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        ListProyek.this, list_data, R.layout.list_proyek,
                        new String[]{"id_proyek", "judul_proyek"},
                        new int[]{R.id.txt_id_proyek, R.id.txt_judul_proyek});

                list_proyek.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListProyek.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        list_proyek.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(ListProyek.this, TransaksiProyek.class);
                intent.putExtra(ServerSPP.PROYEK_ID,map.get(ServerSPP.TAG_ID_PROYEK).toString());
                intent.putExtra(ServerSPP.TAG_TGL_PENAWARAN,map.get(ServerSPP.TAG_TGL_PENAWARAN).toString());
                intent.putExtra(ServerSPP.TAG_NAMA_CLIENT,map.get(ServerSPP.TAG_NAMA_CLIENT).toString());
                intent.putExtra(ServerProyek.TAG_TGL_MULAI,map.get(ServerProyek.TAG_TGL_MULAI).toString());
                intent.putExtra(ServerProyek.TAG_TGL_SELESAI,map.get(ServerProyek.TAG_TGL_SELESAI).toString());
                intent.putExtra(ServerProyek.TAG_DESK_PEKERJAAN,map.get(ServerProyek.TAG_DESK_PEKERJAAN).toString());
                intent.putExtra(ServerProyek.TAG_ID_KARYAWAN,map.get(ServerProyek.TAG_ID_KARYAWAN).toString());
                startActivity(intent);
            }
        });
        requestQueue.add(stringRequest);
    }

}
