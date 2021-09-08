package com.example.projectmonitoring.master;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectmonitoring.R;
import com.example.projectmonitoring.RequestHandler;
import com.example.projectmonitoring.server.ServerClient;

import java.util.HashMap;

public class MasterClient extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextnamaclient, editTextnomorclient;
    private Button buttonSimpanClient, buttonTampilSemuaClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_client);

        editTextnamaclient = (EditText) findViewById(R.id.editTextnamaclient);
        editTextnomorclient = (EditText) findViewById(R.id.editTextnomorclient);
        buttonSimpanClient = (Button) findViewById(R.id.buttonSimpanClient);
        buttonTampilSemuaClient = (Button) findViewById(R.id.buttonTampilSemuaClient);

        buttonSimpanClient.setOnClickListener(MasterClient.this);
        buttonTampilSemuaClient.setOnClickListener(MasterClient.this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSimpanClient){
            final String nama_client = editTextnamaclient.getText().toString().trim();
            final String nohp_client = editTextnomorclient.getText().toString().trim();
            class SimpanClient extends AsyncTask<Void,Void,String > {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(
                            MasterClient.this,
                            "Menambahkan...",
                            "Tunggu Sebentar...",
                            false,false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    reset();
                    Toast.makeText(MasterClient.this, s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(ServerClient.KEY_NAMA_CLIENT,nama_client);
                    params.put(ServerClient.KEY_NOHP_CLIENT,nohp_client);
                    RequestHandler requestHandler = new RequestHandler();
                    return requestHandler.sendPostRequest(ServerClient.URL_ADD_CLIENT,params);
                }
            }

            new SimpanClient().execute();
        }
        if (v == buttonTampilSemuaClient){
            startActivity(new Intent(MasterClient.this, MasterTampilSemuaClient.class));
        }
    }
    private void reset(){
        editTextnamaclient.setText("");
        editTextnomorclient.setText("");
    }
}
