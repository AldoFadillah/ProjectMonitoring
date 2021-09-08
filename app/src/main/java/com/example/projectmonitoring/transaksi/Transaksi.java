package com.example.projectmonitoring.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projectmonitoring.ListProyek;
import com.example.projectmonitoring.R;

public class Transaksi extends AppCompatActivity implements View.OnClickListener{
CardView transaksi_spp, transaksi_proyek, transaksi_kendala, transaksi_cetak_kwitansi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        transaksi_spp = (CardView) findViewById(R.id.transaksi_spp);
        transaksi_proyek = (CardView) findViewById(R.id.transaksi_proyek);
        transaksi_kendala = (CardView) findViewById(R.id.transaksi_kendala);
        transaksi_cetak_kwitansi = (CardView) findViewById(R.id.transaksi_cetak_kwitansi);

        transaksi_spp.setOnClickListener(Transaksi.this);
        transaksi_proyek.setOnClickListener(Transaksi.this);
        transaksi_kendala.setOnClickListener(Transaksi.this);
        transaksi_cetak_kwitansi.setOnClickListener(Transaksi.this);
    }

    @Override
    public void onClick(View v) {
        if (v == transaksi_spp){
            startActivity(new Intent(Transaksi.this, TransaksiSPP.class));
        }
        if (v == transaksi_proyek){
            startActivity(new Intent(Transaksi.this, ListProyek.class));
        }
        if (v == transaksi_kendala){
            startActivity(new Intent(Transaksi.this, TransaksiKendala.class));
        }
        if (v == transaksi_cetak_kwitansi){
            startActivity(new Intent(Transaksi.this, TransaksiCetakKwitansi.class));
        }
    }
}
