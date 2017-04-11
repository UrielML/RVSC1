package com.wordpress.dragkoz.rvsc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class ContactsDBAdapter extends Object {
    //variables a utilizar
    public static final String C_TABLA = "contactos";

    public static final String C_COLUMNA_ID = "_id";
    public static final String C_COLUMNA_NOMBRE = "hip_nombre";
    public static final String C_COLUMNA_TELEFONO = "hip_telefono";

    private Context contexto;
    private ContactsDBHelper dbHelper;
    private SQLiteDatabase db;

    private String[] columnas = new String[]{C_COLUMNA_ID, C_COLUMNA_NOMBRE, C_COLUMNA_TELEFONO};

    public ContactsDBAdapter(Context context) {
        this.contexto = context;
    }
    //esto ayuda a manipular la base de datos
    public ContactsDBAdapter abrir() throws SQLException {
        dbHelper = new ContactsDBHelper(contexto);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }
    //hace un filtro que obtendra los datos
    public Cursor getCursor() throws SQLException {
        Cursor c = db.query(true, C_TABLA, columnas, null, null, null, null, null, null);

        return c;
    }
    //hace una consulta para obtener un registro igualado al id que obtiene del listView del layout Contactos
    public Cursor getRegistro(long id) throws SQLException {
        Cursor c = db.query(true, C_TABLA, columnas, C_COLUMNA_ID + "=" + id, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    //metodo para insertar
    public long insert(ContentValues reg) {
        if (db == null)
            abrir();

        return db.insert(C_TABLA, null, reg);
    }
    //metodo para eliminar
    public long delete(long id) {
        if (db == null)
            abrir();

        return db.delete(C_TABLA, "_id=" + id, null);
    }
    //metodo para modificar/actualizar un registro
    public long update(ContentValues reg) {
        long result = 0;

        if (db == null)
            abrir();

        if (reg.containsKey(C_COLUMNA_ID)) {
            long id = reg.getAsLong(C_COLUMNA_ID);

            reg.remove(C_COLUMNA_ID);

            result = db.update(C_TABLA, reg, "_id=" + id, null);
        }
        return result;
    }
    //obtiene todos los telefonos de la tabla contactos
    public Cursor telefono() {
        Cursor c = db.rawQuery("SELECT hip_telefono FROM contactos", null);

        return c;
    }
}