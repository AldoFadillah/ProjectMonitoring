package com.example.projectmonitoring;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectmonitoring.laporan.Laporan;
import com.example.projectmonitoring.master.Master;
import com.example.projectmonitoring.master.MasterBarang;
import com.example.projectmonitoring.master.MasterDetailBarang;
import com.example.projectmonitoring.profil.Profil;
import com.example.projectmonitoring.server.ServerLogin;
import com.example.projectmonitoring.transaksi.Transaksi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_logout;
    CardView card_master, card_transaksi, card_laporan, card_profil;
    SharedPreferences sharedpreferences;
    TextView txt_id, txt_username, txt_name, txt_jabatan, txt_tmp_lhr, txt_tgl_lhr, txt_alamat, txt_notelp;
    String id_karyawan, username, name, jabatan, tmp_lhr, tgl_lhr, alamat, notelp;
    Boolean session = false;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        card_master = (CardView) findViewById(R.id.card_master);
        card_transaksi = (CardView) findViewById(R.id.card_transaksi);
        card_laporan = (CardView) findViewById(R.id.card_laporan);
        card_profil = (CardView) findViewById(R.id.card_profil);
        txt_username = (TextView) findViewById(R.id.etxt_username);
        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_tmp_lhr = (TextView) findViewById(R.id.txt_tmp_lhr);
        txt_tgl_lhr = (TextView) findViewById(R.id.txt_tgl_lhr);
        txt_jabatan = (TextView) findViewById(R.id.txt_jabatan);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_notelp = (TextView) findViewById(R.id.txt_notelp);
        TextView dateView = (TextView)findViewById(R.id.hari);
        setDate(dateView);

        id_karyawan = getIntent().getStringExtra(ServerLogin.TAG_ID_KARYAWAN);
        username = getIntent().getStringExtra(ServerLogin.TAG_USERNAME);
        name = getIntent().getStringExtra(ServerLogin.TAG_NAME);
        tmp_lhr = getIntent().getStringExtra(ServerLogin.TAG_TMP_LHR);
        tgl_lhr = getIntent().getStringExtra(ServerLogin.TAG_TGL_LHR);
        jabatan = getIntent().getStringExtra(ServerLogin.TAG_JABATAN);
        alamat = getIntent().getStringExtra(ServerLogin.TAG_ALAMAT);
        notelp = getIntent().getStringExtra(ServerLogin.TAG_NOTELP);

        card_master.setOnClickListener(MainActivity.this);
        card_transaksi.setOnClickListener(MainActivity.this);
        card_laporan.setOnClickListener(MainActivity.this);
        card_profil.setOnClickListener(MainActivity.this);

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(ServerLogin.TAG_ID_KARYAWAN, null);
                editor.putString(ServerLogin.TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_karyawan = sharedpreferences.getString(ServerLogin.TAG_ID_KARYAWAN, null);
        username = sharedpreferences.getString(ServerLogin.TAG_USERNAME, null);
        name = sharedpreferences.getString(ServerLogin.TAG_NAME, null);
        tmp_lhr = sharedpreferences.getString(ServerLogin.TAG_TMP_LHR, null);
        tgl_lhr = sharedpreferences.getString(ServerLogin.TAG_TGL_LHR, null);
        jabatan = sharedpreferences.getString(ServerLogin.TAG_JABATAN, null);
        alamat = sharedpreferences.getString(ServerLogin.TAG_ALAMAT, null);
        notelp = sharedpreferences.getString(ServerLogin.TAG_NOTELP, null);
    }
    public void setDate (TextView view){
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE,dd-MMMM-yyyy");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
    }

    @Override
    public void onClick(View v) {
        if (v == card_master){
            startActivity(new Intent(MainActivity.this, Master.class));
        }
        if (v == card_transaksi){
            startActivity(new Intent(MainActivity.this, Transaksi.class));
        }
        if (v == card_laporan){
            startActivity(new Intent(MainActivity.this, Laporan.class));
        }
        if (v == card_profil){
            startActivity(new Intent(MainActivity.this, Profil.class));
            if (session) {
                Intent intent = new Intent(MainActivity.this, Profil.class);
                intent.putExtra(ServerLogin.TAG_ID_KARYAWAN, id_karyawan);
                intent.putExtra(ServerLogin.TAG_NAME, name);
                intent.putExtra(ServerLogin.TAG_TMP_LHR, tmp_lhr);
                intent.putExtra(ServerLogin.TAG_TGL_LHR, tgl_lhr);
                intent.putExtra(ServerLogin.TAG_JABATAN, jabatan);
                intent.putExtra(ServerLogin.TAG_ALAMAT, alamat);
                intent.putExtra(ServerLogin.TAG_NOTELP, notelp);
                intent.putExtra(ServerLogin.TAG_USERNAME, username);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder tombolkeluar = new AlertDialog.Builder(MainActivity.this);
        tombolkeluar.setMessage("Apakah anda yakin ingin keluar?");
        tombolkeluar.setTitle("Keluar Aplikasi");
        tombolkeluar.setIcon(R.drawable.ic_apps);
        tombolkeluar.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
                finishAffinity();
            }
        });
        tombolkeluar.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        tombolkeluar.setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        tombolkeluar.show();
    }
}
