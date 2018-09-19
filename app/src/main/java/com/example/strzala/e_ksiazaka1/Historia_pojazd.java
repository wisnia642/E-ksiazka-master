package com.example.strzala.e_ksiazaka1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Historia_pojazd extends AppCompatActivity {

    Button szukaj1,powrót;
    TextView pole,liczba;
    ListView lista_new=null;
    String tekst,email,zm,zm1;
    Integer pozycja, pozycja2;
    EditText szukaj2;
    CheckBox checkBox3;

    String dane[] = new String[10];
    Boolean status=false,delete=false,click=false,filtr=false,filtr2=false;

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
    ArrayList<String> aktywne = new ArrayList<String>();
    ArrayList<String> Id = new ArrayList<String>();
    ArrayList<String> zm6 = new ArrayList<String>();
    ArrayList<String> zm5 = new ArrayList<String>();
    ArrayList<String> zm8 = new ArrayList<String>();
    ArrayList<String> zm4 = new ArrayList<String>();
    ArrayList<Blob> zdjecie = new ArrayList<Blob>();

    //konstruktor
    private static Historia_pojazd instance;

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
             showToast("Brak podłączenia do intrernetu");
        }

    }

    public void update_konfiguracj(String nr_rejestracyjny, String status)
    {
        podlaczenieDB();

        if (connection != null) {


            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }


                if(checkBox3.isChecked()) {
                    String sql3 = "UPDATE uzytkownik SET czy_zapis = '1' WHERE qr_code = '" + dane[1] + "'";
                    try {
                        st.executeUpdate(sql3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else  if(!checkBox3.isChecked())
                {
                    String sql3 = "UPDATE uzytkownik SET czy_zapis = '0' WHERE qr_code = '" + dane[1] + "'";
                    try {
                        st.executeUpdate(sql3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

               // String sql2 = "UPDATE samochod SET wyswietl = '"+status+"' WHERE nr_rejestracyjny = '" + nr_rejestracyjny + "'";
             //  try {
               // st.executeUpdate(sql2);
               // } catch (SQLException e) {
             //  e.printStackTrace(); }

            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                Log.i("New user",""+se);
                //  showToast("brak połączenia z internetem" +se);
            }

    }


    private void SelectDataUser()
    {
        try {
            podlaczenieDB();
            zm=null;
            Id.clear();
            marka_a.clear();
            nr_rejestracyjny_a.clear();
            qrcode_tab.clear();
            aktywne.clear();

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {

                //pobieranie samochodów dla zywkłego użytkownika
                if (dane[3].equals("0") ) {
                    PreparedStatement stmt1 = connection.prepareStatement("Select * from samochod where qr_code = '" + dane[1] + "' and wyswietl='1'  ");
                    rs = stmt1.executeQuery();
                } if (dane[3].equals("0") & dane[0].equals("konfiguracja"))
                {
                    PreparedStatement stmt1 = connection.prepareStatement("Select * from samochod where qr_code = '" + dane[1] + "'   ");
                    rs = stmt1.executeQuery();

                    //powbieranie wszystkich samochodów dla admina
                }  if(dane[3].equals("0") & filtr==true)
                {
                    filtr=false;
                    PreparedStatement stmt1 = connection.prepareStatement("Select * from samochod where nr_rejestracyjny like  '%" + dane[2] + "%' and qr_code = '" + dane[1] + "'");
                    rs = stmt1.executeQuery();
                }
                 if(dane[3].equals("1") )
                {
                    PreparedStatement stmt1 = connection.prepareStatement("Select * from samochod ");
                    rs = stmt1.executeQuery();
                }
                 if(dane[3].equals("1") & filtr==true )
                {
                    filtr=false;
                    if(dane[2].contains("@")) {
                        PreparedStatement stmt1 = connection.prepareStatement("select * from uzytkownik uzy join samochod sam on uzy.qr_code=sam.qr_code where sam.qr_code in (Select qr_code from samochod) " +
                                "and uzy.email like '%" + dane[2] + "%' @");
                        rs = stmt1.executeQuery();
                    }
                    else
                    {
                        PreparedStatement stmt1 = connection.prepareStatement("select * from uzytkownik uzy join samochod sam on uzy.qr_code=sam.qr_code where sam.qr_code in (Select qr_code from samochod) " +
                                "and (nr_rejestracyjny like  '%" + dane[2] + "%' or marka like  '%" + dane[2] + "%' or model like  '%" + dane[2] + "%') ");
                        rs = stmt1.executeQuery();
                    }

                }



                while (rs.next()) {
                    zm = rs.getString("nr_rejestracyjny");

                    if (zm != null) {
                        Id.add(rs.getString("Id"));
                        marka_a.add(rs.getString("Marka")+" "+  rs.getString("model"));
                        nr_rejestracyjny_a.add(rs.getString("nr_rejestracyjny"));
                        qrcode_tab.add(rs.getString("qr_code"));
                        if(dane[0].equals("konfiguracja")) {
                            aktywne.add(rs.getString("wyswietl"));
                        }else
                        {
                            aktywne.add("nie");
                        }
                        status=true;
                       // Log.i("historiapojazd","Status"+ status);

                    } else  if (zm == null){
                        status=false;
                    }

                }

                if(delete==true) {
                    delete=false;
                    String sql3 = "Delete from samochod where Id='" + Id.get(pozycja) + "'";

                    try {
                        st.executeUpdate(sql3);
                        connection.close();
                        showToast("Samochód został usunięty");
                    } catch (SQLException e) {
                        Log.i("koniecpojazd", "" + e);
                    }
                }


                } catch (SQLException e) {
                Log.i("New user",""+e);

            }
            try {
                if (connection != null)
                    connection.close();
                    liczba.setText("Samochody("+marka_a.size()+")");
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
              //  marka_a.clear();
               // nr_rejestracyjny_a.clear();
                zm5.clear();
                zm6.clear();
                zm8.clear();
                zdjecie.clear();
                qrcode_tab.clear();
                zm1=null;
                Log.i("historia_pojazd",dane[0]+" "+dane[1]+" "+dane[2]);

                if(dane[0].equals("zgloszenie_1")) {
                    PreparedStatement stmt2 = connection.prepareStatement("select * from samochod sam " +
                            "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                            "where sam.qr_code = '" + dane[1] + "' and sam.nr_rejestracyjny = '" + dane[2] + "' order by zgl.id desc ");
                    rs = stmt2.executeQuery();
                }

                if(dane[0].equals("zgloszenie_1") & dane[3].equals("1")) {
                    PreparedStatement stmt2 = connection.prepareStatement("select * from samochod sam " +
                            "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                            "where sam.nr_rejestracyjny = '" + dane[2] + "' order by zgl.id desc ");
                    rs = stmt2.executeQuery();
                }

                if(dane[0].equals("historia"))
                {
                    PreparedStatement stmt2 = connection.prepareStatement("select * from samochod sam " +
                            "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                            "where sam.qr_code = '" + dane[1] + "' and sam.nr_rejestracyjny like '%" + dane[2] + "%' order by zgl.id desc ");
                    rs = stmt2.executeQuery();
                }

                if(dane[0].equals("historia") & dane[3].equals("1")) {
                    PreparedStatement stmt2 = connection.prepareStatement("select * from samochod sam " +
                            "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                            "where sam.nr_rejestracyjny like '%" + dane[2] + "%' order by zgl.id desc ");
                    rs = stmt2.executeQuery();
                }

                while (rs.next()) {
                    zm1 = rs.getString("zgl.data_dod");

                    if (zm1 != null) {
                       // marka_a.add(rs.getString("sam.marka")+" "+  rs.getString("sam.model"));
                      //  nr_rejestracyjny_a.add(rs.getString("sam.nr_rejestracyjny"));
                        zm4.add(rs.getString("zgl.Id"));
                        zm5.add(rs.getString("zgl.status"));
                        zm6.add(rs.getString("zgl.data_dod"));
                        zm8.add(rs.getString("zgl.nr_rejestracyjny"));
                        zdjecie.add( rs.getBlob("zdjecie_przed"));
                        status=true;

                    } else  if (zm1 == null){
                        Log.i("historiapojazd","nie"+ dane[2]);
                        status=false;
                    }

                }

                if(zm1 == null)
                {
                    showToast("Brak zgłoszeń do tego pojazdu");
                }

                //usuwanie zgłoszenia

                if(delete==true) {
                    delete=false;
                    String sql3 = "Delete from zgloszenie where Id='" + zm4.get(pozycja) + "'";
                    try {
                        st.executeUpdate(sql3);
                        connection.close();
                        showToast("Zgłoszenie zostało usunięte");
                    } catch (SQLException e) {
                        Log.i("koniecpojazd", "" + e);
                    }
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

    private void SelectUser()
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
                PreparedStatement stmt1 = connection.prepareStatement("select * from uzytkownik ");
                rs = stmt1.executeQuery();


                    while (rs.next()) {
                        String zm = rs.getString("email");

                        if (zm != null) {
                            marka_a.add(rs.getString("email"));
                            nr_rejestracyjny_a.add(rs.getString("punkty"));
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

    //wyswietlqanie zgloszenia
    public void adapter_Zgloszenie()
    {
        if(zm1 != null) {
            Custom_row_zgloszenie adapter1 = new Custom_row_zgloszenie(this, zm8, zm6, zm5, zdjecie);
            lista_new.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
        }
    }

    public static Historia_pojazd getInstance() {

        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia_pojazd);
        instance = this;

        szukaj1 = (Button) findViewById(R.id.b_szukaj);
        szukaj2 = (EditText) findViewById(R.id.szukaj);
        powrót = (Button) findViewById(R.id.b_powrot);
        pole = (TextView) findViewById(R.id.tekst);
        liczba = (TextView) findViewById(R.id.textView13);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        lista_new = (ListView) findViewById(R.id.lista_new);

        try {

            dane[0] = getIntent().getStringExtra("menu");
            dane[1] = getIntent().getStringExtra("qr_code");
            dane[3] = getIntent().getStringExtra("admin");
            dane[4] = getIntent().getStringExtra("rejestracyjny");
            dane[5] = getIntent().getStringExtra("czy_zapis");
            Log.i("qr_code", dane[4]);



        }catch (Exception e)
        {
            Log.i("historiapojazd",""+e);
        }




        try {
            if (dane[5] != null & dane[5].equals("1")) {
                checkBox3.setChecked(true);
            }
        }catch (Exception e)
        {
            Log.i("historiapojazd",""+e);
        }

        //automatyczne ukrywanie klawiatury na starcie
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //historia pojazdu z samochodami
        if(dane[0].equals("historia")) {
            pole.setText("Historia pojazdów");
            liczba.setText("Samochody("+marka_a.size()+")");

            //zgłoszenie wywyływane z historii
        }else if (dane[0].equals("zgloszenie"))
        {
            pole.setText("Dodaj zgłoszenie");
            liczba.setText("Samochody("+marka_a.size()+")");

            //zgłoszenie wywyoływane z menu
        }else if (dane[0].equals("menu_zgloszenie"))
        {
            //wyłączenie wyszukiwania
            szukaj2.setVisibility(View.INVISIBLE);
            szukaj1.setVisibility(View.INVISIBLE);
            pole.setText("Zgłoszenia pojazdu");

            dane[0]="zgloszenie_1";
         //   Log.i("rejestra ",dane[2]);
            dane[2]=dane[4];

            //pobieranie danych
            SelectDataUserSkan();

            //wyswietlanie zgłoszęń
            adapter_Zgloszenie();

            //wyswietlanie liczby zgłoszeń
            liczba.setText("Zgłoszenia("+zm4.size()+")");
            if(status==false)
            {
                dane[0]="historia";
            }
        //konfiguracja konta
        } else if (dane[0].equals("konfiguracja"))
        {
            liczba.setText("Samochody("+marka_a.size()+")");
            pole.setText("Konfiguracja konta");
            szukaj1.setVisibility(View.INVISIBLE);
            szukaj2.setVisibility(View.INVISIBLE);
            checkBox3.setVisibility(View.VISIBLE);
        }
        else if (dane[0].equals("konta"))
        {
            pole.setText("Użytkownicy systemu");
            szukaj1.setVisibility(View.INVISIBLE);
            szukaj2.setVisibility(View.INVISIBLE);
            checkBox3.setVisibility(View.INVISIBLE);
        }

        //wyswietlanie pobranych danych w liscie
         if (!dane[0].equals("zgloszenie_1") & !dane[0].equals("konta")) {
            SelectDataUser();
             liczba.setText("Samochody("+marka_a.size()+")");
            Custom_row_pojazd adapter2 = new Custom_row_pojazd(Historia_pojazd.this, marka_a, nr_rejestracyjny_a,aktywne);
            lista_new.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }else if (dane[0].equals("konta"))
         {
             SelectUser();
             liczba.setText("Użytkownicy("+marka_a.size()+")");
             Custom_row adapter3 = new Custom_row(Historia_pojazd.this,marka_a,nr_rejestracyjny_a);
             lista_new.setAdapter(adapter3);
             adapter3.notifyDataSetChanged();

         }

        lista_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(click==false) {
                    if (dane[0].equals("zgloszenie")) {
                        Intent i = new Intent(Historia_pojazd.this, lista_pojazd.class);
                        i.putExtra("rejestracyjny", nr_rejestracyjny_a.get(position));
                        i.putExtra("kategoria", "kat_1");
                        i.putExtra("qr_code", dane[1]);
                        i.putExtra("liczba", "");
                        i.putExtra("pozycja", "");
                        i.putExtra("nazwa", "");
                        i.putExtra("admin", dane[3]);
                        startActivity(i);
                    }
                    //przejcie z widoku zgłoszenia
                    else if (dane[0].equals("historia")) {
                        szukaj2.setEnabled(false);
                        dane[0] = "zgloszenie_1";
                        //Log.i("pozycja_rejestracja", nr_rejestracyjny_a.get(position));
                        dane[2] = nr_rejestracyjny_a.get(position);
                        SelectDataUserSkan();
                        liczba.setText("Zgłoszenia(" + zm4.size() + ")");
                        adapter_Zgloszenie();
                        if (status == false) {
                            dane[0] = "historia";
                        }

                    }
                    //przejscie do edycji zgloszenia
                    else if (dane[0].equals("zgloszenie_1")) {
                        Intent i = new Intent(Historia_pojazd.this, koniec_pojazd.class);
                        i.putExtra("menu", "edit");
                        i.putExtra("qr_code", dane[1]);
                        i.putExtra("status", "1");
                        i.putExtra("admin", dane[3]);
                        i.putExtra("rejestracyjny", zm8.get(position));
                        i.putExtra("pozycja2", zm4.get(position));
                        startActivity(i);
                    }

                    //przejście do danych u żytkownika
                    else if (dane[0].equals("konta")) {
                        Intent i = new Intent(Historia_pojazd.this, dane_pojazd.class);
                        i.putExtra("menu", "konfiguracja");
                        i.putExtra("qr_code", dane[1]);
                        i.putExtra("model",marka_a.get(position));
                        i.putExtra("admin", dane[3]);
                        startActivity(i);
                    }
                }

            }
        });



        lista_new.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Historia_pojazd.this);
                click=true;
                if(dane[0].equals("zgloszenie") || dane[0].equals("zgloszenie_1"))
                {
                    builder.setTitle("Czy na pewno chcesz usunąć Zgłoszenie ");
                }
                //przejcie z widoku zgłoszenia
                else if(dane[0].equals("historia"))
                {
                    builder.setTitle("Czy na pewno chcesz usunąć samochód ");
                }

                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pozycja=position;
                        delete=true;

                        if(dane[0].equals("zgloszenie_1"))
                        {
                            click=false;
                            Log.i("koniecpojazd_delete","zm");
                            SelectDataUserSkan();

                            ///TODO do sprawdzenia
                            Intent i = new Intent(Historia_pojazd.this, MainMenu.class);
                            i.putExtra("menu", "historia");
                            i.putExtra("admin", dane[3]);
                            i.putExtra("qr_code", dane[1]);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            }
                            startActivity(i);
                            dialog.cancel();

                        }
                        //przejcie z widoku zgłoszenia
                        else if(dane[0].equals("historia"))
                        {
                            click=false;
                            Log.i("koniecpojazd_delete","zm1");
                            SelectDataUser();
                            dialog.cancel();

                        }

                    }
                });
                builder.setNeutralButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lista_new.setClickable(true);
                        dialog.cancel();
                    }
                });

                AlertDialog alert1 = builder.create();
                alert1.show();

                return false;
            }
        });



        szukaj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dane[2]="";
                dane[2]=szukaj2.getText().toString();
                if(!dane[2].equals("") & filtr2==false) {
                    filtr = true;
                    SelectDataUser();

                    Custom_row_pojazd adapter2 = new Custom_row_pojazd(Historia_pojazd.this, marka_a, nr_rejestracyjny_a, aktywne);
                    lista_new.setAdapter(adapter2);

                    if(dane[3].equals("1")) {
                        showToast("Wyszukano samochochody uzytkowmników z emailem lub rejestracją");
                    }
                    else {
                        showToast("Wyszukano pojazdy z rejestracją: " + dane[2]);
                    }
                }
            }
        });


        powrót.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dane[0].equals("historia") )
                {
                    Intent i = new Intent(Historia_pojazd.this, MainMenu.class);
                    i.putExtra("menu", "historia");
                    i.putExtra("admin", dane[3]);
                    i.putExtra("qr_code", dane[1]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                    startActivity(i);
                } else if (dane[0].equals("zgloszenie_1") ) {
                    Intent i = new Intent(Historia_pojazd.this, Historia_pojazd.class);
                    i.putExtra("menu", "historia");
                    i.putExtra("admin", dane[3]);
                    i.putExtra("qr_code", dane[1]);
                    startActivity(i);
                }
                else if (dane[0].equals("konfiguracja") ) {

                    //update pola czy na pewno zapisać zmany aktywna akceptacja
                    update_konfiguracj("","");

                    Intent i = new Intent(Historia_pojazd.this, MainMenu.class);
                    i.putExtra("admin", dane[3]);
                    i.putExtra("qr_code", dane[1]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Historia_pojazd.this, MainMenu.class);
                    i.putExtra("menu", "historia");
                    i.putExtra("qr_code", dane[1]);
                    i.putExtra("admin", dane[3]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                    startActivity(i);
                }
            }
        });

    }
}
