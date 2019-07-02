package com.pontianak.kasirpakusu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    int REQUEST_QR_CODE=1;
    Service_Connector service_connector;
    Helper helper;
    private String current = "";
    int currpoint=0;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    EditText txt_point;
    ImageButton bt_plus,bt_min;
    Button bt_manual;
    int maxpoint=0;
    int currpointpay=0;
    String cpointpay="";
    String currentpay="";
    int maxpointpay=200000;
    private TextInputEditText edt_id_transaksi;
    private TextInputEditText edt_nominal;
    LayoutInflater gabung2;
    AlertDialog nad;


    View mydialog ;
    TextView txt_id,txt_nominal;
    AlertDialog.Builder builder1;
    AlertDialog dialog1;
    Button bt_skip;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

         edt_id_transaksi=findViewById(R.id.edt_id_transaksi);
        edt_nominal=findViewById(R.id.edt_nominal_transaksi);
        service_connector=new Service_Connector();
        gabung2 =getLayoutInflater();
        mydialog = gabung2.inflate(R.layout.popup_lanjutkan_order, null);
        helper=new Helper(this);
        Button bt_lanjut=findViewById(R.id.bt_lanjut);
        bt_skip=findViewById(R.id.bt_skip);
        txt_point=findViewById(R.id.edt_nominal_point);
        nad = new AlertDialog.Builder(MainActivity.this).create();
        txt_id=mydialog.findViewById(R.id.txt_id_transaksi);
        txt_nominal=mydialog.findViewById(R.id.txt_nominal);
        //maxpoint=helper.meisinteger(intent.getStringExtra("point"));
       builder1 = new AlertDialog.Builder(MainActivity.this);
        // builder.setTitle("Transaksi Point");

        builder1.setView(mydialog);
        builder1.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                sendtransaksi(true);
            }
        });
        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
         dialog1 = builder1.create();

         bt_skip.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 transaksimanual();
             }
         });




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
        bt_manual=findViewById(R.id.bt_manual);
        bt_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaksimanual();
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




               // AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               // builder.setTitle("Transaksi Point");
                //builder1.vie
                txt_id.setText(edt_id_transaksi.getText().toString());
                txt_nominal.setText(helper.torupiah(currpoint));
                //builder.setView(mydialog);
               // builder.setMessage("Anda Yakin Ingin Menambahkan Transaksi "+edt_id_transaksi.getText().toString()+" Sebesar "+helper.torupiah(currpoint));



                dialog1.show();

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

                service_connector.sendgetrequestwithsession_v3(MainActivity.this, helper.session, "logoutkasir", new Service_Connector.VolleyResponseListener_v3() {
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

            case R.id.action_syarat:
                Intent po=new Intent(this,WebActivity.class);
                po.putExtra("url","banner/syarat");
                startActivity(po);
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
            currpointpay=tmb;
            txt_point.setText(helper.torupiah(currpointpay));
        }
    }
    private void mins(){
        int tmb=currpointpay-10000;
        if(tmb>=0){
            currpointpay=tmb;
            txt_point.setText(helper.torupiah(currpointpay));
        }
    }
    private void sendtransaksi(final Boolean forscanner){
        Boolean is_valid=true;
        if(edt_id_transaksi.getText().toString().length()<1){
            edt_id_transaksi.setError("Id Transaksi Harus Terisi");
            is_valid=false;
        }
        if(edt_nominal.getText().toString().length()<4){
            edt_nominal.setError("Nominal Pesanan Harus Terisi");
            is_valid=false;
        }
        if(is_valid){
            Map<String, String> params = new HashMap<String, String>();
            params.put("no_transaksi",edt_id_transaksi.getText().toString());

            if(forscanner){
                params.put("penggunaan_point",txt_point.getText().toString().replaceAll("[Rp,.]", ""));
                params.put("nominal",edt_nominal.getText().toString().replaceAll("[Rp,.]", ""));
                params.put("manual","0");

            }
            else{
                params.put("penggunaan_point","0");
                params.put("nominal",edt_nominal.getText().toString().replaceAll("[Rp,.]", ""));
                params.put("manual","1");

            }

            service_connector.sendpostrequestwithsession_v3(MainActivity.this,helper.session, "generatecode", params, new Service_Connector.VolleyResponseListener_v3() {
                @Override
                public void onError(String message) {

                }

                @Override
                public void onResponese(String response) {
                    try{
                        JSONObject respon=new JSONObject(response);
                        if(respon.getString("status").equals("1")){
                            if(forscanner) {
                                Intent intent = new Intent(MainActivity.this, QrcodeActivity.class);
                                intent.putExtra("hash", respon.getString("hash"));
                                intent.putExtra("nominal", helper.torupiah(currpoint));
                                intent.putExtra("penggunaan_point", helper.torupiah(currpointpay));
                                clearform();

                                startActivityForResult(intent, REQUEST_QR_CODE);
                            }
                            else{
                                Toast toast = Toast.makeText(MainActivity.this, "Transaksi Berhasil", Toast.LENGTH_LONG);
                                toast.show();
                                clearform();
                            }
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(respon.getString("message"));


                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
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

    }

    private boolean formvalidasi(){
        boolean isvalid=true;

        if(edt_id_transaksi.getText().toString().length()<1){
            isvalid=false;
            edt_id_transaksi.setError("isian ini harus terisi");
        }
        if(edt_nominal.getText().toString().length()<1){
            isvalid=false;
            edt_nominal.setError("isian ini harus terisi");
        }
        return isvalid;
    }
    private void clearform(){
        edt_id_transaksi.setText("");
        edt_nominal.setText("");
        txt_point.setText("0");
        currpointpay=0;
        currentpay="0";
    }

    private void transaksimanual(){


        if(formvalidasi()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Transaksi Manual");
            builder.setMessage("Transaksi ini akan ditandai sebagai transaksi yang tidak menggunakan aplikasi. Ingin Melanjutkan Transaksi ?");

            builder.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    sendtransaksi(false);
                }
            });
            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
