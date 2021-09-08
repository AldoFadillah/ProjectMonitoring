package com.example.projectmonitoring.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.projectmonitoring.BuildConfig;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.server.ServerKwitansi;
import com.example.projectmonitoring.server.ServerSPP;
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

import static com.itextpdf.text.Element.ALIGN_JUSTIFIED;

public class DetailCetakKendala extends AppCompatActivity implements View.OnClickListener {
    EditText editTextTanggalCetak, editTextNamaProyek;
    private PdfPCell cell;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
    BaseColor Putih = WebColors.getRGBColor("#FFFFFF");
    String nama_proyek, id_proyek;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView list_semua_kendala;
    private ListView list_kendala;
    private ListView list_solusi;
    ArrayList<HashMap<String, String>> arrayList_semua_kendala;
    ArrayList<HashMap<String, String>> arrayList_kendala;
    ArrayList<HashMap<String, String>> arrayList_solusi;
    Button buttonCetakKendala;
    private static final int STORAGE_PERMISSION_CODE = 123;

    Date dateTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail_cetak_kendala);

        editTextNamaProyek = (EditText) findViewById(R.id.editTextNamaProyek);
        editTextTanggalCetak = (EditText) findViewById(R.id.editTextTanggalCetak);
        buttonCetakKendala = (Button) findViewById(R.id.buttonCetakKendala);
        list_semua_kendala = (ListView) findViewById(R.id.list_semua_kendala);
        list_kendala = (ListView) findViewById(R.id.list_kendala);
        list_solusi = (ListView) findViewById(R.id.list_solusi);

        id_proyek = getIntent().getStringExtra(ServerKwitansi.PROYEK_ID);

        nama_proyek = getIntent().getStringExtra(ServerKwitansi.TAG_NAMA_PROYEK);
        editTextNamaProyek.setText(nama_proyek);

        EditText ettk = (EditText) findViewById(R.id.editTextTanggalCetak);
        setDate(ettk);

        buttonCetakKendala.setOnClickListener(DetailCetakKendala.this);
        
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

        editTextTanggalCetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(DetailCetakKendala.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        requestQueue = Volley.newRequestQueue(DetailCetakKendala.this);
        
        arrayList_semua_kendala = new ArrayList<HashMap<String, String>>();
        arrayList_kendala = new ArrayList<HashMap<String, String>>();
        arrayList_solusi = new ArrayList<HashMap<String, String>>();

        tampilListKendala();
    }
    private void tampilListKendala() {
        String url = "https://tokoyr.com/projectmonitoring/tampil_semua_laporan_kendala.php?id_proyek="+ id_proyek;
        String url1 = "https://tokoyr.com/projectmonitoring/tampil_semua_kendala.php?id_proyek="+ id_proyek;
        String url2 = "https://tokoyr.com/projectmonitoring/tampil_semua_solusi.php?id_proyek="+ id_proyek;
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("kendala");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("deskripsi_kendala", json.getString("deskripsi_kendala"));
                        map.put("deskripsi_solusi", json.getString("deskripsi_solusi"));
                        arrayList_semua_kendala.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        DetailCetakKendala.this, arrayList_semua_kendala, R.layout.list_semua_kendala,
                        new String[]{"deskripsi_kendala", "deskripsi_solusi"},
                        new int[]{R.id.txt_kendala, R.id.txt_solusi});

                list_semua_kendala.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCetakKendala.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

        stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("kendala");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("deskripsi_kendala ", json.getString("deskripsi_kendala"));
                        arrayList_kendala.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        DetailCetakKendala.this, arrayList_kendala, R.layout.list_kendala,
                        new String[]{"deskripsi_kendala"},
                        new int[]{R.id.txt_kendala});

                list_kendala.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCetakKendala.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

        stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("kendala");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("deskripsi_solusi ", json.getString("deskripsi_solusi"));
                        arrayList_solusi.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        DetailCetakKendala.this, arrayList_solusi, R.layout.list_solusi,
                        new String[]{"deskripsi_solusi"},
                        new int[]{R.id.txt_solusi});

                list_solusi.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCetakKendala.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
        editTextTanggalCetak.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonCetakKendala){
            requestStoragePermission();
            createPDF(list_kendala.getAdapter().getCount());
        }
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
        String outpath= Environment.getExternalStorageDirectory()+"/"+"Laporan Kendala-"+nama_proyek+"-"+sdf.format(Calendar.getInstance().getTime())+".pdf";
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
            Drawable myImage = DetailCetakKendala.this.getResources().getDrawable(R.drawable.logo);
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
                PdfPTable table = new PdfPTable(3);

                float[] columnWidth = new float[]{6, 30, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("LAPORAN KENDALA"));
                cell.setColspan(6);
                cell.setPaddingLeft(160);
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
                cell = new PdfPCell(new Phrase("Kendala"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Solusi"));
                cell.setBackgroundColor(Putih);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(3);

                for (int i = 0; i <= countArray-1; i++) {
                    table.addCell(String.valueOf(i+1));
                    table.addCell(arrayList_kendala.get(i).toString());
                    table.addCell(list_solusi.getItemAtPosition(i).toString());
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
}
