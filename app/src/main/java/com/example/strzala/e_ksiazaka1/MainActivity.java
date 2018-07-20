package com.example.strzala.e_ksiazaka1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    Button logowanie,logowanie_qr,konto,przypmnienie_hasla;
    EditText email,haslo;
    CheckBox checkBox;
    String login,password,qr_code,hash,zapis_hasla,pamiec,login_pamiec;
    String login_act,password_act;
    private static final String SAMPLE_DB_NAME = "Baza";
    SQLiteDatabase sampleDB;

    String subject = "";
    String message = "";


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }



    private void hash()
    {
            if(!password_act.equals("%02320%xwc48")) {
                try {
                    hash = "%02320%xwc48" + String.valueOf(password_act.hashCode());


                } catch (Exception e) {
                    Log.i("hash", "" + e);
                }
            }


    }


    //select data from table user, tworzenie tabel jeśli nie istnieją
    private void SelectDataUser()
    {

        try{
            password="";
            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);

            //tworzenie tabeli uzytkownik jezeli nie istnieje
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS uzytkownik (Id INTEGER PRIMARY KEY AUTOINCREMENT,data_dod VARCHAR," +
                    " email VARCHAR, haslo VARCHAR, zapisz_haslo VARCHAR, qr_code VARCHAR, punkty VARCHAR,admin VARCHAR,czy_zapis VARCHAR)");
            //tworzenie tabeli samochod jezeli nie istnieje
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS samochod (Id INTEGER PRIMARY KEY AUTOINCREMENT,marka VARCHAR," +
                    "model VARCHAR, rocznik VARCHAR, silnik VARCHAR, nr_rejestracyjny VARCHAR, qr_code VARCHAR,wyswietl VARCHAR)");

            //tworzenie tabeli zgloszenie jezeli nie istnieje
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS zgloszenie (Id INTEGER PRIMARY KEY AUTOINCREMENT,zdjecie_przed BLOB," +
                    "zdjecie_po BLOB, cena_czesci VARCHAR, cena_uslugi VARCHAR, uwagi VARCHAR, data_dod VARCHAR,data_wykonania VARCHAR," +
                    "status VARCHAR, qr_code VARCHAR, akceptacja VARCHAR)");
            //tworzenie tabeli naprawy jezeli nie istnieje
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS kategorie (Id INTEGER PRIMARY KEY AUTOINCREMENT,nazwa VARCHAR," +
                    "punkty VARCHAR, kat_1 VARCHAR, kat_2 VARCHAR)");

            Cursor c = sampleDB.rawQuery("Select * from uzytkownik where email = '"+login_act+"' ",null);

            while(c.moveToNext())
            {
                pamiec = String.valueOf(c.getString(1));
                if(pamiec != null)
                {
                    login = String.valueOf(c.getString(2));
                    password = String.valueOf(c.getString(3));
                    qr_code = String.valueOf(c.getString(5));
                    Log.i("MainActivity",login);
                }
            }

            sampleDB.close();

        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }
    }

    private void pamiec_hasla()
    {
        if(login_pamiec.equals("")) {
            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS pamiec (Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email VARCHAR, haslo VARCHAR)");
            sampleDB.execSQL("Delete from pamiec");
            sampleDB.execSQL("Insert into pamiec (email,haslo) VALUES ('" + login + "','" + hash + "') ");
            sampleDB.close();
        }

    }

    private void nie_pamiec_hasla()
    {
        password="";
        hash="";
        pamiec="";
        login_pamiec="";
        sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS pamiec (Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email VARCHAR, haslo VARCHAR)");
        sampleDB.execSQL("Delete from pamiec");
        sampleDB.close();

        email.setText("");
        haslo.setText("");

    }

    //sprawdzanie czy uzytkownik zapamietał hasło
    private void sprawdz_haslo()
    {
        password="";
        login_pamiec="";

        sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS pamiec (Id INTEGER PRIMARY KEY AUTOINCREMENT,email VARCHAR, haslo VARCHAR)");
        Cursor d =sampleDB.rawQuery("Select * from pamiec",null);

        while(d.moveToNext())
        {
            String zm = String.valueOf(d.getString(1));
            if(zm != null) {
                login = String.valueOf(d.getString(1));
                password = String.valueOf(d.getString(2));
                login_pamiec=login;
            }

        }

        sampleDB.close();

        if(login!=null) {
            email.setText(login);
            haslo.setText(password);
            checkBox.setChecked(true);
        }

    }


    private void sendemail_execiut() {
        //Getting content for email

        login_act = email.getText().toString();
        if(login_act.equals(login)) {
            message = "Witaj "+login_act+",\n" +
                    " \n "+
                    "Otrzymałeś ten email ponieważ skorzystałeś z opcji przypomnienia hasła. \n" +
                    "Twoje hasło to: "+password+"  \n"+
                    " \n "+
                    "Pozdrawiam Zespół TrustCar. \n";
            subject = "Przypomnienie hasła TrustCar";
            //Creating SendMail object

            try {
                SendEmail sm = new SendEmail(this, login_act, subject, message);

                //Executing sendmail to send email
                sm.execute();
            } catch (Exception e) {
                Log.i("MainActivity", "" + e);
            }
        }
    }

    public boolean activeNetwork () {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare buttton and edittext
        konto = (Button) findViewById(R.id.konto);
        przypmnienie_hasla = (Button) findViewById(R.id.przypomnienie_hasla);
        logowanie = (Button) findViewById(R.id.logowanie);
        logowanie_qr = (Button) findViewById(R.id.logowanie_qr);

        email = (EditText) findViewById(R.id.login);
        haslo = (EditText) findViewById(R.id.password);

        checkBox = (CheckBox) findViewById(R.id.checkBoks);

        sprawdz_haslo();

        //logowanie przy użyciu danych logowania
        logowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                login_act = email.getText().toString();
                password_act = haslo.getText().toString();

                login_act = login_act.replace(" ","");


                SelectDataUser();
                    hash();

                    if (login_act.equals(login) & login_act.contains("@")) {
                        Log.i("password_1",password);
                        Log.i("pasword_asct_1",password_act);
                        if (hash.equals(password) || (password.equals(password_act))) {

                                //sprawdzanie czy mam zapamiętać hasło
                                if(checkBox.isChecked())
                                {
                                    pamiec_hasla();

                                }
                                Intent c = new Intent(MainActivity.this,MainMenu.class);
                                c.putExtra("email",login);
                                startActivity(c);
                                finish();
                                //Log.i("password",password);
                               // Log.i("pasword_asct",password_act);
                                //Log.i("hash",hash);
                                }else
                        {
                            showToast("Nie poprawne hasło" + password);
                        }

                        }else
                    {
                        showToast("Nie poprawny email ");
                    }

            }
        });

        //logowanie przy użyciu QR kode
        logowanie_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,BarCodeScaner.class);
                i.putExtra("ekran","logowanie");
                startActivity(i);
            }
        });

        //wysyłanie maila z hasłem
        przypmnienie_hasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_act = email.getText().toString();

                SelectDataUser();

                if(login_act.contains("@"))
                {
                if(login_act.equals(login))
                {
                    if(activeNetwork()) {
                        sendemail_execiut();
                    }else
                    {
                        showToast("Brak dostępu do internetu");
                    }

                }else
                {
                    showToast("Brak adresu email w bazie");
                }
                }else
                {
                    showToast("Nie poprawny adres email");
                }

            }
        });



        //Tworzenie nowego użytkownika
        konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,New_user.class);
                startActivity(i);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                login_act = email.getText().toString();
                if (isChecked & login_act!=null)
                {
                    //pamiec_hasla();
                }
                else
                {
                    nie_pamiec_hasla();
                }
            }
        });
    }
}
