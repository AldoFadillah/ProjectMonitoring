package com.example.projectmonitoring.master;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.server.ServerKaryawan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MasterTambahKaryawan extends AppCompatActivity implements View.OnClickListener {
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button buttonSimpanKaryawan;
    EditText editTextNamaKaryawan, editTextTempatLahir, editTextTanggalLahir, editTextNoTelp, editTextJabatan, editTextAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_tambah_karyawan);

        editTextNamaKaryawan = (EditText) findViewById(R.id.editTextNamaKaryawan);
        editTextTempatLahir = (EditText) findViewById(R.id.editTextTempatLahir);
        editTextTanggalLahir = (EditText) findViewById(R.id.editTextTanggalLahir);
        editTextNoTelp = (EditText) findViewById(R.id.editTextNoTelp);
        editTextJabatan = (EditText) findViewById(R.id.editTextJabatan);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        buttonSimpanKaryawan = (Button) findViewById(R.id.buttonSimpanKaryawan);

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

        editTextTanggalLahir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MasterTambahKaryawan.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSimpanKaryawan.setOnClickListener(MasterTambahKaryawan.this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSimpanKaryawan){
            final String name = editTextNamaKaryawan.getText().toString().trim();
            final String tmp_lhr = editTextTempatLahir.getText().toString().trim();
            final String tgl_lhr = editTextTanggalLahir.getText().toString().trim();
            final String notelp = editTextNoTelp.getText().toString().trim();
            final String jabatan = editTextJabatan.getText().toString().trim();
            final String alamat = editTextAlamat.getText().toString().trim();
            class SimpanKaryawan extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            MasterTambahKaryawan.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    reset();
                    Toast.makeText(MasterTambahKaryawan.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(ServerKaryawan.KEY_NAME,name);
                    params.put(ServerKaryawan.KEY_TMP_LHR,tmp_lhr);
                    params.put(ServerKaryawan.KEY_TGL_LHR,tgl_lhr);
                    params.put(ServerKaryawan.KEY_NOTELP,notelp);
                    params.put(ServerKaryawan.KEY_JABATAN,jabatan);
                    params.put(ServerKaryawan.KEY_ALAMAT,alamat);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerKaryawan.URL_ADD_KARYAWAN,params);
                }
            }

            new SimpanKaryawan().execute();
        }
    }
    private void reset(){
        editTextNamaKaryawan.setText("");
        editTextTempatLahir.setText("");
        editTextTanggalLahir.setText("");
        editTextNoTelp.setText("");
        editTextJabatan.setText("");
        editTextAlamat.setText("");
    }
    private void updateLabel() {
        String myFormat = "yyyy-MMMM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        editTextTanggalLahir.setText(sdf.format(myCalendar.getTime()));
    }
}
