package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class dane_pojazd extends AppCompatActivity {

    EditText marka,model,rocznik,silnik,qrCode,nr_rejestracyjny;
    Button ok,powrót,skan;
    Boolean status=true;
    SQLiteDatabase sampleDB;
    private static final String SAMPLE_DB_NAME = "Baza";

    String dane[] = new String[10];


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }


    private void InsertCar()
    {
        try {
            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

            Cursor c = sampleDB.rawQuery("Select * from samochod where nr_rejestracyjny = '"+dane[5]+"' ",null);

            while(c.moveToNext())
            {
                String pamiec = String.valueOf(c.getString(1));
                if(pamiec != null)
                {
                    status = false;
                }else
                {
                    status = true;
                }
            }

            if(status==false) {
                sampleDB.execSQL("INSERT INTO samochod (marka,model,rocznik,silnik,nr_rejestracyjny,qrcode,wyswietl) values ('" + dane[0] + "','" + dane[1] + "','" + dane[2] + "','" + dane[3] + "','" + dane[4] + "') ");
            }else
            {
                showToast("Podane auto z takim nr rejestracyjnym już istnieje w systemie");
            }
            sampleDB.close();
        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pojazd);

        marka = (EditText) findViewById(R.id.marka);
        model = (EditText) findViewById(R.id.model);
        rocznik = (EditText) findViewById(R.id.rocznik);
        silnik = (EditText) findViewById(R.id.qr_code4);
        qrCode = (EditText) findViewById(R.id.qr_code);
        nr_rejestracyjny = (EditText) findViewById(R.id.rejestracyjny);

        ok = (Button) findViewById(R.id.zapisz_p);
        powrót = (Button) findViewById(R.id.powrot);
        skan = (Button) findViewById(R.id.skanuj);


        //Odbieranie danych z poprzedniego layoutu
        try {
            dane[0] = getIntent().getStringExtra("marka");
            dane[1] = getIntent().getStringExtra("model");
            dane[2] = getIntent().getStringExtra("rocznik");
            dane[3] = getIntent().getStringExtra("silnik");
            dane[4] = getIntent().getStringExtra("qrCode");
            dane[5] = getIntent().getStringExtra("rejestracja");
            marka.setText(dane[0]);
            model.setText(dane[1]);
            rocznik.setText(dane[2]);
            silnik.setText(dane[3]);
            qrCode.setText(dane[4]);
            nr_rejestracyjny.setText(dane[5]);

        }catch (Exception e)
        {
            Log.i("BarCode",""+e);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    dane[0] = marka.getText().toString();
                    dane[1] = model.getText().toString();
                    dane[2] = rocznik.getText().toString();
                    dane[3] = silnik.getText().toString();
                    dane[4] = qrCode.getText().toString();
                    dane[5] = nr_rejestracyjny.getText().toString();

                if(!dane[0].equals("") ) {

                    if(!dane[5].equals("")) {

                        if(!dane[4].equals("")) {
                            //InsertCar();
                            Intent i = new Intent(dane_pojazd.this, lista_pojazd.class);
                            i.putExtra("rejestracyjny",dane[5]);
                            i.putExtra("kategoria","kat_1");
                            i.putExtra("qr_code",dane[4]);
                            startActivity(i);
                        }else
                        {
                            showToast("Zeskanul lub wpisz kod QR");
                        }
                    }else
                    {
                        showToast("uzupełnij numer rejestracyjny samochodu.");
                    }
                }else
                {
                    showToast("Uzupełnij Markę samochodu.");
                }


            }
        });

        skan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dane[0]= marka.getText().toString();
                dane[1]= model.getText().toString();
                dane[2]= rocznik.getText().toString();
                dane[3]= silnik.getText().toString();
                dane[4]= qrCode.getText().toString();
                dane[5] = nr_rejestracyjny.getText().toString();

                Intent i = new Intent(dane_pojazd.this, BarCodeScaner.class);
                i.putExtra("ekran","pojazd_dane");
                i.putExtra("marka",dane[0]);
                i.putExtra("model",dane[1]);
                i.putExtra("rocznik",dane[2]);
                i.putExtra("silnik",dane[3]);
                i.putExtra("qrCode",dane[4]);
                i.putExtra("rejestracyjny",dane[5]);
                startActivity(i);
            }
        });

        powrót.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dane_pojazd.this, MainMenu.class);
                startActivity(i);
            }
        });
    }
}
