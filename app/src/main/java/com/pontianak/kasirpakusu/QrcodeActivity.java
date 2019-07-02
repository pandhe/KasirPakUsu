package com.pontianak.kasirpakusu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
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

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrcodeActivity extends AppCompatActivity {
    Bitmap bitmap;
    Button bt_kembali;
    TextView txt_ppoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ImageView img_qr=findViewById(R.id.img_qr);
        txt_ppoint=findViewById(R.id.txt_cut_point);

        bt_kembali=findViewById(R.id.bt_kembali);
        bt_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

}
