package com.example.strzala.e_ksiazaka1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class dane_pojazd extends AppCompatActivity {

    EditText marka,model,rocznik,silnik,qrCode,nr_rejestracyjny,vin;
    Button ok,powrót,skan;
    Boolean status = false;
    TextView napis;
    String email="";

    String dane[] = new String[11];

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;
    String status_rejestracji="";

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

    public void update_uzytkownik()
    {
        podlaczenieDB();

        if (connection != null) {


            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }

            String sql2 = "UPDATE uzytkownik SET imie = '"+dane[0]+"',nazwisko = '"+dane[2]+"'," +
                    "adres = '"+dane[3]+"',dane1 = '"+dane[8]+"',dane2 = '"+dane[5]+"'" +
                    " WHERE email = '" + dane[1] + "'";
            try {
                st.executeUpdate(sql2);
            } catch (SQLException e) {
                e.printStackTrace(); }

        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException se) {
            Log.i("New user",""+se);
            //  showToast("brak połączenia z internetem" +se);
        }

    }

    private void InsertCar()
    {
        try {
                podlaczenieDB();
                status_rejestracji="";
                try {
                    st = connection.createStatement();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {

                    PreparedStatement stmt1 = connection.prepareStatement("select * from samochod where nr_rejestracyjny = '"+dane[5]+"' ");
                    rs = stmt1.executeQuery();



                    while (rs.next()) {
                         status_rejestracji = rs.getString("nr_rejestracyjny");

                        if (status_rejestracji != null) {
                            Log.i("danepojazd","tak"+ status_rejestracji);
                            status=true;


                        } else  {
                            Log.i("danepojazd","nie"+ status_rejestracji);
                            status=false;
                        }

                    }

                        if (status_rejestracji.equals(""))
                        {

                            String sql1 = "INSERT INTO samochod (marka,model,rocznik,silnik,nr_rejestracyjny,qr_code,wyswietl,vin) " +
                                    " VALUES (?,?,?,?,?,?,?,?) ";
                            ps = connection.prepareStatement(sql1);
                            ps.setString(1, dane[0]);
                            ps.setString(2, dane[1]);
                            ps.setString(3, dane[2]);
                            ps.setString(4, dane[3]);
                            ps.setString(5, dane[5]);
                            ps.setString(6, dane[6]);
                            ps.setString(7, "1");
                            ps.setString(8, dane[8]);
                            ps.executeUpdate();
                        }else if(status_rejestracji.equals(dane[5]))
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

    //sprawdzam czy dany qr_code jest w bazie danych po zeskanowaniu ponownie
    private void SelectUser(String tekst)
    {
        podlaczenieDB();
        dane[7]="";
        if (connection != null) {

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

            try {
                PreparedStatement stmt1 = connection.prepareStatement("select * from uzytkownik where qr_code like '%"+tekst+"%' ");
                rs = stmt1.executeQuery();

                if(tekst!=null) {
                    while (rs.next()) {
                        String zm = rs.getString("qr_code");

                        if (zm != null) {
                            dane[7] = rs.getString("qr_code");
                           // dane[2] = rs.getString("email");

                        }
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

    private void Datauser()
    {
        podlaczenieDB();


        if (connection != null) {
            dane[0]="";
            dane[2]="";
            dane[3]="";
            dane[8]="";
            dane[5]="";
            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
                Log.i("myTag", "1" + e1);
            }

           // String sql2 = "UPDATE uzytkownik SET imie = '"+dane[0]+"',nazwisko = '"+dane[2]+"'," +
           //         "adres = '"+dane[3]+"',dane1 = '"+dane[8]+"',dane2 = '"+dane[5]+"'" +
           //         " WHERE qr_code = '" + dane[4] + "'";

            try {
                PreparedStatement stmt1 = connection.prepareStatement("select * from uzytkownik where email = '"+dane[1]+"' ");
                rs = stmt1.executeQuery();

                    while (rs.next()) {
                        String zm = rs.getString("email");

                        if (zm != null) {
                            dane[0] = rs.getString("imie");
                            dane[2] = rs.getString("nazwisko");
                            dane[3] = rs.getString("adres");
                            dane[8] = rs.getString("dane1");
                            dane[5] = rs.getString("dane2");
                            email = rs.getString("email");
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
        vin = (EditText) findViewById(R.id.numer_vin);
        napis = (TextView) findViewById(R.id.textView8);

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
            dane[6] = getIntent().getStringExtra("qr_code_kod");
            dane[8] = getIntent().getStringExtra("vin");
            dane[9] = getIntent().getStringExtra("menu");
            dane[10] = getIntent().getStringExtra("admin");


            if(dane[9]==null) {
                marka.setHint("Marka");
                model.setHint("Model");
                rocznik.setHint("Rocznik");
                silnik.setHint("Silnik");
                nr_rejestracyjny.setHint("Nr. Rejestracyjny");
                qrCode.setHint("Qr code");
                vin.setHint("Nr. Vin");
                napis.setText("Dodanie nowego pojazdu");

                marka.setText(dane[0]);
                model.setText(dane[1]);
                rocznik.setText(dane[2]);
                silnik.setText(dane[3]);
                vin.setText(dane[8]);
                if(dane[6].contains("https://vicards.pl/")) {
                    String new_result = dane[6].replace("https://vicards.pl/", "");
                    qrCode.setText(new_result);
                }else
                {
                    qrCode.setText(dane[6]);
                }
                nr_rejestracyjny.setText(dane[5]);

            }else
            {
                marka.setHint("Imię");
                rocznik.setHint("Nazwisko");
                model.setHint("Email");
                silnik.setHint("Adres");
                vin.setHint("Telefon");
                nr_rejestracyjny.setHint("Inne");
                vin.setInputType(InputType.TYPE_CLASS_NUMBER);
                Datauser();
                //importowanie danych o kliencie
                skan.setVisibility(View.INVISIBLE);
                qrCode.setVisibility(View.INVISIBLE);
                napis.setText("Dane klienta");
                model.setText(email);

                if(dane[0]==null) {
                    marka.setHint("Imię");
                }else { marka.setText(dane[0]);}
                if(dane[2]==null) {
                    rocznik.setHint("Nazwisko");
                }else { rocznik.setText(dane[2]);}
                if(dane[5]==null) {
                    silnik.setHint("Adres");
                }else { silnik.setText(dane[5]);}
                if(dane[8]==null) {
                    vin.setHint("Telefon");
                }else { vin.setText(dane[8]);}
                if(dane[3]==null)
                { nr_rejestracyjny.setHint("Inne");
                }else { nr_rejestracyjny.setText(dane[3]);}

                ok.setText("Zapisz");
            }


        }catch (Exception e)
        {
            Log.i("BarCode",""+e);
        }

        vin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dane[8]!=null & dane[10].equals("1")) {
                    try {
                        dane[8] = vin.getText().toString();
                        Uri number = Uri.parse("tel:" + dane[8]);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        startActivity(callIntent);
                    }catch (Exception e)
                    {
                        Log.i("dane_pojazd",""+e);
                    }
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(dane[9]==null) {

                    dane[0] = marka.getText().toString();
                    dane[1] = model.getText().toString();
                    dane[2] = rocznik.getText().toString();
                    dane[3] = silnik.getText().toString();
                    dane[5] = nr_rejestracyjny.getText().toString();
                    dane[8] = vin.getText().toString();
                    SelectUser(dane[6]);
                    if (!dane[0].equals("")) {

                        if (!dane[5].equals("")) {

                            if (!dane[7].equals("")) {

                                InsertCar();
                                if (status_rejestracji.equals("")) {

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
                            } else {
                                showToast("Brak użytkownika z przypisanym kodem QR");
                            }
                        } else {
                            showToast("uzupełnij numer rejestracyjny samochodu.");
                        }
                    } else {
                        showToast("Uzupełnij Markę samochodu.");
                    }
                }else
                {

                    dane[0] = marka.getText().toString();
                    dane[1] = model.getText().toString();
                    dane[2] = rocznik.getText().toString();
                    dane[3] = silnik.getText().toString();
                    dane[5] = nr_rejestracyjny.getText().toString();
                    dane[8] = vin.getText().toString();
                    //SelectUser(dane[4]);
                    update_uzytkownik();
                    showToast("Dane zostały zapisane");
                    Intent i = new Intent(dane_pojazd.this, MainMenu.class);
                    i.putExtra("qr_code",dane[4]);
                    i.putExtra("admin",'1');
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                    startActivity(i);
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
                dane[5] = nr_rejestracyjny.getText().toString();
                dane[8] = vin.getText().toString();

                Intent i = new Intent(dane_pojazd.this, BarCodeScaner.class);
                i.putExtra("ekran","pojazd_dane");
                i.putExtra("marka",dane[0]);
                i.putExtra("model",dane[1]);
                i.putExtra("rocznik",dane[2]);
                i.putExtra("silnik",dane[3]);
                i.putExtra("qr_code",dane[4]);
                i.putExtra("admin",'1');
                i.putExtra("rejestracja",dane[5]);
                i.putExtra("vin",dane[8]);
                startActivity(i);
            }
        });

        powrót.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dane_pojazd.this, MainMenu.class);
                i.putExtra("qr_code",dane[4]);
                i.putExtra("admin",'1');
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                startActivity(i);
            }
        });
    }
}
