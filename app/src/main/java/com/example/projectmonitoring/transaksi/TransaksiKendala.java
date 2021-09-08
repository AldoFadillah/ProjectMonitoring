package com.example.projectmonitoring.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.adapter.AdapterSpinnerProyek;
import com.example.projectmonitoring.app.AppController;
import com.example.projectmonitoring.data.DataProyek;
import com.example.projectmonitoring.master.MasterKaryawan;
import com.example.projectmonitoring.master.MasterTampilSemuaKaryawan;
import com.example.projectmonitoring.server.ServerKendala;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransaksiKendala extends AppCompatActivity implements View.OnClickListener{
    EditText editTextIdProyek, editTextNamaProyek, editTextKendala, editTextSolusi;
    Button buttonSimpanKendala, buttonLihatListKendala;
    Spinner spn_nama_proyek;
    ProgressDialog pDialog;
    AdapterSpinnerProyek adapterSpinnerProyek;
    List<DataProyek> listProyek = new ArrayList<DataProyek>();

    public static final String url = "https://tokoyr.com/projectmonitoring/tampil_semua_proyek.php";
    private static final String TAG = TransaksiKendala.class.getSimpleName();

    public static final String TAG_ID_PROYEK = "id_proyek";
    public static final String TAG_NAMA_PROYEK = "judul_proyek";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_kendala);

        editTextIdProyek = (EditText) findViewById(R.id.editTextIdProyek);
        editTextNamaProyek = (EditText) findViewById(R.id.editTextNamaProyek);
        editTextKendala = (EditText) findViewById(R.id.editTextKendala);
        editTextSolusi = (EditText) findViewById(R.id.editTextSolusi);
        buttonSimpanKendala = (Button) findViewById(R.id.buttonSimpanKendala);
        buttonLihatListKendala = (Button) findViewById(R.id.buttonLihatListKendala);
        spn_nama_proyek = (Spinner) findViewById(R.id.spn_nama_proyek);

        spn_nama_proyek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editTextIdProyek.setText(listProyek.get(position).getIdProyek());
                editTextNamaProyek.setText(listProyek.get(position).getNamaProyek());
                //txt_hasil.setText("Pendidikan Terakhir : " + listPendidikan.get(position).getPendidikan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapterSpinnerProyek = new AdapterSpinnerProyek(TransaksiKendala.this, listProyek);
        spn_nama_proyek.setAdapter(adapterSpinnerProyek);

        callData();

        buttonSimpanKendala.setOnClickListener(TransaksiKendala.this);
        buttonLihatListKendala.setOnClickListener(TransaksiKendala.this);
    }

    private void callData() {
        listProyek.clear();

        pDialog = new ProgressDialog(TransaksiKendala.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                DataProyek item = new DataProyek();

                                item.setIdProyek(obj.getString(TAG_ID_PROYEK));
                                item.setNamaProyek(obj.getString(TAG_NAMA_PROYEK));

                                listProyek.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterSpinnerProyek.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(TransaksiKendala.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSimpanKendala){
            final String id_proyek = editTextIdProyek.getText().toString().trim();
            final String deskripsi_kendala = editTextKendala.getText().toString().trim();
            final String deskripsi_solusi = editTextSolusi.getText().toString().trim();
            class SimpanKendala extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            TransaksiKendala.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(TransaksiKendala.this, s, Toast.LENGTH_LONG).show();
                    reset();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(ServerKendala.KEY_ID_PROYEK,id_proyek);
                    params.put(ServerKendala.KEY_KENDALA,deskripsi_kendala);
                    params.put(ServerKendala.KEY_SOLUSI,deskripsi_solusi);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerKendala.URL_ADD_KENDALA,params);
                }
            }

            new SimpanKendala().execute();
        }
        if (v == buttonLihatListKendala){
            startActivity(new Intent(TransaksiKendala.this, TransaksiTampilSemuaKendala.class));
        }
    }
    private void reset(){
        editTextIdProyek.setText("");
        editTextNamaProyek.setText("");
        editTextKendala.setText("");
        editTextSolusi.setText("");
    }
}
