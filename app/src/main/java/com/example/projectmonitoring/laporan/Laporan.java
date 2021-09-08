package com.example.projectmonitoring.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.projectmonitoring.R;

public class Laporan extends AppCompatActivity implements View.OnClickListener {
    CardView laporan_pengeluaran_dana, laporan_kendala, laporan_selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        laporan_pengeluaran_dana = (CardView) findViewById(R.id.laporan_pengeluaran_dana);
        laporan_kendala = (CardView) findViewById(R.id.laporan_kendala);
        laporan_selesai = (CardView) findViewById(R.id.laporan_selesai);

        laporan_pengeluaran_dana.setOnClickListener(this);
        laporan_kendala.setOnClickListener(this);
        laporan_selesai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == laporan_pengeluaran_dana){
            startActivity(new Intent(Laporan.this, LaporanPengeluaranDana.class));
        }
        if (v == laporan_kendala){
            startActivity(new Intent(Laporan.this, LaporanKendala.class));
        }
        if (v == laporan_selesai){
            startActivity(new Intent(Laporan.this, LaporanSelesai.class));
        }
    }
}
