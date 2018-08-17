package com.example.strzala.e_ksiazaka1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dane_pojazd extends AppCompatActivity {

    EditText marka,model,rocznik,silnik,qrCode,nr_rejestracyjny;
    Button ok,powrót,skan;
    Boolean status = false;

    String dane[] = new String[10];

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    public void showToast(String message) {
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

    public boolean activeNetwork () {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;

    }

    private void InsertCar()
    {
        try {
                podlaczenieDB();

                try {
                    st = connection.createStatement();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {

                    PreparedStatement stmt1 = connection.prepareStatement("select * from samochod where nr_rejestracyjny = '"+dane[5]+"' ");
                    rs = stmt1.executeQuery();



                    while (rs.next()) {
                        String zm = rs.getString("nr_rejestracyjny");

                        if (zm != null) {
                            Log.i("danepojazd","tak"+ dane[5]);
                            status=true;


                        } else  {
                            Log.i("danepojazd","nie"+ dane[5]);
                            status=false;
                        }

                    }

                        if (status==false)
                        {

                            String sql1 = "INSERT INTO samochod (marka,model,rocznik,silnik,nr_rejestracyjny,qr_code,wyswietl) " +
                                    " VALUES (?,?,?,?,?,?,?) ";
                            ps = connection.prepareStatement(sql1);
                            ps.setString(1, dane[0]);
                            ps.setString(2, dane[1]);
                            ps.setString(3, dane[2]);
                            ps.setString(4, dane[3]);
                            ps.setString(5, dane[5]);
                            ps.setString(6, dane[4]);
                            ps.setString(7, "1");
                            ps.executeUpdate();}
                            else
                        {
                            showToast("Podane auto z nr rejestracyjnym już istnieje w systemie");
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
        setContentView(R.layout.activity_pojazd);

        marka = (EditText) findViewById(R.id.email_lista);
        model = (EditText) findViewById(R.id.email_lista);
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
            dane[4] = getIntent().getStringExtra("qr_code");
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

        //testowo póżniej do usnięcia
       // qrCode.setText("https://vicards.pl/kuHSFb3r/test");

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

                            InsertCar();
                            if (status == false) {


                                Intent i = new Intent(dane_pojazd.this, lista_pojazd.class);
                                i.putExtra("rejestracyjny", dane[5]);
                                i.putExtra("kategoria", "kat_1");
                                i.putExtra("qr_code", dane[4]);
                                i.putExtra("liczba", "");
                                i.putExtra("pozycja", "");
                                i.putExtra("nazwa", "");
                                i.putExtra("admin", "1");
                                startActivity(i);

                            }
                        }else
                        {
                            showToast("Zeskanuj QR kod użytkownika");
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
                i.putExtra("qr_code",dane[4]);
                startActivity(i);
            }
        });
    }
}
