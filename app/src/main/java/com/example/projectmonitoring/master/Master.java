package com.example.projectmonitoring.master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectmonitoring.MainActivity;
import com.example.projectmonitoring.R;

public class Master extends AppCompatActivity implements View.OnClickListener{
CardView master_barang, master_client, master_karyawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        master_barang = (CardView) findViewById(R.id.master_barang);
        master_client = (CardView) findViewById(R.id.master_client);
        master_karyawan = (CardView) findViewById(R.id.master_karyawan);

        master_barang.setOnClickListener(Master.this);
        master_client.setOnClickListener(Master.this);
        master_karyawan.setOnClickListener(Master.this);
    }

    @Override
    public void onClick(View v) {
        if (v == master_barang){
            startActivity(new Intent(Master.this, MasterBarang.class));
        }
        if (v == master_client){
            startActivity(new Intent(Master.this, MasterClient.class));
        }
        if (v == master_karyawan){
            startActivity(new Intent(Master.this, MasterKaryawan.class));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Master.this, MainActivity.class));
        finishAffinity();
    }
}
