package com.wordpress.dragkoz.rvsc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class splash extends AppCompatActivity {
    ImageButton bluetooth;

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==bluetooth)
                goToBluetoothScan();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bluetooth = (ImageButton)findViewById(R.id.bluetooth_splash);
        bluetooth.setOnClickListener(onClickListener);
    }

    public void goToBluetoothScan(){
        Intent intent = new Intent(this, deviceList.class);
        startActivity(intent);
        finish();
    }
}