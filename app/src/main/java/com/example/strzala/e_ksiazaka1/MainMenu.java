package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView list=null,list1=null;
    Button zgloszenie,samochod;



    String[] zm = new String[1];
    String[] zm1 = new String[1];
    String[] zm2 = new String[1];

    String dane[] = new String[20];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {

            dane[0] = getIntent().getStringExtra("email");
            dane[1] = getIntent().getStringExtra("admin");


        }catch (Exception e)
        {
            Log.i("MainMenu",""+e);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Custom_row adapter=new Custom_row(MainMenu.this, zm);
        list=(ListView)findViewById(R.id.konto);
        list.setAdapter(adapter);

        zgloszenie =(Button) findViewById(R.id.zgloszenie);
        samochod = (Button) findViewById(R.id.button7);

        Custom_row_zgloszenie adapter1=new Custom_row_zgloszenie(this, zm1);
        list1=(ListView)findViewById(R.id.zgloszenia);
        list1.setAdapter(adapter1);

        Custom_row_pojazd adapter2=new Custom_row_pojazd(this, zm2);
        list1=(ListView)findViewById(R.id.pojazdy);
        list1.setAdapter(adapter2);

        if(dane[1]!=null)
        {
            zgloszenie.setVisibility(View.VISIBLE);
            samochod.setVisibility(View.VISIBLE);

        }

        zgloszenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this,dane_pojazd.class);
                startActivity(i);
            }
        });

        samochod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this,Historia_pojazd.class);
                i.putExtra("menu","zgloszenie");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //wylogowanie uzytkownika
            Intent c = new Intent(MainMenu.this, MainActivity.class);
            c.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(c);
            finish();
        } else if (id == R.id.nav_gallery) {
            // Handle the camera action
        } else if (id == R.id.nav_slideshow) {
            // Handle the camera action
        }else if (id == R.id.historia) {
            Intent i = new Intent(MainMenu.this,Historia_pojazd.class);
            i.putExtra("menu","historia");
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
