package com.example.strzala.e_ksiazaka1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class lista_pojazd extends AppCompatActivity {

    ListView lista_napraw;
    TextView tekst;
    String kategoria="",pozycja="",nazwa="",ekran="",rejestracyjny="";
    String qrcode="";
    Integer liczba;
    String dane4[] = new String[10];

    private static final String SAMPLE_DB_NAME = "Baza";
    SQLiteDatabase sampleDB;

   // public int polaczenie=0;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    ArrayList<String> dane = new ArrayList<String>();
    ArrayList<String> dane1 = new ArrayList<String>();
    ArrayList<String> dane2 = new ArrayList<String>();


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
        dane.clear();
        dane1.clear();
        dane2.clear();

            try {
                sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

                Cursor c = sampleDB.rawQuery("Select * from kategoria where kat_1 != '0' and aktywne='1' or kat_2 !='0' ", null);
                int i = 0;
                while (c.moveToNext()) {
                    String pamiec = String.valueOf(c.getString(1));
                    if (pamiec != null) {

                        dane1.add(String.valueOf(c.getString(3))); //kategoria1
                        dane2.add(String.valueOf(c.getString(4))); //kategoria2

                        if (kategoria.equals("kat_1") & !dane1.get(i).equals("0")) {
                            dane.add(String.valueOf(c.getString(1))); //nazwa
                        }

                        if (kategoria.equals("kat_2") & dane2.get(i).equals(pozycja)) {
                            dane.add(String.valueOf(c.getString(1))); //nazwa
                            // Log.i("lista_pojazd",String.valueOf(dane));
                        }

                        i++;
                    } else {

                        podlaczenieDB();

                        if (connection != null) {

                            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

                            try {
                                st = connection.createStatement();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                                Log.i("myTag", "1" + e1);
                            }

                            try {


                                PreparedStatement stmt2 = connection.prepareStatement("select * from kategoria ");
                                rs = stmt2.executeQuery();

                                sampleDB.execSQL("Delete from kategoria ");

                                while (rs.next()) {
                                    String zm = rs.getString("nazwa");

                                    if (zm != null) {
                                        dane4[0] = rs.getString("nazwa");
                                        dane4[1] = rs.getString("punkty");
                                        dane4[2] = rs.getString("kat_1");
                                        dane4[3] = rs.getString("kat_2");
                                        dane4[4] = rs.getString("aktywne");

                                        sampleDB.execSQL("INSERT INTO kategoria (nazwa,punkty,kat_1,kat_2,aktywne) " +
                                                "VALUES ('" + dane4[0] + "','" + dane4[1] + "','" + dane4[2] + "'," +
                                                "'" + dane4[3] + "','" + dane4[4] + "') ");
                                    }

                                }

                            } catch (Exception e) {
                                Log.i("lista pojazd", "" + e);
                            }
                        }
                        sampleDB.close();
                    }
                }
            } catch (Exception e) {
                Log.i("baza", "" + e);
            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pojazd);

        lista_napraw = (ListView) findViewById(R.id.kategoria_naprawy);
        tekst = (TextView) findViewById(R.id.tekst);


        try {

            pozycja = getIntent().getStringExtra("pozycja");
            nazwa = getIntent().getStringExtra("nazwa");
            kategoria = getIntent().getStringExtra("kategoria");
            rejestracyjny = getIntent().getStringExtra("rejestracyjny");
            qrcode = getIntent().getStringExtra("qr_code");
            liczba = Integer.parseInt(pozycja);
            Log.i("pozycja",pozycja);
            Log.i("qrcode",qrcode);

        }catch (Exception e)
        {
            Log.i("listapojazd",""+e);
        }

            SelectDataUser();

            if (!dane.get(0).equals("") & kategoria.equals("kat_1")) {

                tekst.setText("Kategorie naprawy:");

                Custom_row_kategoriaNaprawy adapter2 = new Custom_row_kategoriaNaprawy(this, dane);
                lista_napraw.setAdapter(adapter2);

            } else if (!dane.get(0).equals("") & kategoria.equals("kat_2"))
            {
                tekst.setText("Kategoria naprawy: "+ nazwa);

                Custom_row_kategoriaNaprawy adapter2 = new Custom_row_kategoriaNaprawy(this, dane);
                lista_napraw.setAdapter(adapter2);

                ekran = "koniec";
            }



        lista_napraw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(ekran.equals("")) {
                    Intent i = new Intent(lista_pojazd.this, lista_pojazd.class);
                    pozycja= String.valueOf(position+1);
                    i.putExtra("pozycja", pozycja);
                    i.putExtra("nazwa", dane.get(position));
                    i.putExtra("ekran", ekran);
                    i.putExtra("kategoria","kat_2");
                    i.putExtra("qr_code",qrcode);
                    startActivity(i);
                }else if (ekran.equals("koniec"))
                {
                    Intent i = new Intent(lista_pojazd.this,koniec_pojazd.class);
                    i.putExtra("status","0");
                    i.putExtra("pozycja2",dane.get(position));
                    i.putExtra("qr_code",qrcode);
                    startActivity(i);
                }

            }
        });



    }
}
