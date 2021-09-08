package com.example.projectmonitoring.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.laporan.DetailCetakKendala;
import com.example.projectmonitoring.laporan.LaporanDetailCetakPengeluaranDana;
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
import java.util.Locale;

public class TransaksiDetailCetakKwitansi extends AppCompatActivity implements View.OnClickListener {
    EditText editTextIDKwitansi, editTextTanggalKwitansi, editTextNamaProyek, editTextBiayaTenagaKerja, editTextNamaClient, editTextIDProyek, editTextBayarProyek;
    String id_kwitansi, nama_proyek, id_proyek, nama_client;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    private PdfPCell cell;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
    BaseColor Putih = WebColors.getRGBColor("#FFFFFF");
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_semua_barang_beli;
    private ListView list_semua_namabarang;
    private ListView list_semua_jml_barang;
    private ListView list_semua_harga_belibarang;
    ArrayList<HashMap<String, String>> arrayList_semua_barang_beli;
    ArrayList<HashMap<String, String>> arrayList_semua_namabarang;
    ArrayList<HashMap<String, String>> arrayList_semua_jml_barang;
    ArrayList<HashMap<String, String>> arrayList_semua_harga_belibarang;
    Button buttonCetakKwitansi;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Date dateTime;
    DateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_detail_cetak_kwitansi);

        editTextIDKwitansi = (EditText) findViewById(R.id.editTextIDKwitansi);
        editTextIDProyek = (EditText) findViewById(R.id.editTextIDProyek);
        editTextNamaProyek = (EditText) findViewById(R.id.editTextNamaProyek);
        editTextBiayaTenagaKerja = (EditText) findViewById(R.id.editTextBiayaTenagaKerja);
        editTextTanggalKwitansi = (EditText) findViewById(R.id.editTextTanggalKwitansi);
        editTextNamaClient = (EditText) findViewById(R.id.editTextNamaClient);
        list_semua_barang_beli = (ListView) findViewById(R.id.list_semua_barang_beli);
        list_semua_namabarang = (ListView) findViewById(R.id.list_semua_namabarang);
        list_semua_jml_barang = (ListView) findViewById(R.id.list_semua_jml_barang);
        list_semua_harga_belibarang = (ListView) findViewById(R.id.list_semua_harga_belibarang);
        buttonCetakKwitansi = (Button) findViewById(R.id.buttonCetakKwitansi);

        id_proyek = getIntent().getStringExtra(ServerKwitansi.PROYEK_ID);
        editTextIDProyek.setText(id_proyek);
        id_kwitansi = getIntent().getStringExtra(ServerKwitansi.PROYEK_ID);
        editTextIDKwitansi.setText(id_kwitansi);
        nama_client = getIntent().getStringExtra(ServerKwitansi.TAG_NAMA_CLIENT);
        editTextNamaClient.setText(nama_client);
        nama_proyek = getIntent().getStringExtra(ServerKwitansi.TAG_NAMA_PROYEK);
        editTextNamaProyek.setText(nama_proyek);

        EditText ettk = (EditText) findViewById(R.id.editTextTanggalKwitansi);
        setDate(ettk);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelTanggal();
            }
        };

        editTextTanggalKwitansi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TransaksiDetailCetakKwitansi.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        requestQueue = Volley.newRequestQueue(TransaksiDetailCetakKwitansi.this);

        arrayList_semua_barang_beli = new ArrayList<HashMap<String, String>>();
        arrayList_semua_namabarang = new ArrayList<HashMap<String, String>>();
        arrayList_semua_jml_barang = new ArrayList<HashMap<String, String>>();
        arrayList_semua_harga_belibarang = new ArrayList<HashMap<String, String>>();

        tampilListBarangBeli();

        buttonCetakKwitansi.setOnClickListener(this);
    }
    private void tampilListBarangBeli() {
        String url = "https://tokoyr.com/projectmonitoring/tampil_semua_laporan_pengeluaran_dana.php?id_proyek="+ id_proyek;
        String url1 = "https://tokoyr.com/projectmonitoring/tampil_semua_laporan_pengeluaran_dana_nama_brg.php?id_proyek="+ id_proyek;
        String url2 = "https://tokoyr.com/projectmonitoring/tampil_semua_laporan_pengeluaran_dana_jumlah_brg.php?id_proyek="+ id_proyek;
        String url3 = "https://tokoyr.com/projectmonitoring/tampil_semua_laporan_pengeluaran_dana_harga_belibrg.php?id_proyek="+ id_proyek;
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
                        arrayList_semua_barang_beli.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TransaksiDetailCetakKwitansi.this, arrayList_semua_barang_beli, R.layout.list_semua_laporan_pengeluaran_dana,
                        new String[]{"nama_barang", "harga_satuanbrg", "jml_beli", "hrgbeli_barang"},
                        new int[]{R.id.txt_nama_barang, R.id.txt_harga_satuanbrg, R.id.txt_jml_beli, R.id.txt_hrgbeli_barang});

                list_semua_barang_beli.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiDetailCetakKwitansi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

        stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("beli");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("nama_barang", json.getString("nama_barang"));
                        map.put("harga_satuanbrg", json.getString("harga_satuanbrg"));
                        arrayList_semua_namabarang.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TransaksiDetailCetakKwitansi.this, arrayList_semua_namabarang, R.layout.list_semua_laporan_pengeluaran_dana_nama_barang,
                        new String[]{"nama_barang", "harga_satuanbrg"},
                        new int[]{R.id.txt_nama_barang, R.id.txt_harga_satuanbrg});

                list_semua_namabarang.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiDetailCetakKwitansi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

        stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("beli");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("jml_beli", json.getString("jml_beli"));
                        arrayList_semua_jml_barang.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TransaksiDetailCetakKwitansi.this, arrayList_semua_jml_barang, R.layout.list_semua_laporan_pengeluaran_dana_jumlah_barang,
                        new String[]{"jml_beli"},
                        new int[]{R.id.txt_jumlah_barang});

                list_semua_jml_barang.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiDetailCetakKwitansi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

        stringRequest = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("beli");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("hrgbeli_barang", json.getString("hrgbeli_barang"));
                        arrayList_semua_harga_belibarang.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        TransaksiDetailCetakKwitansi.this, arrayList_semua_harga_belibarang, R.layout.list_semua_laporan_pengeluaran_dana_hrg_belibarang,
                        new String[]{"hrgbeli_barang"},
                        new int[]{R.id.txt_harga_beli_barang});

                list_semua_harga_belibarang.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransaksiDetailCetakKwitansi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void setDate (TextView view){
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
    }
    private void updateLabelTanggal() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTanggalKwitansi.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onClick(View v) {
        if (v == buttonCetakKwitansi){
            if (editTextBiayaTenagaKerja.length()==0){
                Toast.makeText(getApplicationContext(), "Harap isi biaya tenaga kerja!", Toast.LENGTH_LONG).show();
            }else{
                insertToKwitansi();
                requestStoragePermission();
                createPDF(list_semua_barang_beli.getAdapter().getCount(), Integer.valueOf(editTextBiayaTenagaKerja.getText().toString()), String.valueOf((R.id.txt_harga_beli_barang)), editTextNamaClient.getText().toString(), editTextNamaProyek.getText().toString(), editTextIDKwitansi.getText().toString(), editTextTanggalKwitansi.getText().toString());
            }
        }
    }

    private void insertToKwitansi() {
        final String tgl_kwitansi = editTextTanggalKwitansi.getText().toString().trim();
        final String biaya_tenagakerja = editTextBiayaTenagaKerja.getText().toString().trim();
        final String id_proyek = editTextIDProyek.getText().toString().trim();
        final int hrg_bayarproyek = Integer.valueOf(editTextBiayaTenagaKerja.getText().toString()) * list_semua_barang_beli.getAdapter().getCount();
        class SimpanClient extends AsyncTask<Void,Void,String > {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(
                        TransaksiDetailCetakKwitansi.this,
                        "Menambahkan...",
                        "Tunggu Sebentar...",
                        false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TransaksiDetailCetakKwitansi.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ServerKwitansi.KEY_TGL_KWITANSI,tgl_kwitansi);
                params.put(ServerKwitansi.KEY_BIAYA_TENAGA_KERJA,biaya_tenagakerja);
                params.put(ServerKwitansi.KEY_HARGA_BAYAR_PROYEK, String.valueOf(hrg_bayarproyek));
                params.put(ServerKwitansi.KEY_ID_PROYEK,id_proyek);
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.sendPostRequest(ServerKwitansi.URL_ADD_KWITANSI,params);
            }
        }

        new SimpanClient().execute();
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
    private void createPDF(Integer countArray, Integer biayatenagakerja, String hargabelibarang, String namaclient, String namaproyek, String idkwitansi, String tglcetak) {
//reference to EditText
//        EditText et=(EditText)findViewById(R.id.txt_input);
//create document object
        Document doc=new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyy");
        dateTime = new Date();
        Paint paint = new Paint();
//output file path
        String outpath= Environment.getExternalStorageDirectory()+"/"+"Kwitansi-"+nama_proyek+"-"+sdf.format(Calendar.getInstance().getTime())+".pdf";
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
            Drawable myImage = TransaksiDetailCetakKwitansi.this.getResources().getDrawable(R.drawable.logo);
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
                PdfPTable table = new PdfPTable(4);

                float[] columnWidth = new float[]{6, 31, 31, 32};
                table.setWidths(columnWidth);


                cell = new PdfPCell();

                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("KWITANSI"));
                cell.setColspan(6);
                cell.setPaddingLeft(190);
                table.addCell(cell);
                dateFormat = new SimpleDateFormat("dd/MM/yy");
                cell = new PdfPCell(new Phrase("Tanggal Cetak : "+ dateFormat.format(dateTime)));
                cell.setColspan(6);
                cell.setPaddingLeft(150);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Nomor Kwitansi : " + idkwitansi + "                                          Tanggal Cetak : " + dateFormat.format(dateTime) +
                        "\nNama Client : " + namaclient + "                               Nama Proyek : " + namaproyek ));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("No"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Nama Barang dan Harga Satuan"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Jumlah Beli"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Harga Beli Barang"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(3);

                for (int i = 0; i <= countArray-1; i++) {
                    table.addCell(String.valueOf(i+1));
                    table.addCell(list_semua_namabarang.getItemAtPosition(i).toString());
                    table.addCell(list_semua_jml_barang.getItemAtPosition(i).toString());
                    table.addCell(list_semua_harga_belibarang.getItemAtPosition(i).toString());
                }


                PdfPTable ftable = new PdfPTable(2);
                ftable.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("Biaya tenaga Kerja   : " +"  "+ biayatenagakerja));
                cell.setColspan(6);
                ftable.addCell(cell);
                cell = new PdfPCell(new Paragraph("Total : " + biayatenagakerja*list_semua_barang_beli.getAdapter().getCount()));
                cell.setColspan(6);
                ftable.addCell(cell);
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
