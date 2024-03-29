package com.wordpress.dragkoz.rvsc;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static android.R.attr.id;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //se inicializan los componentes
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

            dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
            dialogEliminar.setTitle(getResources().getString(R.string.title_exit));
            dialogEliminar.setMessage(getResources().getString(R.string.question_exit));
            dialogEliminar.setCancelable(false);

            dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int boton) {
                   finish();
                }
            });
            dialogEliminar.setNegativeButton(android.R.string.no, null);

            dialogEliminar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //metodos para ir a los distintos layouts
    public void goToHome(){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void goToMaps(){
        Intent i = new Intent(this, Hospitals.class);
        startActivity(i);
    }
    public void goToContact(){
        Intent i = new Intent(this, Contacts.class);
        startActivity(i);
    }

    public void goToEstatus(){
        Intent i = new Intent(this, splash.class);
        startActivity(i);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            goToHome();
        } else if (id == R.id.nav_status) {
            goToEstatus();
        } else if (id == R.id.nav_contacts) {
            goToContact();
        } else if (id == R.id.nav_info) {
            Intent i = new Intent(this, testSQLITe.class);
            startActivity(i);
        } else if (id == R.id.nav_maps) {
            goToMaps();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
