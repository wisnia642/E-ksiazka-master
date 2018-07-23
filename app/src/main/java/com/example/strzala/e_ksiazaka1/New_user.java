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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class New_user extends AppCompatActivity {

    public EditText email,haslo,haslo_pow,qrcode;
    public TextView rekulamin_akceptacja;
    public Button skan,ok,anuluj;
    public CheckBox checkBox;
    public boolean status =true;
    public String hash;

    String subject = "",data="",kod="";
    String message = "";

    SQLiteDatabase sampleDB;

    String dane[] = new String[8];

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    private static final String SAMPLE_DB_NAME = "Baza";


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
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

    public void InsertLoginDataMysql() {

        podlaczenieDB();

        if (connection != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy_HH:mm");
            data =sdf.format(new Date());

            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }

            String sql1 = "INSERT INTO uzytkownik (data_dod,email,haslo,zapisz_haslo,qr_code," +
                    "punkty,admin,czy_zapis) VALUES (?,?,?,?,?,?,?,?)";

            try {
                ps = connection.prepareStatement(sql1);
                ps.setString(1, data);
                ps.setString(2, dane[0]);
                ps.setString(3, dane[1]);
                ps.setString(4, "0");
                ps.setString(5, dane[3]);
                ps.setString(6, "0");
                ps.setString(7, "0");
                ps.setString(8, "0");
                ps.executeUpdate();


                String sql2 = "UPDATE qr_code SET aktywne = '0' WHERE kod = '" + dane[3] + "'";
                st.executeUpdate(sql2);

                sampleDB.execSQL("INSERT INTO uzytkownik (data_dod,email,haslo,zapisz_haslo,qr_code," +
                        "punkty,admin,czy_zapis) VALUES ('"+data+"','"+dane[0]+"','"+hash+"','0','"+dane[3]+"'," +
                        "'0','0','0') ");

            } catch (SQLException e) {
                Log.i("New user",""+e);
            }
            try {
                if (connection != null)
                    sampleDB.close();
                    connection.close();
            } catch (SQLException se) {
                Log.i("New user",""+se);
                //  showToast("brak połączenia z internetem" +se);
            }
        }
    }


    private void SelectDataUser()
    {
        MainActivity.getInstance().ImportLogin(dane[0]);

        try{

            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);

            Cursor c = sampleDB.rawQuery("Select * from uzytkownik where email = '"+dane[0]+"' ",null);

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

            sampleDB.close();

        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }
    }

    private void sendemail_execiut() {
        //Getting content for email

        dane[0] = email.getText().toString();

            message = "Witaj "+dane[0]+",\n" +
                    "\n"+
                    " Utworzyłeś konto w Trust Serwis Book, Od teraz będziesz \n" +
                    " mógł przeglądać historię napraw pojazdów w swoim telefonie.\n"+
                    "\n"+
                    " Pozdrawiam Zespół TrustCar. \n";
            subject = "Utworzenie konta Trust Serwis Book.";
            //Creating SendMail object

            try {
                SendEmail sm = new SendEmail(this, dane[0], subject, message);

                //Executing sendmail to send email
                sm.execute();
            } catch (Exception e) {
                Log.i("MainActivity", "" + e);
            }
        }

    public void SelectDataUser_qrcode()
    {
        podlaczenieDB();

        if (connection != null) {
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            try {
                PreparedStatement stmt1 = connection.prepareStatement("select * from qr_code where kod='"+dane[3]+"' and aktywne='1' ");
                rs = stmt1.executeQuery();


                while (rs.next()) {
                    String zm = rs.getString("kod");


                    if (zm != null) {
                         kod = rs.getString("kod");

                    }else
                    {
                        Log.i("New user",""+kod);
                        // MainActivity.getInstance().showToast("Podany kod jest nieprawidłowy lub został już aktywowany");


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

                SelectDataUser();
                SelectDataUser_qrcode();

                //sprawdzanie czy pole email jest uzupełnione
                if(dane[0].contains("@") ) {
                    if (status == true) {
                        if (!dane[1].equals("") & !dane[2].equals("")) {
                            if (dane[1].equals(dane[2])) {
                                if (!dane[3].equals("")) {
                                    Log.i("user1",kod);
                                    if(dane[3].equals(kod)) {
                                        if (checkBox.isChecked()) {
                                            dane[0].replace(" ", "");
                                            hash();

                                            //dodanie danych do dwoch baz
                                            InsertLoginDataMysql();

                                            if (activeNetwork()) {
                                                sendemail_execiut();
                                            } else {
                                                showToast("Brak dostępu do internetu");
                                            }
                                            Intent i = new Intent(New_user.this, MainMenu.class);
                                            i.putExtra("email", dane[0]);
                                            startActivity(i);
                                            showToast("Konto zostało utworzone");
                                        } else {
                                            showToast("Założenie konta wymaga potwierdzenia regulaminu serwisu");
                                        }
                                    }else
                                    {
                                        showToast("Nieprawidłowy lub nie aktywny kod QR");
                                    }

                                } else {
                                    showToast("Podaj kod zabezpieczający kodu QE");
                                }
                            } else {
                                showToast("Hasła nie są identyczne");
                            }

                        } else {
                            showToast("Uzupełnij hasło");
                        }


                    } else {
                        showToast("Email już istnieje");
                    }

                    }else {
                    showToast("Uzupełnij poprawnie email");
                     }

            }
        });

        rekulamin_akceptacja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.getInstance().activeNetwork()) {
                    dane[0] = email.getText().toString();
                    dane[1] = haslo.getText().toString();
                    dane[2] = haslo_pow.getText().toString();
                    dane[3] = qrcode.getText().toString();

                    Intent i = new Intent(New_user.this, Regulamin.class);
                    i.putExtra("ekran", "uzytkownik");
                    i.putExtra("email", dane[0]);
                    i.putExtra("haslo", dane[1]);
                    i.putExtra("haslo_pow", dane[2]);
                    i.putExtra("qrcode", dane[3]);
                    startActivity(i);
                }else
                {
                    showToast("Brak podłączenia do internetu");
                }

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
