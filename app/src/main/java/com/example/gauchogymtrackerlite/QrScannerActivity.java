package com.example.gauchogymtrackerlite;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class QrScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout activity_qr_scanner
        setContentView(R.layout.activity_qr_scanner);
    }
}