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
import com.example.projectmonitoring.adapter.AdapterSpinnerBeliBarang;
import com.example.projectmonitoring.adapter.AdapterSpinnerClient;
import com.example.projectmonitoring.adapter.AdapterSpinnerKaryawan;
import com.example.projectmonitoring.app.AppController;
import com.example.projectmonitoring.data.DataBarang;
import com.example.projectmonitoring.data.DataClient;
import com.example.projectmonitoring.data.DataKaryawan;
import com.example.projectmonitoring.master.MasterDetailBarang;
import com.example.projectmonitoring.server.ServerBarang;
import com.example.projectmonitoring.server.ServerKaryawan;
import com.example.projectmonitoring.server.ServerProyek;
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

public class TransaksiProyek extends AppCompatActivity implements View.OnClickListener{
    EditText editTextNamaClient, editTextNamaKaryawan, editTextTglPenawaran, editTextIDKaryawan, editTextIDProyek, editTextTglMulaiProyek, editTextTglSelesaiProyek, editTextDeskPekerjaan, editTextNamaBarang, editTextHargaSatuan, editTextJumlahBeli, editTextHargaBeliBarang, editTextIDBarang;
    Button buttonSimpanProyek, buttonSimpanLanjut, buttonHitungHargaBeli, buttonListBeliBarang;
    DatePickerDialog.OnDateSetListener date1, date2;
    Calendar myCalendar1, myCalendar2;
    ProgressDialog pDialog;
    ProgressDialog pDialog2;
    int hitungharga;
    Spinner spn_nama_karyawan, spn_barang;
    String id_proyek, id_spp, tgl_penawaran, nama_client, tgl_mulaiproyek, tgl_selesaiproyek, desk_pekerjaan, id_karyawan;
    AdapterSpinnerKaryawan adapterSpinnerKaryawan;
    AdapterSpinnerBeliBarang adapterSpinnerBarang;
    List<DataBarang> listBarang = new ArrayList<DataBarang>();
    List<DataKaryawan> listKaryawan = new ArrayList<DataKaryawan>();

    public static final String url = "https://tokoyr.com/projectmonitoring/tampil_semua_karyawan_teknisi.php";
    public final String url_barang = "https://tokoyr.com/projectmonitoring/tampil_semua_barang_spinner_beli.php";

    private static final String TAG = TransaksiSPP.class.getSimpleName();

    public static final String TAG_ID_KARYAWAN = "id_karyawan";
    public static final String TAG_NAMA_KARYAWAN = "name";
    public static final String TAG_ID_BARANG = "id_barang";
    public static final String TAG_NAMA_BARANG = "nama_barang";
    public static final String TAG_HARGA_SATUAN_BARANG = "harga_satuanbrg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_proyek);

        editTextNamaKaryawan = (EditText) findViewById(R.id.editTextNamaKaryawan);
        editTextIDKaryawan = (EditText) findViewById(R.id.editTextIDKaryawan);
        editTextTglMulaiProyek = (EditText) findViewById(R.id.editTextTglMulaiProyek);
        editTextTglSelesaiProyek = (EditText) findViewById(R.id.editTextTglSelesaiProyek);
        editTextDeskPekerjaan = (EditText) findViewById(R.id.editTextDeskPekerjaan);
        editTextJumlahBeli = (EditText) findViewById(R.id.editTextJumlahBeli);
        editTextHargaSatuan = (EditText) findViewById(R.id.editTextHargaSatuan);
        editTextHargaBeliBarang = (EditText) findViewById(R.id.editTextHargaBeliBarang);
        editTextIDBarang = (EditText) findViewById(R.id.editTextIDBarang);
        editTextIDProyek = (EditText) findViewById(R.id.editTextIDProyek);
        editTextNamaBarang = (EditText) findViewById(R.id.editTextNamaBarang);
        buttonSimpanProyek = (Button) findViewById(R.id.buttonSimpanProyek);
        buttonSimpanLanjut = (Button) findViewById(R.id.buttonSimpanLanjut);
        buttonListBeliBarang = (Button) findViewById(R.id.buttonListBeliBarang);
        buttonHitungHargaBeli = (Button) findViewById(R.id.buttonHitungHargaBeli);
        spn_nama_karyawan = (Spinner) findViewById(R.id.spn_nama_karyawan);
        spn_barang = (Spinner) findViewById(R.id.spn_barang);

        editTextTglPenawaran = (EditText) findViewById(R.id.editTextTglPenawaran);
        editTextNamaClient = (EditText) findViewById(R.id.editTextNamaClient);

        editTextNamaBarang.setVisibility(View.GONE);
        editTextJumlahBeli.setVisibility(View.GONE);
        editTextHargaSatuan.setVisibility(View.GONE);
        editTextHargaBeliBarang.setVisibility(View.GONE);
        buttonSimpanProyek.setVisibility(View.GONE);
        buttonListBeliBarang.setVisibility(View.GONE);
        spn_barang.setVisibility(View.GONE);
        editTextIDBarang.setVisibility(View.GONE);
        buttonHitungHargaBeli.setVisibility(View.GONE);

        tgl_mulaiproyek = getIntent().getStringExtra(ServerProyek.TAG_TGL_MULAI);
        if(tgl_mulaiproyek.equals("null")){
            editTextTglMulaiProyek.setText("");
        }else{
            editTextTglMulaiProyek.setText(tgl_mulaiproyek);
        }
        tgl_selesaiproyek = getIntent().getStringExtra(ServerProyek.TAG_TGL_SELESAI);
        if(tgl_selesaiproyek.equals("null")){
            editTextTglSelesaiProyek.setText("");
        }else{
            editTextTglSelesaiProyek.setText(tgl_selesaiproyek);
        }
        desk_pekerjaan = getIntent().getStringExtra(ServerProyek.TAG_DESK_PEKERJAAN);
        if(desk_pekerjaan.equals("null")){
            editTextDeskPekerjaan.setText("");
        }else {
            editTextDeskPekerjaan.setText(desk_pekerjaan);
        }
        id_karyawan = getIntent().getStringExtra(ServerProyek.TAG_ID_KARYAWAN);
        editTextIDKaryawan.setText(id_karyawan);
        id_proyek = getIntent().getStringExtra(ServerSPP.PROYEK_ID);
        editTextIDProyek.setText(id_proyek);
        id_spp = getIntent().getStringExtra(ServerSPP.PROYEK_ID);
        tgl_penawaran = getIntent().getStringExtra(ServerSPP.TAG_TGL_PENAWARAN);
        editTextTglPenawaran.setText(tgl_penawaran);
        nama_client = getIntent().getStringExtra(ServerSPP.TAG_NAMA_CLIENT);
        editTextNamaClient.setText(nama_client);

        spn_nama_karyawan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editTextNamaKaryawan.setText(listKaryawan.get(position).getNamaKaryawan());
                editTextIDKaryawan.setText(listKaryawan.get(position).getIdKaryawan());
                //txt_hasil.setText("Pendidikan Terakhir : " + listPendidikan.get(position).getPendidikan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapterSpinnerKaryawan = new AdapterSpinnerKaryawan(TransaksiProyek.this, listKaryawan);
        spn_nama_karyawan.setAdapter(adapterSpinnerKaryawan);

        callData();

        spn_barang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                editTextNamaBarang.setText(listBarang.get(position).getNamaBarang());
                editTextHargaSatuan.setText(listBarang.get(position).getHargaSatuanBrg());
                editTextIDBarang.setText(listBarang.get(position).getIdBarang());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        adapterSpinnerBarang = new AdapterSpinnerBeliBarang(TransaksiProyek.this, listBarang);
        spn_barang.setAdapter(adapterSpinnerBarang);

        callDataBarang();

        myCalendar1 = Calendar.getInstance();
        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelTanggalMulai();
            }
        };

        editTextTglMulaiProyek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TransaksiProyek.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalendar2 = Calendar.getInstance();
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelTanggalSelesai();
            }
        };

        editTextTglSelesaiProyek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TransaksiProyek.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSimpanLanjut.setOnClickListener(TransaksiProyek.this);
        buttonHitungHargaBeli.setOnClickListener(TransaksiProyek.this);
        buttonSimpanProyek.setOnClickListener(TransaksiProyek.this);
        buttonListBeliBarang.setOnClickListener(TransaksiProyek.this);
    }
    private void callDataBarang() {listBarang.clear();
        pDialog2 = new ProgressDialog(TransaksiProyek.this);
        pDialog2.setCancelable(false);
        pDialog2.setMessage("Loading...");
        showDialogs();

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
                                item.setHargaSatuanBrg(obj.getString(TAG_HARGA_SATUAN_BARANG));

                                listBarang.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterSpinnerBarang.notifyDataSetChanged();

                        hideDialogs();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(TransaksiProyek.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialogs();
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
        listKaryawan.clear();

        pDialog = new ProgressDialog(TransaksiProyek.this);
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

                                DataKaryawan item = new DataKaryawan();

                                item.setIdKaryawan(obj.getString(TAG_ID_KARYAWAN));
                                item.setNamaKaryawan(obj.getString(TAG_NAMA_KARYAWAN));

                                listKaryawan.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapterSpinnerKaryawan.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(TransaksiProyek.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void updateLabelTanggalMulai() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTglMulaiProyek.setText(sdf.format(myCalendar1.getTime()));
    }

    private void updateLabelTanggalSelesai() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTglSelesaiProyek.setText(sdf.format(myCalendar2.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSimpanLanjut){
            final String tanggal_mulai_proyek = editTextTglMulaiProyek.getText().toString().trim();
            final String tanggal_selesai_proyek = editTextTglSelesaiProyek.getText().toString().trim();
            final String deskripsi_pekerjaan = editTextDeskPekerjaan.getText().toString().trim();
            final String id_karyawan = editTextIDKaryawan.getText().toString().trim();
            class ubahData extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            TransaksiProyek.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(TransaksiProyek.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ServerProyek.KEY_ID_PROYEK, id_proyek);
                    params.put(ServerProyek.KEY_ID_KARYAWAN, id_karyawan);
                    params.put(ServerProyek.KEY_TGL_MULAI, tanggal_mulai_proyek);
                    params.put(ServerProyek.KEY_TGL_SELESAI, tanggal_selesai_proyek);
                    params.put(ServerProyek.KEY_DESK_PEKERJAAN, deskripsi_pekerjaan);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerProyek.URL_UPDATE_PROYEK, params);
                }

            }
            new ubahData().execute();
            editTextNamaBarang.setVisibility(View.VISIBLE);
            editTextJumlahBeli.setVisibility(View.VISIBLE);
            editTextHargaSatuan.setVisibility(View.VISIBLE);
            editTextHargaBeliBarang.setVisibility(View.VISIBLE);
            buttonSimpanLanjut.setVisibility(View.GONE);
            buttonHitungHargaBeli.setVisibility(View.VISIBLE);
            spn_barang.setVisibility(View.VISIBLE);
        }
        if (v == buttonHitungHargaBeli){
            hitungHargaTotal();
            buttonSimpanProyek.setVisibility(View.VISIBLE);
            simpanBeliBarang();
        }
        if (v == buttonListBeliBarang){
            startActivity(new Intent(TransaksiProyek.this, TransaksiListBeliBarang.class));
            id_proyek = getIntent().getStringExtra(ServerProyek.TAG_ID_PROYEK);
        }
        if (v == buttonSimpanProyek) {
            final String tanggal_mulai_proyek = editTextTglMulaiProyek.getText().toString().trim();
            final String tanggal_selesai_proyek = editTextTglSelesaiProyek.getText().toString().trim();
            final String deskripsi_pekerjaan = editTextDeskPekerjaan.getText().toString().trim();
            final String id_karyawan = editTextIDKaryawan.getText().toString().trim();
            class ubahData extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            TransaksiProyek.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(TransaksiProyek.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ServerProyek.KEY_ID_PROYEK, id_proyek);
                    params.put(ServerProyek.KEY_ID_KARYAWAN, id_karyawan);
                    params.put(ServerProyek.KEY_TGL_MULAI, tanggal_mulai_proyek);
                    params.put(ServerProyek.KEY_TGL_SELESAI, tanggal_selesai_proyek);
                    params.put(ServerProyek.KEY_DESK_PEKERJAAN, deskripsi_pekerjaan);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerProyek.URL_UPDATE_PROYEK, params);
                }

            }
            new ubahData().execute();
        }
    }

    private void hitungHargaTotal() {
        String jumlah = editTextJumlahBeli.getText().toString();
        String harga_satuan = editTextHargaSatuan.getText().toString();

        double j = Double.parseDouble(jumlah);
        double hs = Double.parseDouble(harga_satuan);

        int hitungharga = (int) (j * hs);
        String output = String.valueOf(hitungharga);

        editTextHargaBeliBarang.setText(output.toString());
    }
    private void simpanBeliBarang() {
        final String jml_beli = editTextJumlahBeli.getText().toString().trim();
        final String id_barang = editTextIDBarang.getText().toString().trim();
        final String id_proyek = editTextIDProyek.getText().toString().trim();
        final String hrg_belibrg = editTextHargaBeliBarang.getText().toString().trim();
        class SimpanBeliBarang extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        TransaksiProyek.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TransaksiProyek.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ServerProyek.KEY_JML_BELI,jml_beli);
                params.put(ServerProyek.KEY_ID_BARANG,id_barang);
                params.put(ServerProyek.KEY_ID_PROYEK,id_proyek);
                params.put(ServerProyek.KEY_HARGA_BELI_BARANG,hrg_belibrg);
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerProyek.URL_ADD_BELI_BARANG,params);
            }
        }
        new SimpanBeliBarang().execute();
    }
}
