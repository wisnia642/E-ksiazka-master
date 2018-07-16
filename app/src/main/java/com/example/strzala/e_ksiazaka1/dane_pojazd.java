package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class dane_pojazd extends AppCompatActivity {

    EditText marka,model,rocznik,silnik,qrCode;
    Button ok,powrót,skan;
    SQLiteDatabase sampleDB;
    private static final String SAMPLE_DB_NAME = "Baza";


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }


    private void InsertCar()
    {
        try {
            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS samochod (Id INTEGER PRIMARY KEY AUTOINCREMENT, marka,VARCHAR, model VARCHAR, rocznik VARCHAR, silnik VARCHAR, qrcode VARCHAR)");
            sampleDB.execSQL("INSERT INTO samochod (marka,model,rocznik,silnik,qrcode) values ('"+dane[0]+"','"+dane[1]+"','"+dane[2]+"','"+dane[3]+"','"+dane[4]+"') ");
            sampleDB.close();
        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }
    }


    String dane[] = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pojazd);

        marka = (EditText) findViewById(R.id.marka);
        model = (EditText) findViewById(R.id.model);
        rocznik = (EditText) findViewById(R.id.rocznik);
        silnik = (EditText) findViewById(R.id.silnik);
        qrCode = (EditText) findViewById(R.id.qr_code);

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
            marka.setText(dane[0]);
            model.setText(dane[1]);
            rocznik.setText(dane[2]);
            silnik.setText(dane[3]);
            qrCode.setText(dane[4]);
        }catch (Exception e)
        {
            Log.i("BarCode",""+e);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dane[0]= marka.getText().toString();
                dane[1]= model.getText().toString();
                dane[2]= rocznik.getText().toString();
                dane[3]= silnik.getText().toString();
                dane[4]= qrCode.getText().toString();

                if(!dane[0].equals("") & !dane[4].equals("")) {
                    InsertCar();
                    Intent i = new Intent(dane_pojazd.this,lista_pojazd.class);
                    startActivity(i);
                }else
                {
                    showToast("Uzupełnij wszystkie dane");
                }


            }
        });

        skan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dane_pojazd.this, BarCodeScaner.class);
                i.putExtra("ekran","pojazd_dane");
                i.putExtra("marka",dane[0]);
                i.putExtra("model",dane[1]);
                i.putExtra("rocznik",dane[2]);
                i.putExtra("silnik",dane[3]);
                i.putExtra("qrCode",dane[4]);
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
