package com.example.projectmonitoring.transaksi;

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
import android.widget.Toast;

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.master.MasterDetailClient;
import com.example.projectmonitoring.master.MasterDetailKaryawan;
import com.example.projectmonitoring.master.MasterTampilSemuaClient;
import com.example.projectmonitoring.master.MasterTampilSemuaKaryawan;
import com.example.projectmonitoring.server.ServerKaryawan;
import com.example.projectmonitoring.server.ServerKendala;

import java.util.HashMap;

public class TransaksiDetailKendala extends AppCompatActivity implements View.OnClickListener {
    EditText editTextNamaProyek, editTextKendala, editTextSolusi;
    String id_kendala, judul_proyek, deskripsi_kendala, deskripsi_solusi;
    Button buttonHapusData, buttonUbahData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_detail_kendala);

        editTextNamaProyek = (EditText) findViewById(R.id.editTextNamaProyek);
        editTextKendala = (EditText) findViewById(R.id.editTextKendala);
        editTextSolusi = (EditText) findViewById(R.id.editTextSolusi);
        buttonUbahData = (Button) findViewById(R.id.buttonUbahData);
        buttonHapusData = (Button) findViewById(R.id.buttonHapusData);

        id_kendala = getIntent().getStringExtra(ServerKendala.KENDALA_ID);
        judul_proyek = getIntent().getStringExtra(ServerKendala.TAG_JUDUL_PROYEK);
        editTextNamaProyek.setText(judul_proyek);
        deskripsi_kendala = getIntent().getStringExtra(ServerKendala.TAG_DESKRIPSI_KENDALA);
        editTextKendala.setText(deskripsi_kendala);
        deskripsi_solusi = getIntent().getStringExtra(ServerKendala.TAG_DESKRIPSI_SOLUSI);
        editTextSolusi.setText(deskripsi_solusi);

        buttonHapusData.setOnClickListener(this);
        buttonUbahData.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == buttonUbahData){
            ubahData();
            startActivity(new Intent(TransaksiDetailKendala.this, TransaksiTampilSemuaKendala.class));
        }
        if(v == buttonHapusData){
            konfirmasiHapus();
        }
    }

    private void ubahData() {
        final String deskripsi_kendala = editTextKendala.getText().toString().trim();
        final String deskripsi_solusi = editTextSolusi.getText().toString().trim();
        class ubahData extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        TransaksiDetailKendala.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TransaksiDetailKendala.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ServerKendala.KEY_ID_KENDALA,id_kendala);
                params.put(ServerKendala.KEY_KENDALA,deskripsi_kendala);
                params.put(ServerKendala.KEY_SOLUSI,deskripsi_solusi);
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerKendala.URL_UPDATE_KENDALA,params);
            }
        }

        new ubahData().execute();
    }

    private void konfirmasiHapus(){
        new AlertDialog.Builder(TransaksiDetailKendala.this)
                .setMessage("Hapus data kendala ini ?")
                .setTitle("KONFIRMASI")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusDataKendala();
                        startActivity(new Intent(TransaksiDetailKendala.this, TransaksiTampilSemuaKendala.class));
                    }
                })
                .setNegativeButton("Tidak",null)
                .show();
    }
    private void hapusDataKendala() {
        class hapusDataKendala extends AsyncTask<Void,Void,String> {

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(
                        TransaksiDetailKendala.this,"Menghapus Data",
                        "Tunggu Sebentar",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(TransaksiDetailKendala.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequestParam(ServerKendala.URL_DELETE_KENDALA,id_kendala);
            }
        }

        new hapusDataKendala().execute();
    }
}
