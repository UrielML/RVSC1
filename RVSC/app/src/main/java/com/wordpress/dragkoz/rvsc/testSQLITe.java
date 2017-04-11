package com.wordpress.dragkoz.rvsc;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class testSQLITe extends AppCompatActivity {
    private ContactsDBAdapter dbAdapter;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sqlite);
        dbAdapter = new ContactsDBAdapter(this);
        dbAdapter.abrir();


        cursor = dbAdapter.telefono();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Thread.sleep (5000);
                } catch (Exception e) {
                }

                String telefonos = cursor.getString(0);
                Log.d("Numero", telefonos);
                EnviarMensaje(telefonos);
                try {
                    Thread.sleep (1000);
                } catch (Exception e) {
// Mensaje en caso de que falle
                }
            } while (cursor.moveToNext());
        }
    }

    private void EnviarMensaje (String telefonos){
        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)) {
            Toast.makeText(this, "Lo sentimos, tu dispositivo probablemente no pueda enviar SMS...", Toast.LENGTH_SHORT).show();
        }
        try {
            SmsManager sms = SmsManager.getDefault();

            sms.sendTextMessage(telefonos,null,"Hola",null,null);

            Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
