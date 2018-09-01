package com.example.strzala.e_ksiazaka1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.StrictMode;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    Button logowanie,logowanie_qr,konto,przypmnienie_hasla;
    EditText email,haslo;
    CheckBox checkBox;
    String login,password,qr_code,hash,pass,pamiec,login_pamiec;
    String login_act,password_act;
    private static final String SAMPLE_DB_NAME = "Baza";
    SQLiteDatabase sampleDB;

    String subject = "";
    String message = "";
    public int polaczenie=0;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    ProgressBar simpleProgressBar = null;
    private Handler handler = new Handler();
    boolean StartLog=false;
    private static final int REQUEST_CAMERA = 1;

    String dane[] = new String[35];

    //konstruktor
    private static MainActivity instance;

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
            polaczenie = 1;

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                //   showToast("brak polaczenia z internetem");
                Log.i("aaa", String.valueOf(e));
                polaczenie = 0;
            }

            try {
                connection = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                showToast("brak polaczenia z internetem");
                Log.i("aaa", String.valueOf(e));
                polaczenie = 0;
            }

        }else
       {
           connection = null;
            showToast("Brak podłączenia do intrernetu");
       }

    }

    public void ImportLogin(String msg_login)
    {
        podlaczenieDB();


        if (connection != null) {
            password="";

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            try {
                PreparedStatement stmt = connection.prepareStatement("select * from uzytkownik where email='"+msg_login+"' ");
                rs = stmt.executeQuery();


                while (rs.next()) {
                    String zm = rs.getString("email");

                    if (zm != null) {
                        login = rs.getString("email");
                        password = rs.getString("haslo");
                        dane[3] = rs.getString("zapisz_haslo");
                        qr_code = rs.getString("qr_code");
                        //dane[6] = rs.getString("admin");
                        dane[6] = rs.getString("czy_zapis");
                        pass =   hash = "%02320%xwc48" + String.valueOf(password.hashCode());

                    }

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "3" + e1);
            }

            try {
                if (connection != null)
                connection.close();
            } catch (SQLException se) {
                Log.i("myTag", "4" + se);
                showToast("brak polaczenia z internetem");
            }

        }
    }

/*
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
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS kategoria (Id INTEGER PRIMARY KEY AUTOINCREMENT,nazwa VARCHAR," +
                    "punkty VARCHAR, kat_1 VARCHAR, kat_2 VARCHAR, aktywne VARCHAR)");


            Cursor c = sampleDB.rawQuery("Select * from uzytkownik where email = '"+login_act+"' ",null);

            while(c.moveToNext())
            {
                pamiec = String.valueOf(c.getString(1));
                if(pamiec != null)
                {
                    login = String.valueOf(c.getString(2));
                    password = String.valueOf(c.getString(3));
                    qr_code = String.valueOf(c.getString(5));
                    dane[6] = String.valueOf(c.getString(8));
                    Log.i("MainActivity",login);
                }else
                {
                    //imporowanie loginów jeżeli ich nie ma w wewnętrznej bazie danych
                    ImportLogin(login_act);
                }
            }

            sampleDB.close();

        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }
    }
*/
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

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        //declare buttton and edittext
        konto = (Button) findViewById(R.id.konto);
        przypmnienie_hasla = (Button) findViewById(R.id.przypomnienie_hasla);
        logowanie = (Button) findViewById(R.id.logowanie);
        logowanie_qr = (Button) findViewById(R.id.logowanie_qr);

        email = (EditText) findViewById(R.id.login);
        haslo = (EditText) findViewById(R.id.password);

        checkBox = (CheckBox) findViewById(R.id.checkBoks);

        //inicjalizacja progresbaru
        simpleProgressBar=(ProgressBar) findViewById(R.id.progressBar);

        sprawdz_haslo();

        //sprawdzanie uprawnien do robienia zdjecia i zapisu
        verifyStoragePermissions(MainActivity.this);

        if(checkPermission()==false) {
            requestPermission();
        }

        //logowanie przy użyciu danych logowania
        logowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activeNetwork()) {
                simpleProgressBar.setVisibility(View.VISIBLE);
                StartLog = true;
                }
                 else {
                    showToast("Brak dostępu do internetu");
                }

            }
        });

        //logowanie przy użyciu QR kode
        logowanie_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeNetwork()) {
                Intent i = new Intent(MainActivity.this,BarCodeScaner.class);
                i.putExtra("ekran","logowanie");
                startActivity(i);
                }
                else {
                    showToast("Brak dostępu do internetu");
                }
            }
        });

        //wysyłanie maila z hasłem
        przypmnienie_hasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeNetwork()) {
                login_act = email.getText().toString();

                ImportLogin(login_act);

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
                else {
                    showToast("Brak dostępu do internetu");
                }

            }
        });



        //Tworzenie nowego użytkownika
        konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeNetwork()) {
                Intent i = new Intent(MainActivity.this,New_user.class);
                i.putExtra("menu","");
                i.putExtra("qr_code","");
                startActivity(i);
            }
                else {
                showToast("Brak dostępu do internetu");
            }
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

        handler = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {

                if(StartLog)
                {
                    login_act = email.getText().toString();
                    password_act = haslo.getText().toString();

                    login_act = login_act.replace(" ","");


                    ImportLogin(login_act);
                    hash();

                    if (login_act.equals(login) & login_act.contains("@")) {
                        if (hash.equals(password) || (password.equals(password_act)) || (pass.equals(password_act))) {

                            //sprawdzanie czy mam zapamiętać hasło
                            if(checkBox.isChecked())
                            {
                                pamiec_hasla();
                            }

                            //importowanie danych
                           // ImportLogin(login_act);

                            Intent c = new Intent(MainActivity.this,MainMenu.class);
                            c.putExtra("email",login);
                            c.putExtra("admin",dane[6]);
                            c.putExtra("qr_code",qr_code);
                            startActivity(c);
                            finish();
                        }else
                        {
                            showToast("Nie poprawne hasło ");
                        }

                    }else
                    {
                        showToast("Nie poprawny email ");
                    }

                    simpleProgressBar.setVisibility(View.INVISIBLE);
                    StartLog=false;
                }


              //  simpleProgressBar.setProgress(suma);
                handler.sendEmptyMessageDelayed(0, 100);
            }
        };

        handler.sendEmptyMessage(0);
    }
}
