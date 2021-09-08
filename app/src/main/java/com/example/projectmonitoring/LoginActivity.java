package com.example.projectmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener{
    private CheckBox rem_userpass;

    ProgressDialog pDialog;
    Button btn_login;
    EditText etxt_username, etxt_password;
    TextView txt_register;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = ServerLogin.URL + "login.php";

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Boolean session = false;
    String id_karyawan, username, name, jabatan, tmp_lhr, tgl_lhr, alamat, notelp;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        //Mencari nilai variabel pada activity xml
        btn_login = (Button) findViewById(R.id.btn_login);
        etxt_username = (EditText) findViewById(R.id.etxt_username);
        etxt_password = (EditText) findViewById(R.id.etxt_password);
        txt_register = (TextView) findViewById(R.id.txt_register);
        rem_userpass = (CheckBox)findViewById(R.id.checkBox);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        session = sharedpreferences.getBoolean(session_status, false);
        id_karyawan = sharedpreferences.getString(ServerLogin.TAG_ID_KARYAWAN, null);
        username = sharedpreferences.getString(ServerLogin.TAG_USERNAME, null);
        name = sharedpreferences.getString(ServerLogin.TAG_NAME, null);
        jabatan = sharedpreferences.getString(ServerLogin.TAG_JABATAN, null);
        tmp_lhr = sharedpreferences.getString(ServerLogin.TAG_TMP_LHR, null);
        tgl_lhr = sharedpreferences.getString(ServerLogin.TAG_TGL_LHR, null);
        alamat = sharedpreferences.getString(ServerLogin.TAG_ALAMAT, null);
        notelp = sharedpreferences.getString(ServerLogin.TAG_NOTELP, null);

        if(sharedpreferences.getBoolean(ServerLogin.KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);

        etxt_username.setText(sharedpreferences.getString(ServerLogin.KEY_USERNAME,""));
        etxt_password.setText(sharedpreferences.getString(ServerLogin.KEY_PASS,""));

        etxt_username.addTextChangedListener(this);
        etxt_password.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(ServerLogin.TAG_ID_KARYAWAN, id_karyawan);
            intent.putExtra(ServerLogin.TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = etxt_username.getText().toString();
                String password = etxt_password.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(ServerLogin.TAG_USERNAME);
                        String id_karyawan = jObj.getString(ServerLogin.TAG_ID_KARYAWAN);
                        String name = jObj.getString(ServerLogin.TAG_NAME);
                        String jabatan = jObj.getString(ServerLogin.TAG_JABATAN);
                        String tmp_lhr = jObj.getString(ServerLogin.TAG_TMP_LHR);
                        String tgl_lhr = jObj.getString(ServerLogin.TAG_TGL_LHR);
                        String alamat = jObj.getString(ServerLogin.TAG_ALAMAT);
                        String notelp = jObj.getString(ServerLogin.TAG_NOTELP);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(ServerLogin.TAG_ID_KARYAWAN, id_karyawan);
                        editor.putString(ServerLogin.TAG_NAME, name);
                        editor.putString(ServerLogin.TAG_JABATAN, jabatan);
                        editor.putString(ServerLogin.TAG_TMP_LHR, tmp_lhr);
                        editor.putString(ServerLogin.TAG_TGL_LHR, tgl_lhr);
                        editor.putString(ServerLogin.TAG_ALAMAT, alamat);
                        editor.putString(ServerLogin.TAG_NOTELP, notelp);
                        editor.putString(ServerLogin.TAG_USERNAME, username);
                        editor.commit();

                        // Memanggil main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(ServerLogin.TAG_ID_KARYAWAN, id_karyawan);
                        intent.putExtra(ServerLogin.TAG_USERNAME, username);
                        intent.putExtra(ServerLogin.TAG_NAME, name);
                        intent.putExtra(ServerLogin.TAG_JABATAN, jabatan);
                        intent.putExtra(ServerLogin.TAG_TMP_LHR, tmp_lhr);
                        intent.putExtra(ServerLogin.TAG_TGL_LHR, tgl_lhr);
                        intent.putExtra(ServerLogin.TAG_ALAMAT, alamat);
                        intent.putExtra(ServerLogin.TAG_NOTELP, notelp);
                        finish();
                        startActivity(intent);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        managePrefs();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        managePrefs();
    }

    private void managePrefs(){
        if(rem_userpass.isChecked()){
            editor.putString(ServerLogin.KEY_USERNAME, etxt_username.getText().toString().trim());
            editor.putString(ServerLogin.KEY_PASS, etxt_password.getText().toString().trim());
            editor.putBoolean(ServerLogin.KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(ServerLogin.KEY_REMEMBER, false);
            editor.remove(ServerLogin.KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(ServerLogin.KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }
}
