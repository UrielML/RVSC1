package com.wordpress.dragkoz.rvsc;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class h extends Activity {

    double latitud = 0.0;
    double longitud = 0.0;
    private ContactsDBAdapter dbAdapter;
    private Cursor cursor;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);
        dbAdapter = new ContactsDBAdapter(this);
        dbAdapter.abrir();
        //verifica que tenga permisos para usar los sensores de ubicacion, de ser asi inica la deteccion de ubicacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart(1);
        }
        locationStart(1);
    }
    //iincia o detiene la acualizacion de la ubicacion mediate el ṕarametro n
    private void locationStart(int n) {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        if(n==1) {
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) locationListener);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) locationListener);
        }else if(n==2){
            mlocManager.removeUpdates(locationListener);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart(1);
                return;
            }
        }

    }
    //actividad que recibe los valores sos servicios de localiczacion
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
           //obtiene las coordenas las almacena en variables
            location.getLatitude();
            location.getLongitude();

            latitud = location.getLatitude();
            longitud = location.getLongitude();

            Toast.makeText(getApplicationContext(),"Lat:"+latitud,Toast.LENGTH_SHORT).show();
            //si ya tiene ua ubicacion abre google maps mostrando los hospitales mas cercanos
            //iniia ell proseso de enviar mesajes a losconcatos de emergenia
            if(latitud>0){
                Uri gmmIntentUri = Uri.parse("geo:"+latitud+","+longitud+"?q=hospitals");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                locationStart(2);
                String data=getIntent().getStringExtra("dataInPrint");
                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                numeros(data,latitud,longitud);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };
    //obtiene todos los telefonos de la base de datos
    private void numeros(String dataInPrint, double lat, double lon){
        cursor = dbAdapter.telefono();
        if (cursor.moveToFirst()) {
            do {
                //try {
                    //Thread.sleep(1000);
                    String telefonos = cursor.getString(0);
                    Log.w("------>",telefonos);
                    EnviarMensaje(telefonos,dataInPrint, lat, lon);
                //}catch (InterruptedException e){}

            } while (cursor.moveToNext());
        }
    }
    //este metodo enva el mensaje sin la necesidad de abrir el gestor de mensajeria
    private void EnviarMensaje (String strPhone,String strMessage, double lat, double lon){
        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)) {
            Toast.makeText(this, "Lo sentimos, tu dispositivo probablemente no pueda enviar SMS...", Toast.LENGTH_SHORT).show();
        }
        try {
            String la=""+lat;
            String lo=""+lon;
            SmsManager sms = SmsManager.getDefault();

            sms.sendTextMessage(strPhone,null,"Ayuda tengo una temperatra de "+strMessage+"°C",null,null);
            sms.sendTextMessage(strPhone,null,"ubicacion: lat:"+la+", long: "+lo,null,null);

            Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
