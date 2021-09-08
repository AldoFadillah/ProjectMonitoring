package com.example.projectmonitoring.master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmonitoring.ListProyek;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.server.ServerBarang;
import com.example.projectmonitoring.server.ServerSPP;
import com.example.projectmonitoring.transaksi.TransaksiSPP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterBarang extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    int quantity = 0;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    ArrayList<HashMap<String, String>> list_data;
    private EditText editTextnamabarang;
    SwipeRefreshLayout swipelayout;
    private TextView txt_jml_barang;
    private Button btambahbarang,bsimpanbarang;
    private ListView listmasterbarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_barang);

        //mencari nilai dari id xml
        editTextnamabarang   = (EditText) findViewById(R.id.editTextnamabarang);
        txt_jml_barang   = (TextView) findViewById(R.id.txt_jml_barang);
        listmasterbarang = (ListView) findViewById(R.id.listmasterbarang);
        swipelayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);

        btambahbarang = (Button) findViewById(R.id.btambahbarang);
        bsimpanbarang  = (Button) findViewById(R.id.bsimpanbarang);

        btambahbarang.setOnClickListener(MasterBarang.this);
        bsimpanbarang.setOnClickListener(MasterBarang.this);
        bsimpanbarang.setText("Simpan");
        btambahbarang.setText("Tambah");


        requestQueue = Volley.newRequestQueue(MasterBarang.this);
        list_data = new ArrayList<HashMap<String, String>>();
        swipelayout.setOnRefreshListener(this);

        swipelayout.post(new Runnable() {
                       @Override
                       public void run() {
                           swipelayout.setRefreshing(true);
                           callData();
                       }
                   }
        );
    }

    private void callData() {
        list_data.clear();
        swipelayout.setRefreshing(true);
        String url_tampil_semua = ServerBarang.URL_TAMPIL_SEMUA_BARANG;
        stringRequest = new StringRequest(Request.Method.POST, url_tampil_semua, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("barang");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_barang", json.getString("id_barang"));
                        map.put("nama_barang", json.getString("nama_barang"));
                        map.put("jml_barang", json.getString("jml_barang"));
                        list_data.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        MasterBarang.this, list_data, R.layout.list_item_master_barang,
                        new String[]{"id_barang", "nama_barang", "jml_barang"},
                        new int[]{R.id.id_barang, R.id.namabarang, R.id.displayjumlah});

                listmasterbarang.setAdapter(adapter);
                swipelayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MasterBarang.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        listmasterbarang.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MasterBarang.this, MasterDetailBarang.class);
                intent.putExtra(ServerBarang.BARANG_ID,map.get(ServerBarang.TAG_ID_BARANG).toString());
                intent.putExtra(ServerBarang.TAG_NAMA_BARANG,map.get(ServerBarang.TAG_NAMA_BARANG).toString());
                intent.putExtra(ServerBarang.TAG_JML_BARANG,map.get(ServerBarang.TAG_JML_BARANG).toString());
                startActivity(intent);
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == bsimpanbarang){
            final String nama_barang = editTextnamabarang.getText().toString().trim();
            final String jml_barang = txt_jml_barang.getText().toString().trim();
            class TambahBarang extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            MasterBarang.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    reset();
                    Toast.makeText(MasterBarang.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ServerBarang.KEY_NAMA_BARANG, nama_barang);
                    params.put(ServerBarang.KEY_JML_BARANG, jml_barang);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerBarang.URL_ADD_BARANG, params);
                }

            }
            new TambahBarang().execute();
            btambahbarang.setText("Reset");
        }
        if (v == btambahbarang){
            btambahbarang.setText("Tambah");
            quantity += 1;
            display(quantity);
        }
    }
    private void display(int number) {
        TextView quantity = (TextView) findViewById(R.id.txt_jml_barang);
        quantity.setText("" + number);
    }

    private void reset(){
        editTextnamabarang.setText("");
        quantity = 0;
    }

    @Override
    public void onRefresh() {
        callData();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MasterBarang.this, Master.class));
        finishAffinity();
    }
}
