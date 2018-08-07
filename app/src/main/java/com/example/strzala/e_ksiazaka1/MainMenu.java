package com.example.strzala.e_ksiazaka1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView list=null,list1=null;
    Button zgloszenie,samochod;

    String dane[] = new String[20];
    Boolean s1=false,s2=false,s3=false;
    TextView tekst1,tekst2;
    Cursor DBcursor;

    File file;

    ArrayList<String> zm1 = new ArrayList<String>();
    ArrayList<String> zm2 = new ArrayList<String>();
    ArrayList<String> zm3 = new ArrayList<String>();
    ArrayList<String> zm4 = new ArrayList<String>();
    ArrayList<String> zm5 = new ArrayList<String>();
    ArrayList<String> zm6 = new ArrayList<String>();
    ArrayList<String> zm7 = new ArrayList<String>();
    ArrayList<String> zm8 = new ArrayList<String>();
    ArrayList<Blob> zdjecie = new ArrayList<Blob>();


    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    FileOutputStream fos =null;
    Connection connection = null;
    InputStream is;


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    public boolean activeNetwork () {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;

    }

    public void podlaczenieDB()
    {
        if(activeNetwork()) {
            //tworzenie polaczenia z baza danych
            String url ="jdbc:mysql://s56.linuxpl.com:3306/trustcar_app";
            String user = "trustcar_admin";
            String pass = "Kubamobile2001!";
            //  Log.i("login", getResources().getString(R.string.loginMySQL));
            // Log.i("haslo", getResources().getString(R.string.hasloMySQL));
            // Log.i("url", getResources().getString(R.string.url));


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                //   showToast("brak polaczenia z internetem");
                Log.i("aaa", String.valueOf(e));
            }

            try {
                connection = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                showToast("brak polaczenia z internetem");
                Log.i("aaa", String.valueOf(e));
            }

        }else
        {
            connection = null;
            // showToast("Brak podłączenia do intrernetu");
        }

    }


    private void SelectDataUser()
    {
        try {
            podlaczenieDB();

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {

                PreparedStatement stmt1 = connection.prepareStatement("Select * from uzytkownik uzy where uzy.qr_code= '"+dane[2]+"' order by id desc");

                rs = stmt1.executeQuery();



                while (rs.next()) {
                    String zm = rs.getString("uzy.Id");

                    if (zm != null) {
                      //  Log.i("historiapojazd","tak"+ dane[5]);
                        zm3.add(rs.getString("uzy.email"));
                        zm4.add("Punkty: " + rs.getString("uzy.punkty"));
                        zm7.add(rs.getString("uzy.admin"));
                        s1=true;

                    }
                }

                PreparedStatement stmt2 = connection.prepareStatement("Select * from samochod sam where sam.qr_code = '"+dane[2]+"' and sam.wyswietl='1' order by id desc");

                rs = stmt2.executeQuery();



                while (rs.next()) {
                    String zm = rs.getString("sam.Id");

                    if (zm != null) {
                       // Log.i("historiapojazd","tak"+ dane[5]);
                        zm1.add(rs.getString("sam.Marka")+" "+  rs.getString("model"));
                        zm2.add(rs.getString("sam.nr_rejestracyjny"));
                        s2=true;

                    }

                }

                if(s2==false)
                {
                    showToast("Brak pojazdów do wyświetlenia");
                }

                PreparedStatement stmt3 = connection.prepareStatement("select * from samochod sam " +
                        "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                        "where sam.qr_code = '"+dane[2]+"' order by zgl.id desc ");

                rs = stmt3.executeQuery();



                while (rs.next()) {
                    String zm = rs.getString("zgl.Id");

                    if (zm != null) {
                        s3=true;
                        zm5.add(rs.getString("zgl.status"));
                        zm6.add(rs.getString("zgl.data_dod"));
                        zm8.add(rs.getString("zgl.nr_rejestracyjny"));

                       // Blob blob = rs.getBlob("zdjecie_przed");
                        //zdjecie.add(rs.getBlob("zdjecie_przed"));

                        zdjecie.add( rs.getBlob("zdjecie_przed"));
                       // is = blob.getBinaryStream();
                        //imageView.setImageBitmap(BitmapFactory.decodeStream(is));

                        Log.i("mainmenu",""+String.valueOf(zdjecie.get(0)));


                    }

                }

                if(s3==false)
                {

                   showToast("Brak zgłoszeń do wyświetlenia");
                }


            } catch (SQLException e) {
                Log.i("New user",""+e);

            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                Log.i("New user",""+se);
                //  showToast("brak połączenia z internetem" +se);
            }


        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {

            dane[0] = getIntent().getStringExtra("email");
            dane[1] = getIntent().getStringExtra("admin");
            dane[2] = getIntent().getStringExtra("qr_code");


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

        SelectDataUser();

        Custom_row adapter=new Custom_row(MainMenu.this, zm3,zm4);
        list=(ListView)findViewById(R.id.konto);
        list.setAdapter(adapter);

        zgloszenie =(Button) findViewById(R.id.zgloszenie);
        samochod = (Button) findViewById(R.id.button7);
        tekst1 = (TextView) findViewById(R.id.textView2);
        tekst2 = (TextView) findViewById(R.id.textView);

        Custom_row_zgloszenie adapter1=new Custom_row_zgloszenie(this, zm8,zm6,zm5,zdjecie);
        list1=(ListView)findViewById(R.id.zgloszenia);
        list1.setAdapter(adapter1);

        Custom_row_pojazd adapter2=new Custom_row_pojazd(this, zm1,zm2);
        list1=(ListView)findViewById(R.id.pojazdy);
        list1.setAdapter(adapter2);

        tekst2.setText("Samochody("+String.valueOf(zm1.size())+")");
        tekst1.setText("Zgłoszenia("+String.valueOf(zm5.size())+")");

        try {

            if (zm7.get(0).equals("1")) {
                zgloszenie.setVisibility(View.VISIBLE);
                samochod.setVisibility(View.VISIBLE);

            }

        }catch (Exception e)
        {
            Log.i("mainmenu",""+e);
        }

        zgloszenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, Historia_pojazd.class);
                i.putExtra("menu","zgloszenie");
                i.putExtra("qr_code",dane[2]);
                startActivity(i);
            }
        });

        samochod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainMenu.this,dane_pojazd.class);
                i.putExtra("qr_code",dane[2]);
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
            i.putExtra("qr_code",dane[2]);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
