package com.example.projectmonitoring.master;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmonitoring.MainActivity;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.server.ServerBarang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MasterDetailBarang extends AppCompatActivity implements View.OnClickListener{
    private String id_barang, nama_barang, jumlah_barang;
    private EditText editTextIDBarang, editTextNamaBarang, editTextJumlahBarang;
    Button buttonTambahJumlah, buttonKurangJumlah, buttonHapusData, buttonUbahData;
    int jumlah = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail_barang);

        editTextIDBarang = (EditText) findViewById(R.id.editTextIDBarang);
        editTextNamaBarang = (EditText) findViewById(R.id.editTextNamaBarang);
        editTextJumlahBarang = (EditText) findViewById(R.id.editTextJumlahBarang);
        buttonTambahJumlah = (Button) findViewById(R.id.buttonTambahJumlah);
        buttonKurangJumlah = (Button) findViewById(R.id.buttonKurangJumlah);
        buttonUbahData = (Button) findViewById(R.id.buttonUbahData);
        buttonHapusData = (Button) findViewById(R.id.buttonHapusData);

        id_barang = getIntent().getStringExtra(ServerBarang.BARANG_ID);
        editTextIDBarang.setText(id_barang);
        nama_barang = getIntent().getStringExtra(ServerBarang.TAG_NAMA_BARANG);
        editTextNamaBarang.setText(nama_barang);
        jumlah_barang = getIntent().getStringExtra(ServerBarang.TAG_JML_BARANG);
        editTextJumlahBarang.setText(jumlah_barang);

        buttonTambahJumlah.setOnClickListener(this);
        buttonKurangJumlah.setOnClickListener(this);
        buttonUbahData.setOnClickListener(this);
        buttonHapusData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonTambahJumlah){
            jumlah += 1;
            display(jumlah);
        }
        if(v == buttonKurangJumlah){
            if(jumlah == 0){
                jumlah -= 0;
                display(jumlah);
            } else {
                jumlah -= 1;
                display(jumlah);
            }
        }
        if(v == buttonUbahData){
            ubahData();
            startActivity(new Intent(MasterDetailBarang.this,MasterBarang.class));
        }
        if(v == buttonHapusData){
            konfirmasiHapus();
        }
    }

    private void ubahData() {
        final String nama_barang = editTextNamaBarang.getText().toString().trim();
        final String jml_barang = editTextJumlahBarang.getText().toString().trim();
        class ubahData extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        MasterDetailBarang.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MasterDetailBarang.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ServerBarang.KEY_ID_BARANG, id_barang);
                params.put(ServerBarang.KEY_NAMA_BARANG, nama_barang);
                params.put(ServerBarang.KEY_JML_BARANG, jml_barang);
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerBarang.URL_UPDATE_BARANG, params);
            }

        }
        new ubahData().execute();
    }

    private void konfirmasiHapus(){
        new AlertDialog.Builder(MasterDetailBarang.this)
                .setMessage("Hapus data barang ini ?")
                .setTitle("KONFIRMASI")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusDataBarang();
                        startActivity(new Intent(MasterDetailBarang.this,MasterBarang.class));
                    }
                })
                .setNegativeButton("Tidak",null)
                .show();
    }

    private void hapusDataBarang() {
        class hapusDataBarang extends AsyncTask<Void,Void,String>{

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(
                        MasterDetailBarang.this,"Menghapus Data",
                        "Tunggu Sebentar",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(MasterDetailBarang.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequestParam(ServerBarang.URL_DELETE_BARANG,id_barang);
            }
        }

        new hapusDataBarang().execute();
    }

    private void display(int jumlah) {
        EditText editTextJumlahBarang = (EditText) findViewById(R.id.editTextJumlahBarang);
        editTextJumlahBarang.setText("" + jumlah);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MasterDetailBarang.this, MasterBarang.class));
        finishAffinity();
    }
}
