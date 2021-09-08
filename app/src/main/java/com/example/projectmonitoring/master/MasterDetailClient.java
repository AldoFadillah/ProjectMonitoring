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

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.server.ServerBarang;
import com.example.projectmonitoring.server.ServerClient;

import java.util.HashMap;

public class MasterDetailClient extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextNamaClient, editTextNohpClient;
    String id_client, nama_client, nohp_client;
    Button buttonHapusData, buttonUbahData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail_client);

        buttonUbahData = (Button) findViewById(R.id.buttonUbahData);
        buttonHapusData = (Button) findViewById(R.id.buttonHapusData);
        editTextNamaClient = (EditText) findViewById(R.id.editTextNamaClient);
        editTextNohpClient = (EditText) findViewById(R.id.editTextNohpClient);

        id_client = getIntent().getStringExtra(ServerClient.CLIENT_ID);
        nama_client = getIntent().getStringExtra(ServerClient.TAG_NAMA_CLIENT);
        editTextNamaClient.setText(nama_client);
        nohp_client = getIntent().getStringExtra(ServerClient.TAG_NOHP_CLIENT);
        editTextNohpClient.setText(nohp_client);

        buttonUbahData.setOnClickListener(this);
        buttonHapusData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUbahData){
            ubahData();
            startActivity(new Intent(MasterDetailClient.this,MasterTampilSemuaClient.class));
        }
        if(v == buttonHapusData){
            konfirmasiHapus();
        }
    }

    private void ubahData() {
        final String nama_client = editTextNamaClient.getText().toString().trim();
        final String nohp_client = editTextNohpClient.getText().toString().trim();
        class ubahData extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        MasterDetailClient.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MasterDetailClient.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ServerClient.KEY_ID_CLIENT,id_client);
                params.put(ServerClient.KEY_NAMA_CLIENT,nama_client);
                params.put(ServerClient.KEY_NOHP_CLIENT,nohp_client);
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerClient.URL_UPDATE_CLIENT,params);
            }
        }

        new ubahData().execute();
    }

    private void konfirmasiHapus(){
        new AlertDialog.Builder(MasterDetailClient.this)
                .setMessage("Hapus data client ini ?")
                .setTitle("KONFIRMASI")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusDataClient();
                        startActivity(new Intent(MasterDetailClient.this,MasterTampilSemuaClient.class));
                    }
                })
                .setNegativeButton("Tidak",null)
                .show();
    }
    private void hapusDataClient() {
        class hapusDataClient extends AsyncTask<Void,Void,String> {

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(
                        MasterDetailClient.this,"Menghapus Data",
                        "Tunggu Sebentar",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(MasterDetailClient.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequestParam(ServerClient.URL_DELETE_CLIENT,id_client);
            }
        }

        new hapusDataClient().execute();
    }
}
