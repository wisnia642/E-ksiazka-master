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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Historia_pojazd extends AppCompatActivity {

    Button szukaj1,powrót;
    TextView pole;
    ListView lista_new=null;
    String zm2[] = new String[6];
    String tekst,email;

    String dane[] = new String[10];
    Boolean status=false;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    FileOutputStream fos =null;
    Connection connection = null;
    InputStream is;

    ArrayList<String> marka_a = new ArrayList<String>();
    ArrayList<String> nr_rejestracyjny_a = new ArrayList<String>();

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
        try {
            podlaczenieDB();

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {

                PreparedStatement stmt1 = connection.prepareStatement("sSelect * from samochod where qr_code = '"+dane[1]+"' and wyswietl='1'  ");
                rs = stmt1.executeQuery();



                while (rs.next()) {
                    String zm = rs.getString("nr_rejestracyjny");

                    if (zm != null) {
                        Log.i("historiapojazd","tak"+ dane[5]);
                        marka_a.add(rs.getString("Marka")+" "+  rs.getString("model"));
                        nr_rejestracyjny_a.add(rs.getString("nr_rejestracyjny"));

                    } else  {
                        Log.i("historiapojazd","nie"+ dane[5]);
                        status=false;
                    }

                }

                if(status==false)
                {
                    showToast("Brak pojazdów do wyświetlenia");
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
        setContentView(R.layout.activity_historia_pojazd);

        try {

            dane[0] = getIntent().getStringExtra("menu");
            dane[1] = getIntent().getStringExtra("qr_code");

        }catch (Exception e)
        {
            Log.i("MainMenu",""+e);
        }

            if(dane[0].equals("historia")) {
               pole.setText("Historia pojazdów");
            }else if (dane[0].equals("zgloszenie"))
            {
                pole.setText("Dodaj zgłoszenie");
            }

        szukaj1 = (Button) findViewById(R.id.b_szukaj);
        powrót = (Button) findViewById(R.id.b_powrot);
        pole = (TextView) findViewById(R.id.tekst);



        SelectDataUser();

        Custom_row_pojazd adapter2=new Custom_row_pojazd(Historia_pojazd.this, marka_a,nr_rejestracyjny_a);
        lista_new = (ListView) findViewById(R.id.lista_new);
        lista_new.setAdapter(adapter2);

        lista_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(dane[0].equals("zgloszenie"))
                {
                    Intent i = new Intent (Historia_pojazd.this, lista_pojazd.class);
                    i.putExtra("rejestracyjny","");
                    i.putExtra("kategoria","kat_1");
                    i.putExtra("qr_code",dane[1]);
                    i.putExtra("liczba","");
                    i.putExtra("pozycja","");
                    i.putExtra("nazwa","");
                    startActivity(i);
                }
                if(dane[0].equals("historia"))
                {
                    Intent i = new Intent (Historia_pojazd.this, lista_pojazd.class);
                    startActivity(i);
                }
            }
        });

        szukaj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectDataUser();

                tekst=pole.getText().toString();
            }
        });

        powrót.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Historia_pojazd.this,MainMenu.class);
                i.putExtra("email",email);
                startActivity(i);
            }
        });

    }
}
