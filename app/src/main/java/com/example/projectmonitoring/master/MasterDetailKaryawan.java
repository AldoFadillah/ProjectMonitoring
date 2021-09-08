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
import android.widget.Toast;

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.server.ServerClient;
import com.example.projectmonitoring.server.ServerKaryawan;

import java.util.HashMap;

public class MasterDetailKaryawan extends AppCompatActivity implements View.OnClickListener{
    EditText editTextNamaKaryawan, editTextTempatLahir, editTextTanggalLahir, editTextNoTelp, editTextJabatan, editTextAlamat;
    String id_karyawan, nama_karyawan, tmp_lhr, tgl_lhr, notelp, jabatan, alamat;
    Button buttonHapusData, buttonUbahData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail_karyawan);

        buttonUbahData = (Button) findViewById(R.id.buttonUbahData);
        buttonHapusData = (Button) findViewById(R.id.buttonHapusData);
        editTextNamaKaryawan = (EditText) findViewById(R.id.editTextNamaKaryawan);
        editTextTempatLahir = (EditText) findViewById(R.id.editTextTempatLahir);
        editTextTanggalLahir = (EditText) findViewById(R.id.editTextTanggalLahir);
        editTextNoTelp = (EditText) findViewById(R.id.editTextNoTelp);
        editTextJabatan = (EditText) findViewById(R.id.editTextJabatan);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);

        id_karyawan = getIntent().getStringExtra(ServerKaryawan.KARYAWAN_ID);
        nama_karyawan = getIntent().getStringExtra(ServerKaryawan.TAG_NAME);
        editTextNamaKaryawan.setText(nama_karyawan);
        tmp_lhr = getIntent().getStringExtra(ServerKaryawan.TAG_TMP_LHR);
        editTextTempatLahir.setText(tmp_lhr);
        tgl_lhr = getIntent().getStringExtra(ServerKaryawan.TAG_TGL_LHR);
        editTextTanggalLahir.setText(tgl_lhr);
        notelp = getIntent().getStringExtra(ServerKaryawan.TAG_NOTELP);
        editTextNoTelp.setText(notelp);
        jabatan = getIntent().getStringExtra(ServerKaryawan.TAG_JABATAN);
        editTextJabatan.setText(jabatan);
        alamat = getIntent().getStringExtra(ServerKaryawan.TAG_ALAMAT);
        editTextAlamat.setText(alamat);

        buttonUbahData.setOnClickListener(this);
        buttonHapusData.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == buttonUbahData){
            ubahData();
            startActivity(new Intent(MasterDetailKaryawan.this,MasterTampilSemuaKaryawan.class));
        }
        if(v == buttonHapusData){
            konfirmasiHapus();
        }
    }

    private void ubahData() {
        final String name = editTextNamaKaryawan.getText().toString().trim();
        final String tmp_lhr = editTextTempatLahir.getText().toString().trim();
        final String tgl_lhr = editTextTanggalLahir.getText().toString().trim();
        final String notelp = editTextNoTelp.getText().toString().trim();
        final String jabatan = editTextJabatan.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();
        class ubahData extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        MasterDetailKaryawan.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MasterDetailKaryawan.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ServerKaryawan.KEY_ID_KARYAWAN,id_karyawan);
                params.put(ServerKaryawan.KEY_NAME,name);
                params.put(ServerKaryawan.KEY_TMP_LHR,tmp_lhr);
                params.put(ServerKaryawan.KEY_TGL_LHR,tgl_lhr);
                params.put(ServerKaryawan.KEY_NOTELP,notelp);
                params.put(ServerKaryawan.KEY_JABATAN,jabatan);
                params.put(ServerKaryawan.KEY_ALAMAT,alamat);
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerKaryawan.URL_UPDATE_KARYAWAN,params);
            }
        }

        new ubahData().execute();
    }

    private void konfirmasiHapus(){
        new AlertDialog.Builder(MasterDetailKaryawan.this)
                .setMessage("Hapus data pegawai ini ?")
                .setTitle("KONFIRMASI")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hapusDataKaryawan();
                        startActivity(new Intent(MasterDetailKaryawan.this,MasterTampilSemuaKaryawan.class));
                    }
                })
                .setNegativeButton("Tidak",null)
                .show();
    }
    private void hapusDataKaryawan() {
        class hapusDataKaryawan extends AsyncTask<Void,Void,String> {

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(
                        MasterDetailKaryawan.this,"Menghapus Data",
                        "Tunggu Sebentar",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(MasterDetailKaryawan.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequestParam(ServerKaryawan.URL_DELETE_KARYAWAN,id_karyawan);
            }
        }

        new hapusDataKaryawan().execute();
    }
}
