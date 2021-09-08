package com.example.projectmonitoring.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectmonitoring.LoginActivity;
import com.example.projectmonitoring.MainActivity;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.server.ServerLogin;

import java.text.SimpleDateFormat;

public class Profil extends AppCompatActivity {
    TextView txt_id, txt_username, txt_name, txt_tmp_lhr, txt_tgl_lhr, txt_jabatan, txt_alamat, txt_notelp;
    String id_karyawan, username, name, tmp_lhr, tgl_lhr, jabatan, alamat, notelp;
    SharedPreferences sharedpreferences;
    ImageView ic_back;
    Boolean session = false;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_jabatan = (TextView) findViewById(R.id.txt_jabatan);
        txt_tmp_lhr = (TextView) findViewById(R.id.txt_tmp_lhr);
        txt_tgl_lhr = (TextView) findViewById(R.id.txt_tgl_lhr);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_notelp = (TextView) findViewById(R.id.txt_notelp);
        ic_back = (ImageView) findViewById(R.id.ic_back);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        id_karyawan = getIntent().getStringExtra(ServerLogin.TAG_ID_KARYAWAN);
        username = getIntent().getStringExtra(ServerLogin.TAG_USERNAME);
        name = getIntent().getStringExtra(ServerLogin.TAG_NAME);
        jabatan = getIntent().getStringExtra(ServerLogin.TAG_JABATAN);
        tmp_lhr = getIntent().getStringExtra(ServerLogin.TAG_TMP_LHR);
        tgl_lhr = getIntent().getStringExtra(ServerLogin.TAG_TGL_LHR);
        alamat = getIntent().getStringExtra(ServerLogin.TAG_ALAMAT);
        notelp = getIntent().getStringExtra(ServerLogin.TAG_NOTELP);

        txt_id.setText(id_karyawan);
        txt_username.setText(username);
        txt_name.setText(name);
        txt_jabatan.setText(jabatan);
        txt_tmp_lhr.setText(tmp_lhr);
        txt_tgl_lhr.setText(tgl_lhr);
        txt_alamat.setText(alamat);
        txt_notelp.setText(notelp);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_karyawan = sharedpreferences.getString(ServerLogin.TAG_ID_KARYAWAN, null);
        username = sharedpreferences.getString(ServerLogin.TAG_USERNAME, null);
        name = sharedpreferences.getString(ServerLogin.TAG_NAME, null);
        jabatan = sharedpreferences.getString(ServerLogin.TAG_JABATAN, null);
        tmp_lhr = sharedpreferences.getString(ServerLogin.TAG_TMP_LHR, null);
        tgl_lhr = sharedpreferences.getString(ServerLogin.TAG_TGL_LHR, null);
        alamat = sharedpreferences.getString(ServerLogin.TAG_ALAMAT, null);
        notelp = sharedpreferences.getString(ServerLogin.TAG_NOTELP, null);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putData();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profil.this, MainActivity.class));
        putData();
    }

    private void putData() {
        if (session) {
            Intent intent = new Intent(Profil.this, MainActivity.class);
            intent.putExtra(ServerLogin.TAG_ID_KARYAWAN, id_karyawan);
            intent.putExtra(ServerLogin.TAG_NAME, name);
            intent.putExtra(ServerLogin.TAG_TMP_LHR, tmp_lhr);
            intent.putExtra(ServerLogin.TAG_TGL_LHR, tgl_lhr);
            intent.putExtra(ServerLogin.TAG_JABATAN, jabatan);
            intent.putExtra(ServerLogin.TAG_ALAMAT, alamat);
            intent.putExtra(ServerLogin.TAG_NOTELP, notelp);
            intent.putExtra(ServerLogin.TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }
    }
}
