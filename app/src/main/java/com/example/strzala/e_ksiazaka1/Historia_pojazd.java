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
import java.sql.Blob;
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
    String tekst,email;
    EditText szukaj2;

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
    ArrayList<String> qrcode_tab = new ArrayList<String>();
    ArrayList<String> zm6 = new ArrayList<String>();
    ArrayList<String> zm5 = new ArrayList<String>();
    ArrayList<String> zm8 = new ArrayList<String>();
    ArrayList<String> zm4 = new ArrayList<String>();
    ArrayList<Blob> zdjecie = new ArrayList<Blob>();

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

                if(dane[3].equals("0")) {
                    PreparedStatement stmt1 = connection.prepareStatement("Select * from samochod where qr_code = '" + dane[1] + "' and wyswietl='1'  ");
                    rs = stmt1.executeQuery();
                }else if(dane[3].equals("1"))
                {
                    PreparedStatement stmt1 = connection.prepareStatement("Select * from samochod ");
                    rs = stmt1.executeQuery();
                }



                while (rs.next()) {
                    String zm = rs.getString("nr_rejestracyjny");

                    if (zm != null) {
                        marka_a.add(rs.getString("Marka")+" "+  rs.getString("model"));
                        nr_rejestracyjny_a.add(rs.getString("nr_rejestracyjny"));
                        qrcode_tab.add(rs.getString("qr_code"));
                        Log.i("historiapojazd","nie"+ marka_a.get(0));
                       status=true;

                    } else  {
                        Log.i("historiapojazd","nie"+ dane[1]);
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

    private void SelectDataUserSkan()
    {
        try {
            podlaczenieDB();

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {

                //czyszczenie tablic
                marka_a.clear();
                nr_rejestracyjny_a.clear();
                zm5.clear();
                zm6.clear();
                zm8.clear();
                zdjecie.clear();
                qrcode_tab.clear();

                if(dane[0].equals("zgloszenie_1")) {
                    PreparedStatement stmt2 = connection.prepareStatement("select * from samochod sam " +
                            "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                            "where sam.qr_code = '" + dane[1] + "' and sam.nr_rejestracyjny = '" + dane[2] + "' order by zgl.id desc ");
                    rs = stmt2.executeQuery();
                } else if(dane[0].equals("historia"))
                {
                    PreparedStatement stmt2 = connection.prepareStatement("select * from samochod sam " +
                            "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                            "where sam.qr_code = '" + dane[1] + "' and sam.nr_rejestracyjny like '%" + dane[2] + "%' order by zgl.id desc ");
                    rs = stmt2.executeQuery();
                }

                while (rs.next()) {
                    String zm = rs.getString("sam.nr_rejestracyjny");

                    if (zm != null) {
                        marka_a.add(rs.getString("sam.marka")+" "+  rs.getString("sam.model"));
                        nr_rejestracyjny_a.add(rs.getString("sam.nr_rejestracyjny"));
                        zm4.add(rs.getString("zgl.Id"));
                        zm5.add(rs.getString("zgl.status"));
                        zm6.add(rs.getString("zgl.data_dod"));
                        zm8.add(rs.getString("zgl.nr_rejestracyjny"));
                        zdjecie.add( rs.getBlob("zdjecie_przed"));
                        status=true;

                    } else  {
                        Log.i("historiapojazd","nie"+ dane[2]);
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

    public void adapter_Zgloszenie()
    {
        Custom_row_zgloszenie adapter1=new Custom_row_zgloszenie(this, zm8,zm6,zm5,zdjecie);
        lista_new.setAdapter(adapter1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_pojazd);

        try {

            dane[0] = getIntent().getStringExtra("menu");
            dane[1] = getIntent().getStringExtra("qr_code");
            dane[3] = getIntent().getStringExtra("admin");
            dane[4] = getIntent().getStringExtra("rejestracyjny");

        }catch (Exception e)
        {
            Log.i("MainMenu",""+e);
        }

        szukaj1 = (Button) findViewById(R.id.b_szukaj);
        szukaj2 = (EditText) findViewById(R.id.szukaj);
        powrót = (Button) findViewById(R.id.b_powrot);
        pole = (TextView) findViewById(R.id.tekst);

        SelectDataUser();

        if(dane[0].equals("historia")) {
            pole.setText("Historia pojazdów");
        }else if (dane[0].equals("zgloszenie"))
        {
            pole.setText("Dodaj zgłoszenie");
        }else if (dane[0].equals("menu_zgloszenie"))
        {
            dane[0]="zgloszenie_1";
            dane[2]=dane[4];
            SelectDataUserSkan();
            adapter_Zgloszenie();

        }

        Custom_row_pojazd adapter2=new Custom_row_pojazd(Historia_pojazd.this, marka_a,nr_rejestracyjny_a);
        lista_new = (ListView) findViewById(R.id.lista_new);
        lista_new.setAdapter(adapter2);

        lista_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(dane[0].equals("zgloszenie"))
                {
                    Intent i = new Intent (Historia_pojazd.this, lista_pojazd.class);
                    i.putExtra("rejestracyjny",nr_rejestracyjny_a.get(position));
                    i.putExtra("kategoria","kat_1");
                    i.putExtra("qr_code",qrcode_tab.get(position));
                    i.putExtra("liczba","");
                    i.putExtra("pozycja","");
                    i.putExtra("nazwa","");
                    startActivity(i);
                }
                //przejcie z widoku zgłoszenia
                else if(dane[0].equals("historia"))
                {
                    dane[0]="zgloszenie_1";
                    dane[2]=nr_rejestracyjny_a.get(position);
                    SelectDataUserSkan();
                    adapter_Zgloszenie();

                }
                //przejscie do edycji zgloszenia
                else if(dane[0].equals("zgloszenie_1"))
                {
                    Intent i = new Intent(Historia_pojazd.this, koniec_pojazd.class);
                    i.putExtra("menu","edit");
                    i.putExtra("qr_code",dane[1]);
                    i.putExtra("status","1");
                    i.putExtra("admin",dane[3]);
                    i.putExtra("rejestracyjny",zm8.get(position));
                    i.putExtra("pozycja2",zm4.get(position));
                    startActivity(i);
                }
            }
        });

        szukaj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dane[2]=szukaj2.getText().toString();
                marka_a.clear();
                nr_rejestracyjny_a.clear();

                SelectDataUserSkan();

                Custom_row_pojazd adapter2=new Custom_row_pojazd(Historia_pojazd.this, marka_a,nr_rejestracyjny_a);
                lista_new.setAdapter(adapter2);

                showToast("Wyszukano pojazdy z rejestracją: "+dane[2]);
            }
        });

        powrót.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Historia_pojazd.this,MainMenu.class);
                i.putExtra("email",email);
                i.putExtra("qr_code",dane[1]);
                startActivity(i);
            }
        });

    }
}
