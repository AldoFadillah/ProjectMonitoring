package com.example.projectmonitoring.laporan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmonitoring.R;
import com.example.projectmonitoring.adapter.Adapter;
import com.example.projectmonitoring.app.AppController;
import com.example.projectmonitoring.data.DataModelKwitansi;
import com.example.projectmonitoring.server.ServerKwitansi;
import com.example.projectmonitoring.transaksi.TransaksiDetailCetakKwitansi;
import com.example.projectmonitoring.transaksi.TransaksiCetakKwitansi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LaporanSelesai extends AppCompatActivity implements ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = LaporanSelesai.class.getSimpleName();
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";
    public static final String TAG_RESULTS = "results";
    String tag_json_obj = "json_obj_req";
    RequestQueue requestQueue;
    List<DataModelKwitansi> listData = new ArrayList<DataModelKwitansi>();
    Adapter adapter;
    SwipeRefreshLayout swipe;
    private ListView listView;
    ProgressDialog pDialog;
    Calendar myCalendar1, myCalendar2;
    DatePickerDialog.OnDateSetListener date1, date2;
    EditText editTextTanggalMulai, editTextTanggalSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_selesai);

        requestQueue = Volley.newRequestQueue(LaporanSelesai.this);

        listView = (ListView) findViewById(R.id.listView);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        editTextTanggalMulai = (EditText) findViewById(R.id.editTextTanggalMulai);
        editTextTanggalSelesai = (EditText) findViewById(R.id.editTextTanggalSelesai);
        adapter = new Adapter(LaporanSelesai.this, listData);
        listView.setAdapter(adapter);

        myCalendar1 = Calendar.getInstance();
        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelAwal();
            }
        };

        myCalendar2 = Calendar.getInstance();
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelAkhir();
                cariData();
            }
        };
        editTextTanggalMulai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LaporanSelesai.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editTextTanggalMulai.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    editTextTanggalSelesai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(LaporanSelesai.this)
                                    .setTitle("Delete entry")
                                    .setMessage("Are you sure you want to delete this entry?")
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                        }
                                    })
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });
                } else {
                    editTextTanggalSelesai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(LaporanSelesai.this, date2, myCalendar2
                                    .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                                    myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

        swipe.setOnRefreshListener(this);
    }

    private void cariData() {
        pDialog = new ProgressDialog(LaporanSelesai.this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, ServerKwitansi.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    int value = jObj.getInt(TAG_VALUE);

                    if (value == 1) {
                        listData.clear();
                        adapter.notifyDataSetChanged();

                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);

                            DataModelKwitansi data = new DataModelKwitansi();

                            data.setIdProyek(json.getString(ServerKwitansi.TAG_ID_PROYEK));
                            data.setNamaProyek(json.getString(ServerKwitansi.TAG_NAMA_PROYEK));
                            data.setTanggalMulaiProyek(json.getString(ServerKwitansi.TAG_TANGGAL_MULAI));
                            data.setTanggalSelesaiProyek(json.getString(ServerKwitansi.TAG_TANGGAL_SELESAI));
                            listData.add(data);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tgl_mulai", editTextTanggalMulai.getText().toString());
                params.put("tgl_selesai", editTextTanggalSelesai.getText().toString());
                return params;
            }

        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(LaporanSelesai.this, LaporanDetailCetakLaporanSelesai.class);
                intent.putExtra(ServerKwitansi.PROYEK_ID,listData.get(position).getIdProyek());
                intent.putExtra(ServerKwitansi.TAG_NAMA_PROYEK,listData.get(position).getNamaProyek());
                intent.putExtra(ServerKwitansi.TAG_TANGGAL_MULAI,listData.get(position).getTanggalMulaiProyek());
                intent.putExtra(ServerKwitansi.TAG_TANGGAL_SELESAI,listData.get(position).getTanggalSelesaiProyek());
                startActivity(intent);
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void updateLabelAwal() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTanggalMulai.setText(sdf.format(myCalendar1.getTime()));
    }

    private void updateLabelAkhir() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTanggalSelesai.setText(sdf.format(myCalendar2.getTime()));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this,TransaksiCetakKwitansi.class);
        HashMap<String, String> map = (HashMap)adapterView.getItemAtPosition(position);
        intent.putExtra(ServerKwitansi.PROYEK_ID,map.get(ServerKwitansi.PROYEK_ID).toString());
        intent.putExtra(ServerKwitansi.TAG_NAMA_PROYEK,map.get(ServerKwitansi.TAG_NAMA_PROYEK).toString());
        intent.putExtra(ServerKwitansi.TAG_TANGGAL_MULAI,map.get(ServerKwitansi.TAG_TANGGAL_MULAI).toString());
        intent.putExtra(ServerKwitansi.TAG_TANGGAL_SELESAI,map.get(ServerKwitansi.TAG_TANGGAL_SELESAI).toString());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
