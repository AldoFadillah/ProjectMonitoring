package com.example.projectmonitoring.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.server.ServerKwitansi;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LaporanDetailCetakPengeluaranDana extends AppCompatActivity implements View.OnClickListener {
    EditText editTextNamaProyek, editTextTanggalCetak, editTextTglMulaiProyek, editTextTglSelesaiProyek;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_semua_barang;
    ArrayList<HashMap<String, String>> arrayList_semua_barang;
    private PdfPCell cell;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
    BaseColor Putih = WebColors.getRGBColor("#FFFFFF");
    String nama_proyek, id_proyek, tgl_mulaiproyek, tgl_selesaiproyek;
    Button buttonCetakPengeluaranDana;
    private static final int STORAGE_PERMISSION_CODE = 123;

    Date dateTime;
    DateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail_cetak_pengeluaran_dana);

        editTextNamaProyek = (EditText) findViewById(R.id.editTextNamaProyek);
        editTextTanggalCetak = (EditText) findViewById(R.id.editTextTanggalCetak);
        editTextTglMulaiProyek = (EditText) findViewById(R.id.editTextTglMulaiProyek);
        editTextTglSelesaiProyek = (EditText) findViewById(R.id.editTextTglSelesaiProyek);
        buttonCetakPengeluaranDana = (Button) findViewById(R.id.buttonCetakPengeluaranDana);
        list_semua_barang = (ListView) findViewById(R.id.list_semua_barang);

        id_proyek = getIntent().getStringExtra(ServerKwitansi.PROYEK_ID);

        nama_proyek = getIntent().getStringExtra(ServerKwitansi.TAG_NAMA_PROYEK);
        editTextNamaProyek.setText(nama_proyek);
        tgl_mulaiproyek = getIntent().getStringExtra(ServerKwitansi.TAG_TANGGAL_SELESAI);
        editTextTglMulaiProyek.setText(tgl_mulaiproyek);
        tgl_selesaiproyek = getIntent().getStringExtra(ServerKwitansi.TAG_TANGGAL_SELESAI);
        editTextTglSelesaiProyek.setText(tgl_selesaiproyek);

        EditText ettk = (EditText) findViewById(R.id.editTextTanggalCetak);
        setDate(ettk);

        buttonCetakPengeluaranDana.setOnClickListener(LaporanDetailCetakPengeluaranDana.this);

        requestQueue = Volley.newRequestQueue(LaporanDetailCetakPengeluaranDana.this);

        arrayList_semua_barang = new ArrayList<HashMap<String, String>>();

        tampilListBarang();
    }
    public void setDate (TextView view){
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
    }
    private void tampilListBarang() {
        String url = "https://tokoyr.com/projectmonitoring/tampil_semua_laporan_pengeluaran_dana.php?id_proyek="+ id_proyek;
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("beli");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_proyek", json.getString("id_proyek"));
                        map.put("id_barang", json.getString("id_barang"));
                        map.put("harga_satuanbrg", json.getString("harga_satuanbrg"));
                        map.put("nama_barang", json.getString("nama_barang"));
                        map.put("jml_beli", json.getString("jml_beli"));
                        map.put("hrgbeli_barang", json.getString("hrgbeli_barang"));
                        arrayList_semua_barang.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        LaporanDetailCetakPengeluaranDana.this, arrayList_semua_barang, R.layout.list_semua_laporan_pengeluaran_dana,
                        new String[]{"nama_barang", "jml_beli", "hrgbeli_barang"},
                        new int[]{R.id.txt_nama_barang, R.id.txt_jml_beli, R.id.txt_hrgbeli_barang});

                list_semua_barang.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LaporanDetailCetakPengeluaranDana.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonCetakPengeluaranDana){
            requestStoragePermission();
            createPDF(list_semua_barang.getAdapter().getCount());
        }
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    private void createPDF(Integer countArray) {
//reference to EditText
//        EditText et=(EditText)findViewById(R.id.txt_input);
//create document object
        Document doc=new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyy");
        dateTime = new Date();
        Paint paint = new Paint();
//output file path
        String outpath= Environment.getExternalStorageDirectory()+"/"+"Laporan Pengeluaran Dana-"+nama_proyek+"-"+sdf.format(Calendar.getInstance().getTime())+".pdf";
        File filePath = new File(outpath);
        try {
//create pdf writer instance
            PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            doc.open();
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = LaporanDetailCetakPengeluaranDana.this.getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.scaleAbsolute(406f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                //cell.addElement(new Paragraph("Trinity Tuts"));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(2);

                float[] columnWidth = new float[]{4, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("LAPORAN PENGELUARAN DANA"));
                cell.setColspan(6);
                cell.setPaddingLeft(120);
                table.addCell(cell);
                dateFormat = new SimpleDateFormat("dd/MM/yy");
                cell = new PdfPCell(new Phrase("Tanggal Cetak : "+ dateFormat.format(dateTime)));
                cell.setColspan(6);
                cell.setPaddingLeft(150);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("No"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Barang"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(2);

                for (int i = 0; i <= countArray-1; i++) {
                    table.addCell(String.valueOf(i+1));
                    table.addCell(arrayList_semua_barang.get(i).toString());
                }


                PdfPTable ftable = new PdfPTable(6);
                ftable.setWidthPercentage(100);
                //cell = new PdfPCell(new Paragraph("Footer"));
                //cell.setColspan(6);
                //ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                //Buka file pdf
                Intent intent=new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(filePath);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getApplicationContext(), "Berhasil Cetak! Lokasi Folder(Internal>ProjectMonitoring)", Toast.LENGTH_LONG).show();
                //buka file
                startActivity(intent);
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
