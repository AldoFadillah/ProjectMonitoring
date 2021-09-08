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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LaporanDetailCetakLaporanSelesai extends AppCompatActivity implements View.OnClickListener{
    EditText editTextNamaProyek, editTextTanggalCetak, editTextTglMulaiProyek, editTextTglSelesaiProyek;
    private PdfPCell cell;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
    BaseColor Putih = WebColors.getRGBColor("#FFFFFF");
    String nama_proyek, id_proyek, tgl_mulaiproyek, tgl_selesaiproyek;
    Button buttonCetakLaporanSelesai;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Date dateTime;
    DateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail_cetak_laporan_selesai);

        editTextNamaProyek = (EditText) findViewById(R.id.editTextNamaProyek);
        editTextTanggalCetak = (EditText) findViewById(R.id.editTextTanggalCetak);
        editTextTglMulaiProyek = (EditText) findViewById(R.id.editTextTglMulaiProyek);
        editTextTglSelesaiProyek = (EditText) findViewById(R.id.editTextTglSelesaiProyek);
        buttonCetakLaporanSelesai = (Button) findViewById(R.id.buttonCetakLaporanSelesai);

        nama_proyek = getIntent().getStringExtra(ServerKwitansi.TAG_NAMA_PROYEK);
        editTextNamaProyek.setText(nama_proyek);
        tgl_mulaiproyek = getIntent().getStringExtra(ServerKwitansi.TAG_TANGGAL_MULAI);
        editTextTglMulaiProyek.setText(tgl_mulaiproyek);
        tgl_selesaiproyek = getIntent().getStringExtra(ServerKwitansi.TAG_TANGGAL_SELESAI);
        editTextTglSelesaiProyek.setText(tgl_selesaiproyek);

        EditText ettk = (EditText) findViewById(R.id.editTextTanggalCetak);
        setDate(ettk);

        buttonCetakLaporanSelesai.setOnClickListener(this);
    }
    public void setDate (TextView view){
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("HmddMMyyyy");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
    }
    @Override
    public void onClick(View v) {
        if (v == buttonCetakLaporanSelesai){
            requestStoragePermission();
            createPDF(editTextNamaProyek.getText().toString(), editTextTanggalCetak.getText().toString(), editTextTglMulaiProyek.getText().toString(), editTextTglSelesaiProyek.getText().toString());
        }
    }

    private void createPDF(String sometext1, String sometext2, String sometext3, String sometext4) {
        requestStoragePermission();
/*reference to EditText
        EditText et=(EditText)findViewById(R.id.txt_input);*/
//create document object
        Document doc=new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("HmddMMyyyy");
        dateTime = new Date();
        Paint paint = new Paint();
//output file path
        String outpath= Environment.getExternalStorageDirectory()+"/"+"Laporan Proyek Selesai-"+nama_proyek+"-"+sdf.format(Calendar.getInstance().getTime())+".pdf";
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
            Drawable myImage = LaporanDetailCetakLaporanSelesai.this.getResources().getDrawable(R.drawable.logo);
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
                //cell.addElement(new Paragraph("Trinity Tuts"));
                //cell.addElement(new Paragraph("Trinity Tuts"));
                //cell.addElement(new Paragraph("1"));
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

                float[] columnWidth = new float[]{60, 30, 30};
                table.setWidths(columnWidth);

                cell = new PdfPCell();

                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("LAPORAN PROYEK SELESAI"));
                cell.setColspan(6);
                cell.setPaddingLeft(130);
                table.addCell(cell);
                dateFormat = new SimpleDateFormat("dd/MM/yy");
                cell = new PdfPCell(new Phrase("Tanggal Cetak : "+ dateFormat.format(dateTime)));
                cell.setColspan(6);
                cell.setPaddingLeft(150);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("\nDengan laporan ini maka dapat dinyatakan pelaksanaan pekerjaan proyek " + nama_proyek + "\nyang dimulai pada tanggal\t " + tgl_mulaiproyek + "\ndan diakhiri pada tanggal\t " + tgl_selesaiproyek + "\nkami informasikan bahwa pekerjaan tersebut telah selesai." + "\n\nDemikian laporan ini kami buat dengan sesungguhnya dan sesuai hasil kerja."));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);

                cell.setBackgroundColor(myColor1);

                PdfPTable ftable = new PdfPTable(1);
                //cell = new PdfPCell(new Paragraph("Footer"));
                //cell.setColspan(6);
                //ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                doc.add(table);
                //Buka file pdf
                Intent intent=new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(filePath);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getApplicationContext(), "Laporan Berhasil Dicetak!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Lokasi : Internal>Projectmonitoring", Toast.LENGTH_LONG).show();
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
