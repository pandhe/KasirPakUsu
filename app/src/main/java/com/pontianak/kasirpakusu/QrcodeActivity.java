package com.pontianak.kasirpakusu;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrcodeActivity extends AppCompatActivity {
    Bitmap bitmap;
    Button bt_kembali;
    TextView txt_ppoint;
    Service_Connector service_connector;
    Helper helper;
    AlertDialog.Builder builder1;
    AlertDialog dialog1;
    String hash;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            finish();
            //sqliteHelper.insertNote(intent.getStringExtra("title"),intent.getStringExtra("body"),"","","");
            //loadpoint(false);
            //playbeep();
           // txt_count_notif.setText(String.valueOf(sqliteHelper.getNotesCount()));



        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ImageView img_qr=findViewById(R.id.img_qr);
        txt_ppoint=findViewById(R.id.txt_cut_point);
        helper=new Helper(this);
        service_connector=new Service_Connector();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(MyConfig.PUSH_NOTIFICATION));


        bt_kembali=findViewById(R.id.bt_kembali);
        bt_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder1 = new AlertDialog.Builder(QrcodeActivity.this);
                // builder.setTitle("Transaksi Point");

                //builder1.setView(mydialog);
                builder1.setMessage("Ingin Membatalkan Transaksi Ini ?");
                builder1.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        bataltransaksi();
                    }
                });
                builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                dialog1 = builder1.create();
                dialog1.show();

            }
        });

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        TextView nominal=findViewById(R.id.txt_nominal_transaksi);

        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        Intent intent=getIntent();
        nominal.setText(intent.getStringExtra("nominal"));
        txt_ppoint.setText(intent.getStringExtra("penggunaan_point"));
        hash=intent.getStringExtra("hash");
        QRGEncoder qrgEncoder = new QRGEncoder(intent.getStringExtra("hash"), null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            img_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("ez", e.toString());
        }
    }

    public void bataltransaksi(){
        bt_kembali.setText("Loading...");
        Map<String, String> params = new HashMap<String, String>();
        params.put("hash",hash);

        service_connector.sendpostrequestwithsession_v3(QrcodeActivity.this,helper.session, "batalkan_transaksi", params, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {

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

    }

}
