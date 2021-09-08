package com.example.projectmonitoring.master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectmonitoring.R;

public class MasterKaryawan extends AppCompatActivity implements View.OnClickListener{
    CardView master_karyawan_tambah_karyawan, master_karyawan_lihat_semua_karyawan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_karyawan);

        master_karyawan_tambah_karyawan = (CardView) findViewById(R.id.master_karyawan_tambah_karyawan);
        master_karyawan_lihat_semua_karyawan = (CardView) findViewById(R.id.master_karyawan_lihat_semua_karyawan);

        master_karyawan_tambah_karyawan.setOnClickListener(this);
        master_karyawan_lihat_semua_karyawan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == master_karyawan_tambah_karyawan){
            startActivity(new Intent(MasterKaryawan.this, MasterTambahKaryawan.class));
        }
        if (v == master_karyawan_lihat_semua_karyawan){
            startActivity(new Intent(MasterKaryawan.this, MasterTampilSemuaKaryawan.class));
        }
    }
}
