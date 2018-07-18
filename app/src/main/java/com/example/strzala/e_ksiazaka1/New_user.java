package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class New_user extends AppCompatActivity {

    public EditText email,haslo,haslo_pow,qrcode;
    public TextView rekulamin_akceptacja;
    public Button skan,ok,anuluj;
    public CheckBox checkBox;
    public int status;
    public String hash;

    String dane[] = new String[8];

    private static final String SAMPLE_DB_NAME = "Baza";

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    public void InsertLoginDataSqligt() {

        try {
            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS uzytkownik (Id INTEGER PRIMARY KEY AUTOINCREMENT,data_dod VARCHAR," +
                    "email VARCHAR, haslo VARCHAR, zapisz_haslo VARCHAR, qr_code VARCHAR, punkty VARCHAR,admin VARCHAR)");
            sampleDB.execSQL("INSERT INTO uzytkownik (email,haslo,qr_code) VALUES ('"+dane[0]+"','"+hash+"','"+dane[3]+"') ");
            sampleDB.close();
        } catch (Exception e) {
            Log.i("new user", "" + e);
        }
    }

    private void hash()
    {
        try{

            hash = "%02320%xwc48" + String.valueOf(dane[1].hashCode());


        }catch (Exception e)
        {
            Log.i("hash",""+e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        email = (EditText) findViewById(R.id.marka);
        haslo = (EditText) findViewById(R.id.model);
        haslo_pow = (EditText) findViewById(R.id.silnik);
        qrcode = (EditText) findViewById(R.id.qr_code);

        rekulamin_akceptacja = (TextView) findViewById(R.id.regulamin);

        skan = (Button) findViewById(R.id.skanuj);
        ok = (Button) findViewById(R.id.zapisz_p);
        anuluj = (Button) findViewById(R.id.powrot);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

        try {

            dane[0] = getIntent().getStringExtra("email");
            dane[1] = getIntent().getStringExtra("haslo");
            dane[2] = getIntent().getStringExtra("haslo_pow");
            dane[3] = getIntent().getStringExtra("qrcode");

            email.setText(dane[0]);
            haslo.setText(dane[1]);
            haslo_pow.setText(dane[2]);
            qrcode.setText(dane[3]);
        }catch (Exception e)
        {
            Log.i("BarCode",""+e);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dane[0] = email.getText().toString();
                dane[1] = haslo.getText().toString();
                dane[2] = haslo_pow.getText().toString();
                dane[3] = qrcode.getText().toString();


                //sprawdzanie czy pole email jest uzupełnione
                if(dane[0].contains("@"))
                {
                    if(!dane[1].equals("") & !dane[2].equals(""))
                    {

                        if(dane[1].equals(dane[2]))
                        {
                             if (!dane[3].equals(""))
                             {
                                 if(checkBox.isChecked()) {
                                     hash();
                                     InsertLoginDataSqligt();
                                     Intent i = new Intent(New_user.this, MainMenu.class);
                                     i.putExtra("email", dane[0]);
                                     startActivity(i);
                                     showToast("Konto zostało utworzone");
                                 }else
                                 {
                                     showToast("Założenie konta wymaga potwierdzenia regulaminu serwisu");
                                 }

                            }else
                            {
                            showToast("Podaj kod zabezpieczający kodu QE");
                             }
                        }else
                        {
                            showToast("Hasła nie są identyczne");
                        }

                    }else
                    {
                        showToast("Uzupełnij hasło");
                    }


                }else
                {
                    showToast("Uzupełnij poprawnie email");
                }

            }
        });

        rekulamin_akceptacja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dane[0] = email.getText().toString();
                dane[1] = haslo.getText().toString();
                dane[2] = haslo_pow.getText().toString();
                dane[3] = qrcode.getText().toString();

                Intent i = new Intent(New_user.this,Regulamin.class);
                i.putExtra("ekran","uzytkownik");
                i.putExtra("email",dane[0]);
                i.putExtra("haslo",dane[1]);
                i.putExtra("haslo_pow",dane[2]);
                i.putExtra("qrcode",dane[3]);
                startActivity(i);
            }
        });

        anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(New_user.this,MainActivity.class);
                startActivity(i);
            }
        });

        skan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dane[0] = email.getText().toString();
                dane[1] = haslo.getText().toString();
                dane[2] = haslo_pow.getText().toString();
                dane[3] = qrcode.getText().toString();

                //przejscie do layoutu z odczytem kodu
                Intent i = new Intent(New_user.this, BarCodeScaner.class);
                i.putExtra("ekran","uzytkownik");
                i.putExtra("email",dane[0]);
                i.putExtra("haslo",dane[1]);
                i.putExtra("haslo_pow",dane[2]);
                i.putExtra("qrcode",dane[3]);
                startActivity(i);
            }
        });

    }
}
