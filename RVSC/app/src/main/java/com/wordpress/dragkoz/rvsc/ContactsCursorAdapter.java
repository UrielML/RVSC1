package com.wordpress.dragkoz.rvsc;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by dragkoz on 5/04/17.
 */
//esta clase nos sirve como un filtro par obtener un dato mas especifico de una consulta
public class ContactsCursorAdapter extends CursorAdapter {
    private ContactsDBAdapter dbAdapter = null ;

    public ContactsCursorAdapter(Context context, Cursor c)
    {
        super(context, c);
        dbAdapter = new ContactsDBAdapter(context);
        dbAdapter.abrir();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView tv = (TextView) view ;

        tv.setText(cursor.getString(cursor.getColumnIndex(ContactsDBAdapter.C_COLUMNA_NOMBRE)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

        return view;
    }
}
