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
import com.example.projectmonitoring.server.ServerBarang;
import com.example.projectmonitoring.server.ServerClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterTampilSemuaClient extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_client;
    ArrayList<HashMap<String, String>> list_data_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_tampil_semua_client);

        list_client = (ListView) findViewById(R.id.list_client);

        requestQueue = Volley.newRequestQueue(MasterTampilSemuaClient.this);

        list_data_client = new ArrayList<HashMap<String, String>>();

        tampilListClient();
    }

    private void tampilListClient() {
        stringRequest = new StringRequest(Request.Method.GET, ServerClient.URL_TAMPIL_LIST_CLIENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("client");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_client", json.getString("id_client"));
                        map.put("nama_client", json.getString("nama_client"));
                        map.put("nohp_client", json.getString("nohp_client"));
                        list_data_client.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        MasterTampilSemuaClient.this, list_data_client, R.layout.list_client,
                        new String[]{"nama_client", "nohp_client"},
                        new int[]{R.id.txt_nama_client, R.id.txt_nohp_client});

                list_client.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MasterTampilSemuaClient.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        list_client.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MasterTampilSemuaClient.this, MasterDetailClient.class);
                intent.putExtra(ServerClient.CLIENT_ID,map.get(ServerClient.TAG_ID_CLIENT).toString());
                intent.putExtra(ServerClient.TAG_NAMA_CLIENT,map.get(ServerClient.TAG_NAMA_CLIENT).toString());
                intent.putExtra(ServerClient.TAG_NOHP_CLIENT,map.get(ServerClient.TAG_NOHP_CLIENT).toString());
                startActivity(intent);
            }
        });
        requestQueue.add(stringRequest);
    }
}
