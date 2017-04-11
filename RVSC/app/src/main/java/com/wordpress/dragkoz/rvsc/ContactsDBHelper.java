package com.wordpress.dragkoz.rvsc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dragkoz on 5/04/17.
 */

public class ContactsDBHelper extends SQLiteOpenHelper {
    private static int version = 1;
    private static String name = "ContactsDb" ;
    private static SQLiteDatabase.CursorFactory factory = null;

    public ContactsDBHelper(Context context){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i(this.getClass().toString(), "Creando base de datos");

        db.execSQL( "CREATE TABLE contactos(" +
                " _id INTEGER PRIMARY KEY," +
                " hip_nombre TEXT NOT NULL, " +
                " hip_telefono TEXT)");

        db.execSQL( "CREATE UNIQUE INDEX hip_nombre ON contactos(hip_nombre ASC)" );

        Log.i(this.getClass().toString(), "Tabla contactos creada");

		db.execSQL("INSERT INTO contactos(_id, hip_nombre) VALUES(1,'UNAMED')");


        Log.i(this.getClass().toString(), "Datos iniciales Contactos insertados");

        Log.i(this.getClass().toString(), "Base de datos creada");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){


    }
}

