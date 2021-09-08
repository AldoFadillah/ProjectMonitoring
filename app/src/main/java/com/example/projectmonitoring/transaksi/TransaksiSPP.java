package com.example.projectmonitoring.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.adapter.AdapterSpinnerBarang;
import com.example.projectmonitoring.adapter.AdapterSpinnerClient;
import com.example.projectmonitoring.adapter.AdapterSpinnerProyek;
import com.example.projectmonitoring.app.AppController;
import com.example.projectmonitoring.data.DataBarang;
import com.example.projectmonitoring.data.DataClient;
import com.example.projectmonitoring.data.DataProyek;
import com.example.projectmonitoring.server.ServerKaryawan;
import com.example.projectmonitoring.server.ServerSPP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TransaksiSPP extends AppCompatActivity implements View.OnClickListener {
    EditText editTextIDProyek, editTextnamaclient, editTextIdClient, editTextrincianpenawaran, editTexttgl_spp, editTextjudulproyek, editTextlokasiproyek, editTexthargapenawaran, editTextkeluhan, editTextIDBarang, editTextnamabarang, editTextjumlahsatuan, editTexthargasatuan, editTextjumlahbarang;
    Button buttonSimpanSPP, buttonSimpan, buttonTambahBarang, buttonListButuhBarang;
    Calendar myCalendar;
    ProgressDialog pDialog1;
    ProgressDialog pDialog2;
    ProgressDialog pDialog3;
    int hitungklik=0;
    int id_proyek;
    Spinner spn_nama_client, spn_id_proyek, spn_barang, spn_satuan_barang;
    AdapterSpinnerClient adapterSpinnerClient;
    AdapterSpinnerProyek adapterSpinnerProyek;
    AdapterSpinnerBarang adapterSpinnerBarang;
    DatePickerDialog.OnDateSetListener date;
    List<DataClient> listClient = new ArrayList<DataClient>();
    List<DataProyek> listID = new ArrayList<DataProyek>();
    List<DataBarang> listBarang = new ArrayList<DataBarang>();
    String id_spp;

    private static final String TAG = TransaksiSPP.class.getSimpleName();
    public static final String url = "https://tokoyr.com/projectmonitoring/tampil_semua_client.php";
    public static final String url_id_proyek = "https://tokoyr.com/projectmonitoring/tampil_id_max_proyek.php";
    public static final String url_barang = "https://tokoyr.com/projectmonitoring/tampil_semua_barang_spinner.php";

    public static final String TAG_ID_PROYEK = "id_proyek";
    public static final String TAG_ID_CLIENT = "id_client";
    public static final String TAG_NAMA_CLIENT = "nama_client";
    public static final String TAG_ID_BARANG = "id_barang";
    public static final String TAG_NAMA_BARANG = "nama_barang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_s_p_p);

        editTextnamaclient = (EditText) findViewById(R.id.editTextnamaclient);
        editTextrincianpenawaran = (EditText) findViewById(R.id.editTextrincianpenawaran);
        editTexttgl_spp = (EditText) findViewById(R.id.editTexttgl_spp);
        editTextIdClient = (EditText) findViewById(R.id.editTextIdClient);
        editTextjudulproyek = (EditText) findViewById(R.id.editTextjudulproyek);
        editTextlokasiproyek = (EditText) findViewById(R.id.editTextlokasiproyek);
        editTexthargapenawaran = (EditText) findViewById(R.id.editTexthargapenawaran);
        editTextkeluhan = (EditText) findViewById(R.id.editTextkeluhan);
        editTextIDBarang = (EditText) findViewById(R.id.editTextIDBarang);
        editTextnamabarang = (EditText) findViewById(R.id.editTextnamabarang);
        editTextjumlahsatuan = (EditText) findViewById(R.id.editTextjumlahsatuan);
        editTexthargasatuan = (EditText) findViewById(R.id.editTexthargasatuan);
        editTextjumlahbarang = (EditText) findViewById(R.id.editTextjumlahbarang);
        buttonSimpanSPP = (Button) findViewById(R.id.buttonSimpanSPP);
        buttonTambahBarang = (Button) findViewById(R.id.buttonTambahBarang);
        buttonListButuhBarang = (Button) findViewById(R.id.buttonListButuhBarang);
        buttonSimpan = (Button) findViewById(R.id.buttonSimpan);
        spn_nama_client = (Spinner) findViewById(R.id.spn_nama_client);
        editTextIDProyek = (EditText) findViewById(R.id.editTextIDProyek);
        spn_id_proyek = (Spinner) findViewById(R.id.spn_id_proyek);
        spn_barang = (Spinner) findViewById(R.id.spn_barang);
        spn_satuan_barang = (Spinner) findViewById(R.id.listSatuan);

        editTextnamabarang.setVisibility(View.GONE);
        spn_barang.setVisibility(View.GONE);
        editTextjumlahsatuan.setVisibility(View.GONE);
        editTexthargasatuan.setVisibility(View.GONE);
        editTextIDProyek.setVisibility(View.GONE);
        editTextjumlahbarang.setVisibility(View.GONE);
        buttonSimpanSPP.setVisibility(View.GONE);
        buttonTambahBarang.setVisibility(View.GONE);
        buttonListButuhBarang.setVisibility(View.GONE);
        spn_satuan_barang.setVisibility(View.GONE);
        editTextIDBarang.setVisibility(View.GONE);

        final EditText editTextjumlahsatuan = findViewById(R.id.editTextjumlahsatuan);

        spn_satuan_barang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //editTextjumlahsatuan.setText(List.getSelectedItem().toString());
                //txt_hasil.setText("Pendidikan Terakhir : " + listPendidikan.get(position).getPendidikan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        spn_nama_client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editTextnamaclient.setText(listClient.get(position).getNamaClient());
                editTextIdClient.setText(listClient.get(position).getIdClient());
                //txt_hasil.setText("Pendidikan Terakhir : " + listPendidikan.get(position).getPendidikan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapterSpinnerClient = new AdapterSpinnerClient(TransaksiSPP.this, listClient);
        spn_nama_client.setAdapter(adapterSpinnerClient);

        callData();

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTexttgl_spp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TransaksiSPP.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSimpan.setOnClickListener(TransaksiSPP.this);
        editTextIDProyek.setText("1");

        spn_id_proyek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_proyek = Integer.parseInt(listID.get(position).getIdProyek());
                id_proyek += 1;
                editTextIDProyek.setText(""+Integer.parseInt(String.valueOf(id_proyek)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapterSpinnerProyek = new AdapterSpinnerProyek(TransaksiSPP.this, listID);
        spn_id_proyek.setAdapter(adapterSpinnerProyek);

        callDataIDProyek();

        spn_barang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editTextnamabarang.setText(listBarang.get(position).getNamaBarang());
                editTextIDBarang.setText(listBarang.get(position).getIdBarang());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        adapterSpinnerBarang = new AdapterSpinnerBarang(TransaksiSPP.this, listBarang);
        spn_barang.setAdapter(adapterSpinnerBarang);

        callDataBarang();

        buttonTambahBarang.setOnClickListener(TransaksiSPP.this);
        buttonListButuhBarang.setOnClickListener(TransaksiSPP.this);
        buttonSimpanSPP.setOnClickListener(TransaksiSPP.this);
    }
    private void callDataBarang() {listBarang.clear();

        pDialog3 = new ProgressDialog(TransaksiSPP.this);
        pDialog3.setCancelable(false);
        pDialog3.setMessage("Loading...");
        showDialogss();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url_barang,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                DataBarang item = new DataBarang();

                                item.setIdBarang(obj.getString(TAG_ID_BARANG));
                                item.setNamaBarang(obj.getString(TAG_NAMA_BARANG));

                                listBarang.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterSpinnerBarang.notifyDataSetChanged();

                        hideDialogss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(TransaksiSPP.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialogss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
    private void showDialogss() {
        if (!pDialog3.isShowing())
            pDialog3.show();
    }
    private void hideDialogss() {
        if (pDialog3.isShowing())
            pDialog3.dismiss();
    }


    private void callDataIDProyek() {listID.clear();

        pDialog2 = new ProgressDialog(TransaksiSPP.this);
        pDialog2.setCancelable(false);
        pDialog2.setMessage("Loading...");
        showDialogs();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url_id_proyek,
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

                                listID.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterSpinnerProyek.notifyDataSetChanged();

                        hideDialogs();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(TransaksiSPP.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
    private void showDialogs() {
        if (!pDialog2.isShowing())
            pDialog2.show();
    }
    private void hideDialogs() {
        if (pDialog2.isShowing())
            pDialog2.dismiss();
    }

    private void callData() {
        listClient.clear();

        pDialog1 = new ProgressDialog(TransaksiSPP.this);
        pDialog1.setCancelable(false);
        pDialog1.setMessage("Loading...");
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

                                DataClient item = new DataClient();

                                item.setIdClient(obj.getString(TAG_ID_CLIENT));
                                item.setNamaClient(obj.getString(TAG_NAMA_CLIENT));

                                listClient.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterSpinnerClient.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(TransaksiSPP.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
    private void showDialog() {
        if (!pDialog1.isShowing())
            pDialog1.show();
    }
    private void hideDialog() {
        if (pDialog1.isShowing())
            pDialog1.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSimpan){
            int length = editTextrincianpenawaran.getText().length();
            int length2 = editTexttgl_spp.getText().length();
            int length3 = editTextjudulproyek.getText().length();
            int length4 = editTextlokasiproyek.getText().length();
            int length5 = editTexthargapenawaran.getText().length();
            int length6 = editTextkeluhan.getText().length();
            if(length == 0){
                Toast.makeText(getApplicationContext(), "Harap lengkapi form terlebih dahulu untuk lanjut!", Toast.LENGTH_LONG).show();
            }else{
                if(length2 == 0){
                    Toast.makeText(getApplicationContext(), "Harap lengkapi form terlebih dahulu untuk lanjut!", Toast.LENGTH_LONG).show();
                }else{
                    if(length3 == 0){
                        Toast.makeText(getApplicationContext(), "Harap lengkapi form terlebih dahulu untuk lanjut!", Toast.LENGTH_LONG).show();
                    }else{
                        if(length4 == 0){
                            Toast.makeText(getApplicationContext(), "Harap lengkapi form terlebih dahulu untuk lanjut!", Toast.LENGTH_LONG).show();
                        }else{
                            if(length5 == 0){
                                Toast.makeText(getApplicationContext(), "Harap lengkapi form terlebih dahulu untuk lanjut!", Toast.LENGTH_LONG).show();
                            }else{
                                if(length6 == 0){
                                    Toast.makeText(getApplicationContext(), "Harap lengkapi form terlebih dahulu untuk lanjut!", Toast.LENGTH_LONG).show();
                                }else{
                                    hitungklik +=1;
                                    AddProyek();
                                    editTextnamabarang.setVisibility(View.VISIBLE);
                                    editTextjumlahsatuan.setVisibility(View.VISIBLE);
                                    editTexthargasatuan.setVisibility(View.VISIBLE);
                                    editTextjumlahbarang.setVisibility(View.VISIBLE);
                                    editTextIDProyek.setVisibility(View.VISIBLE);

                                    spn_barang.setVisibility(View.VISIBLE);
                                    buttonSimpan.setVisibility(View.GONE);
                                    buttonTambahBarang.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (v == buttonTambahBarang){
            final String jml_barang = editTextjumlahbarang.getText().toString().trim();
            final String id_barang = editTextIDBarang.getText().toString().trim();
            final String id_proyek = editTextIDProyek.getText().toString().trim();
            final String harga_satuanbrg = editTexthargasatuan.getText().toString().trim();
            class SimpanButuhBarang extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            TransaksiSPP.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(TransaksiSPP.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(ServerSPP.KEY_HARGA_SATUAN_BARANG,harga_satuanbrg);
                    params.put(ServerSPP.KEY_JML_BARANG,jml_barang);
                    params.put(ServerSPP.KEY_ID_BARANG,id_barang);
                    params.put(ServerSPP.KEY_ID_PROYEK,id_proyek);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerSPP.URL_ADD_BUTUH_BARANG,params);
                }
            }
            new SimpanButuhBarang().execute();
            buttonListButuhBarang.setVisibility(View.VISIBLE);
            buttonSimpanSPP.setVisibility(View.VISIBLE);
        }
        if (v == buttonListButuhBarang){
            startActivity(new Intent(TransaksiSPP.this, TransaksiListButuhBarang.class));
            id_spp = getIntent().getStringExtra(ServerSPP.TAG_ID_PROYEK);
        }
        if (v == buttonSimpanSPP){
            final String rincian_penawaran = editTextrincianpenawaran.getText().toString().trim();
            final String tgl_penawaran = editTexttgl_spp.getText().toString().trim();
            final String judul_proyek = editTextjudulproyek.getText().toString().trim();
            final String hrg_penawaran = editTexthargapenawaran.getText().toString().trim();
            final String lokasi_proyek = editTextlokasiproyek.getText().toString().trim();
            final String keluhan = editTextkeluhan.getText().toString().trim();
            final String jml_satuan_barang = editTextjumlahsatuan.getText().toString().trim();
            final String nama_barang = editTextnamabarang.getText().toString().trim();
            final String id_proyek = editTextIDProyek.getText().toString().trim();
            final String id_client = editTextIdClient.getText().toString().trim();
            class SimpanSPP extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            TransaksiSPP.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(TransaksiSPP.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(ServerSPP.KEY_RINCIAN_PENAWARAN,rincian_penawaran);
                    params.put(ServerSPP.KEY_TGL_PENAWARAN,tgl_penawaran);
                    params.put(ServerSPP.KEY_JUDUL_PROYEK,judul_proyek);
                    params.put(ServerSPP.KEY_HRG_PENAWARAN,hrg_penawaran);
                    params.put(ServerSPP.KEY_LOKASI,lokasi_proyek);
                    params.put(ServerSPP.KEY_KELUHAN,keluhan);
                    params.put(ServerSPP.KEY_SATUAN_BARANG,jml_satuan_barang);
                    params.put(ServerSPP.KEY_NAMA_BARANG,nama_barang);
                    params.put(ServerSPP.KEY_ID_PROYEK,id_proyek);
                    params.put(ServerSPP.KEY_ID_CLIENT,id_client);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerSPP.URL_ADD_SPP,params);
                }
            }

            new SimpanSPP().execute();
        }
    }

    private void AddProyek() {
        class AddEmployee extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        TransaksiSPP.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TransaksiSPP.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerSPP.URL_ADD_PROYEK,params);
            }
        }
        new AddEmployee().execute();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTexttgl_spp.setText(sdf.format(myCalendar.getTime()));
    }
}
