package com.wordpress.dragkoz.rvsc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class deviceList extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ListView closeDevicesSearch;
    private ArrayAdapter adapter;
    private Button searchDevices;

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == searchDevices){
                adapter.clear();
                scanPairedDevices();
            }
        }
    };

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String MACAddress = info.substring(info.length() - 17);
            goToMainActivity(MACAddress);
            messageToast(MACAddress);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        closeDevicesSearch = (ListView)findViewById(R.id.list_device);
        searchDevices = (Button)findViewById(R.id.scan_devices);
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);
        closeDevicesSearch.setAdapter(adapter);
        searchDevices.setOnClickListener(onClickListener);

        scanPairedDevices();
        getBluetoothStatus();

        closeDevicesSearch.setOnItemClickListener(onItemClickListener);
    }

    public void getBluetoothStatus(){
        if(mBluetoothAdapter == null) {//Revisa si el bluetooth es compatible
            // El dispositivo no esta disponible
        }else if(!mBluetoothAdapter.isEnabled()){//Si el bluetooth no esta activado pide que lo active
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void scanPairedDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){//Si existe alguan dispositivo sicronizado lo muestra en un listView
            for (BluetoothDevice device : pairedDevices){//Agrega el nombre de los dispositivos ya sincronizados
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }


    public void messageToast(String message){
        Toast.makeText(deviceList.this, message, Toast.LENGTH_LONG).show();
    }

    public void goToMainActivity(String address){
        Intent intent = new Intent(this, body.class);
        finish();
        intent.putExtra("address",address);
        startActivity(intent);
    }
}