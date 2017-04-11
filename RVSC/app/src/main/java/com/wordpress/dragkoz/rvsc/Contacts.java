package com.wordpress.dragkoz.rvsc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Contacts extends AppCompatActivity {
    //se declaran variables globales
    private ContactsDBAdapter dbAdapter;
    private Cursor cursor;
    private ContactsCursorAdapter contactAdapter;
    private ListView lista;
    //indican de que forma se mostrara el layout
    public static final String C_MODO = "modo";
    public static final int C_VISUALIZAR = 551;
    public static final int C_CREAR = 552;
    public static final int C_EDITAR = 553;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //Se ejecuta crea la base de datos a partir de la clase ContactsDBHelper
        ContactsDBHelper dbHelper = new ContactsDBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //se declara la lista y se inican las consultas
        lista = (ListView) findViewById(R.id.contactList);
        dbAdapter = new ContactsDBAdapter(this);
        dbAdapter.abrir();
        consultar();

        Toast.makeText(getBaseContext(), "Base de datos preparada", Toast.LENGTH_LONG).show();
    }
    //se le dan los datos al cursor y se espesifica el dato que se mostrara
    //se agregan a la lista
    private void consultar() {
        cursor = dbAdapter.getCursor();
        startManagingCursor(cursor);
        contactAdapter = new ContactsCursorAdapter(this, cursor);
        lista.setAdapter(contactAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visualizar(id);
            }
        });
    }
    // Llamamos a la Actividad FormContacts indicando el modo visualización y el identificador del registro
    private void visualizar(long id) {

        Intent i = new Intent(Contacts.this, FormContacts.class);
        i.putExtra(C_MODO, C_VISUALIZAR);
        i.putExtra(ContactsDBAdapter.C_COLUMNA_ID, id);

        startActivityForResult(i, C_VISUALIZAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacto, menu);
        return true;
    }

    //Abrimos la actividad FormContacts con los datos extraidas de la lista
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_crear) {
            Intent i;
            i = new Intent(Contacts.this, FormContacts.class);
            i.putExtra(C_MODO, C_CREAR);
            startActivityForResult(i, C_CREAR);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}