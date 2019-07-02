package com.pontianak.kasirpakusu;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ManualActivity extends AppCompatActivity {
    int REQUEST_QR_CODE=1;
    Service_Connector service_connector;
    Helper helper;
    private String current = "";
    int currpoint=0;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    EditText txt_point;
    ImageButton bt_plus,bt_min;
    int maxpoint=0;
    int currpointpay=0;
    String cpointpay="";
    String currentpay="";
    int maxpointpay=200000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextInputEditText edt_id_transaksi=findViewById(R.id.edt_id_transaksi);
        final TextInputEditText edt_nominal=findViewById(R.id.edt_nominal_transaksi);
        service_connector=new Service_Connector();
        helper=new Helper(this);
        Button bt_lanjut=findViewById(R.id.bt_lanjut);
        txt_point=findViewById(R.id.edt_nominal_point);
        //maxpoint=helper.meisinteger(intent.getStringExtra("point"));


        bt_plus=findViewById(R.id.bt_plus);
        bt_min=findViewById(R.id.bt_min);
        bt_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mins();
            }
        });
        bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus();
            }
        });
        edt_nominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if(!s.toString().equals(current)){
                    edt_nominal.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = formatRupiah.format((parsed));

                    current = formatted;
                    edt_nominal.setText(formatted);
                    edt_nominal.setSelection(formatted.length());

                    edt_nominal.addTextChangedListener(this);
                }*/
                if(!s.toString().equals(current)){
                    edt_nominal.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,R,.]", "");



                    String formatted="";
                    if(cleanString.length()>1) {
                        double parsed = Double.parseDouble(cleanString);
                        Log.i("ez",cleanString);
                        formatted = formatRupiah.format((parsed));
                    }
                    else{
                        formatted =cleanString;

                    }


                    current = formatted;
                    edt_nominal.setText(formatted);
                    edt_nominal.setSelection(formatted.length());
                    currpoint=helper.meisinteger(edt_nominal.getText().toString().replaceAll("[Rp,R,.]", ""));

                    edt_nominal.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txt_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                // txt_point.setText(helper.torupiah(helper.meisinteger(txt_point.getText().toString())));
                if(!s.toString().equals(current)){
                    txt_point.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,R,.]", "");



                    String formatted="";
                    if(cleanString.length()>1) {
                        double parsed = Double.parseDouble(cleanString);
                        Log.i("ez",cleanString);
                        formatted = helper.formatRupiah.format((parsed));
                    }
                    else{
                        formatted =cleanString;

                    }


                    currentpay = formatted;
                    txt_point.setText(formatted);
                    txt_point.setSelection(formatted.length());
                    currpointpay=helper.meisinteger(txt_point.getText().toString().replaceAll("[Rp,R,.]", ""));

                    txt_point.addTextChangedListener(this);
                }

            }
        });

        bt_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_transaksi",edt_id_transaksi.getText().toString());
                params.put("penggunaan_point",txt_point.getText().toString().replaceAll("[Rp,.]", ""));
                params.put("nominal",edt_nominal.getText().toString().replaceAll("[Rp,.]", ""));

                service_connector.sendpostrequestwithsession_v3(ManualActivity.this,helper.session, "generatecode", params, new Service_Connector.VolleyResponseListener_v3() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponese(String response) {
                        try{
                            JSONObject respon=new JSONObject(response);
                            if(respon.getString("status").equals("1")){
                                Intent intent=new Intent(ManualActivity.this,QrcodeActivity.class);
                                intent.putExtra("hash",respon.getString("hash"));
                                intent.putExtra("nominal",helper.torupiah(currpoint));
                                intent.putExtra("penggunaan_point",helper.torupiah(currpointpay));

                                startActivityForResult(intent,REQUEST_QR_CODE);
                            }
                        }
                        catch (JSONException joe){

                        }

                    }

                    @Override
                    public void onNoConnection(String message) {

                    }

                    @Override
                    public void OnServerError(String message) {

                    }

                    @Override
                    public void OnTimeOut() {

                    }
                });

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_akun, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                service_connector.sendgetrequestwithsession_v3(ManualActivity.this, helper.session, "logoutkasir", new Service_Connector.VolleyResponseListener_v3() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponese(String response) {
                        helper.meditor.putBoolean("is_login",false).apply();
                        helper.meditor.putBoolean("remember_me",false).apply();
                        helper.meditor.putString("Set-Cookie"," ").apply();
                        helper.meditor.putString("username"," ").apply();
                        helper.meditor.putString("password"," ").apply();
                        finish();

                    }

                    @Override
                    public void onNoConnection(String message) {

                    }

                    @Override
                    public void OnServerError(String message) {

                    }

                    @Override
                    public void OnTimeOut() {

                    }
                });

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_QR_CODE){
            //Toast toast=Toast.makeText(this,data.getStringExtra("vocer"),Toast.LENGTH_LONG);
            //toast.show();
        }
    }
    private void plus(){
        int tmb=currpointpay+10000;
        if(tmb<=maxpointpay){
            currpoint=tmb;
            txt_point.setText(helper.torupiah(currpoint));
        }
    }
    private void mins(){
        int tmb=currpointpay-10000;
        if(tmb>=0){
            currpoint=tmb;
            txt_point.setText(helper.torupiah(currpoint));
        }
    }

}
