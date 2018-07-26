package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class lista_pojazd extends AppCompatActivity {

    ListView lista_napraw;
    TextView tekst;
    String kategoria="",pozycja="",nazwa="",ekran="",nazwa2="";

    private static final String SAMPLE_DB_NAME = "Baza";
    SQLiteDatabase sampleDB;

    String dane[]=null;
    String dane1[]=null;
    String dane2[]=null;


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    private void SelectDataUser()
    {

        if(kategoria.equals("kat_1")) {
            try {

                sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

                Cursor c = sampleDB.rawQuery("Select * from kategoria where kat_1 != '0' and aktywne='1' ", null);
                int i = 0;
                while (c.moveToNext()) {
                    String pamiec = String.valueOf(c.getString(1));
                    if (pamiec != null) {
                        dane[i] = String.valueOf(c.getString(2)); //nazwa
                        dane1[i] = String.valueOf(c.getString(4)); //kategoria1
                        dane1[i] = String.valueOf(c.getString(5)); //kategoria2

                        Log.i("MainActivity", dane[i]);
                        i++;
                    } else {
                        //imporowanie kategori jeżeli ich nie ma w wewnętrznej bazie danych

                    }
                }

                sampleDB.close();

            } catch (Exception e) {
                Log.i("baza", "" + e);
            }
        }else if (kategoria.equals("kat_2"))
        {
            try {

                sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

                Cursor c = sampleDB.rawQuery("Select * from kategoria where kat_2 = '"+pozycja+1+"' and aktywne='1' ", null);
                int i = 0;
                while (c.moveToNext()) {
                    String pamiec = String.valueOf(c.getString(1));
                    if (pamiec != null) {
                        dane[i] = String.valueOf(c.getString(2)); //nazwa
                        dane1[i] = String.valueOf(c.getString(4)); //kategoria1
                        dane1[i] = String.valueOf(c.getString(5)); //kategoria2

                        Log.i("MainActivity", dane[i]);
                        i++;
                    } else {
                        //imporowanie kategori jeżeli ich nie ma w wewnętrznej bazie danych

                    }
                }

                sampleDB.close();

            } catch (Exception e) {
                Log.i("baza", "" + e);
            }
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

        }catch (Exception e)
        {
            Log.i("MainMenu",""+e);
        }

        if(pozycja==null) {
            tekst.setText("Kategorie naprawy");

            kategoria="kat_1";
            //pobieranie danych z sqlLight
            SelectDataUser();



            if (dane[0].equals("")) {

                ekran="start";

                Custom_row_kategoriaNaprawy adapter2 = new Custom_row_kategoriaNaprawy(this, dane);
                lista_napraw.setAdapter(adapter2);
            }
        }else if (pozycja!=null)
        {
            kategoria="kat_2";
            SelectDataUser();

            tekst.setText("Kategorie naprawy => "+nazwa);

            if (dane[0].equals("")) {
                //sprawdzanie czy juz druga kategoria
                ekran="koniec";
                Custom_row_kategoriaNaprawy adapter2 = new Custom_row_kategoriaNaprawy(this, dane);
                lista_napraw.setAdapter(adapter2);
            }
        }


        lista_napraw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(ekran.equals("start")) {
                    Intent i = new Intent(lista_pojazd.this, lista_pojazd.class);
                    i.putExtra("pozycja", position);
                    i.putExtra("nazwa", dane[position]);
                    i.putExtra("ekran", ekran);
                    startActivity(i);
                }else if (ekran.equals("koniec"))
                {
                    Intent i = new Intent(lista_pojazd.this,koniec_pojazd.class);
                    i.putExtra("pozycja1",nazwa);
                    i.putExtra("pozycja2",dane[position]);
                    startActivity(i);
                }

            }
        });



    }
}
