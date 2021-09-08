package com.example.projectmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.projectmonitoring.app.AppController;
import com.example.projectmonitoring.server.ServerLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button btn_register;
    EditText etxt_username, etxt_password, etxt_confirm_password, etxt_name, etxt_tempatlahir, etxt_tgl_lhr, etxt_notelp, etxt_jabatan, etxt_alamat;
    TextView txt_login;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = ServerLogin.URL + "register.php";

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        txt_login = (TextView) findViewById(R.id.txt_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        etxt_username = (EditText) findViewById(R.id.etxt_username);
        etxt_password = (EditText) findViewById(R.id.etxt_password);
        etxt_confirm_password = (EditText) findViewById(R.id.etxt_confirm_password);
        etxt_name = (EditText) findViewById(R.id.etxt_name);
        etxt_tempatlahir = (EditText) findViewById(R.id.etxt_tempatlahir);
        etxt_tgl_lhr = (EditText) findViewById(R.id.etxt_tgl_lhr);
        etxt_notelp = (EditText) findViewById(R.id.etxt_notelp);
        etxt_jabatan = (EditText) findViewById(R.id.etxt_jabatan);
        etxt_alamat = (EditText) findViewById(R.id.etxt_alamat);

        txt_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etxt_tgl_lhr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = etxt_username.getText().toString();
                String password = etxt_password.getText().toString();
                String confirm_password = etxt_confirm_password.getText().toString();
                String name = etxt_name.getText().toString();
                String tmp_lhr = etxt_tempatlahir.getText().toString();
                final String tgl_lhr = etxt_tgl_lhr.getText().toString().trim();
                String notelp = etxt_notelp.getText().toString();
                String jabatan = etxt_jabatan.getText().toString();
                String alamat = etxt_alamat.getText().toString();

                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    checkRegister(username, password, confirm_password, name, tmp_lhr, tgl_lhr, notelp, jabatan, alamat);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkRegister(final String username, final String password, final String confirm_password, final String name, final String tmp_lhr, final String tgl_lhr, final String notelp, final String jabatan, final String alamat) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        etxt_username.setText("");
                        etxt_password.setText("");
                        etxt_confirm_password.setText("");
                        etxt_name.setText("");
                        etxt_tempatlahir.setText("");
                        etxt_tgl_lhr.setText("");
                        etxt_notelp.setText("");
                        etxt_jabatan.setText("");
                        etxt_alamat.setText("");

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("confirm_password", confirm_password);
                params.put("name", name);
                params.put("tmp_lhr", tmp_lhr);
                params.put("tgl_lhr", tgl_lhr);
                params.put("notelp", notelp);
                params.put("jabatan", jabatan);
                params.put("alamat", alamat);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(RegisterActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MMMM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etxt_tgl_lhr.setText(sdf.format(myCalendar.getTime()));
    }

}
