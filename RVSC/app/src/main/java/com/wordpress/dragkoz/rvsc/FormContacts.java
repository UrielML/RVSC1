package com.wordpress.dragkoz.rvsc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormContacts extends AppCompatActivity {

    private ContactsDBAdapter dbAdapter;
    private Cursor cursor;

    private int modo ;

    private long id ;

    private EditText nombre;
    private EditText condiciones;
    private EditText contacto;
    private EditText telefono;
    private EditText email;
    private EditText observaciones;

    private Button boton_guardar;
    private Button boton_cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contacts);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        nombre = (EditText) findViewById(R.id.nombre);
        contacto = (EditText) findViewById(R.id.contacto);
        telefono = (EditText) findViewById(R.id.telefono);

        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        dbAdapter = new ContactsDBAdapter(this);
        dbAdapter.abrir();

        if (extra.containsKey(ContactsDBAdapter.C_COLUMNA_ID))
        {
            id = extra.getLong(ContactsDBAdapter.C_COLUMNA_ID);
            consultar(id);
        }

        establecerModo(extra.getInt(Contacts.C_MODO));

        boton_guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                guardar();
            }
        });

        boton_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                cancelar();
            }
        });

    }
    //modifica la forma en la que se vera interfaz editable o solo visualizacion
    private void establecerModo(int m)
    {
        this.modo = m ;

        if (modo == Contacts.C_VISUALIZAR)
        {
            this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        }
        else if (modo == Contacts.C_CREAR)
        {
            this.setTitle(R.string.contacto_crear_titulo);
            this.setEdicion(true);
        }
        else if (modo == Contacts.C_EDITAR)
        {
            this.setTitle(R.string.contacto_editar_titulo);
            this.setEdicion(true);
        }
    }
    //obtiene los valores de nombre y telefono
    private void consultar(long id)
    {
        cursor = dbAdapter.getRegistro(id);

        nombre.setText(cursor.getString(cursor.getColumnIndex(ContactsDBAdapter.C_COLUMNA_NOMBRE)));
        telefono.setText(cursor.getString(cursor.getColumnIndex(ContactsDBAdapter.C_COLUMNA_TELEFONO)));
    }

    private void setEdicion(boolean opcion)
    {
        nombre.setEnabled(opcion);
        telefono.setEnabled(opcion);
    }
    //obtieno los datos del formulario para modificacion o nuevo archivo
    private void guardar()
    {
        ContentValues reg = new ContentValues();

        if (modo == Contacts.C_EDITAR)
            reg.put(ContactsDBAdapter.C_COLUMNA_ID, id);

        reg.put(ContactsDBAdapter.C_COLUMNA_NOMBRE, nombre.getText().toString());
        reg.put(ContactsDBAdapter.C_COLUMNA_TELEFONO, telefono.getText().toString());

        if (modo == Contacts.C_CREAR)
        {
            dbAdapter.insert(reg);
            Toast.makeText(FormContacts.this, R.string.contacto_crear_confirmacion, Toast.LENGTH_SHORT).show();
        }
        else if (modo == Contacts.C_EDITAR)
        {
            Toast.makeText(FormContacts.this, R.string.contacto_editar_confirmacion, Toast.LENGTH_SHORT).show();
            dbAdapter.update(reg);
        }

        setResult(RESULT_OK);
        finish();
    }
    //termina la actividad
    private void cancelar()
    {
        setResult(RESULT_CANCELED, null);
        finish();
    }
    //elimina un registro
    private void borrar(final long id)
    {
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle(getResources().getString(R.string.contacto_eliminar_titulo));
        dialogEliminar.setMessage(getResources().getString(R.string.contacto_eliminar_mensaje));
        dialogEliminar.setCancelable(false);

        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                dbAdapter.delete(id);
                Toast.makeText(FormContacts.this, R.string.contacto_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        dialogEliminar.setNegativeButton(android.R.string.no, null);

        dialogEliminar.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();

        if (modo == Contacts.C_VISUALIZAR)
            getMenuInflater().inflate(R.menu.contacto_ver, menu);

        else
            getMenuInflater().inflate(R.menu.contacto_editar, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_eliminar:
                borrar(id);
                return true;

            case R.id.menu_cancelar:
                cancelar();
                return true;

            case R.id.menu_guardar:
                guardar();
                return true;

            case R.id.menu_editar:
                establecerModo(Contacts.C_EDITAR);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
