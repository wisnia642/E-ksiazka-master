package com.example.strzala.e_ksiazaka1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import static android.Manifest.permission.CAMERA;

public class koniec_pojazd extends AppCompatActivity {

    ImageView zdjecie,galeria;
    RelativeLayout layout;
    private static final int CAMERA_PIC_REQUEST = 1111;
    public byte[] data1;
    Button zapis,anuluj,dodaj;
    String data="",qrcode="",kategoria="",punkty_baza="",sql1="",czy_zapis="",status="";
    EditText czesci1,uslugi,punkty1;
    AutoCompleteTextView uwagi;
    TextView naprawa,opis;
    Switch przelacznik;
    Integer punkty_p,czesci_p,uslugi_p;

    String dane[] = new String[18];
    public int polaczenie=0;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;
    InputStream is;
    Boolean plik=false, akceptacja=false;

    Blob zdjecie_przed = null;
    Blob zdjecie_po = null;
    Bitmap bitmap;

    //dodanie opcji do menu
    public static final int PIERWSZY_ELEMENT = 1;
    private static final int REQUEST_CAMERA = 1;

    File file=null;
     ProgressBar simpleProgressBar_new = null;
    private Handler handler = new Handler();
    boolean StartLog=false;


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }



    //akceptacja kosztów zgłoszenia
    public void akceptacja_kosztów()
    {
        podlaczenieDB();

        if (connection != null) {

            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                //e1.printStackTrace();
            }

            try {

                if (dane[10].equals("1")) {
                    String sql2 = "Update zgloszenie set akceptacja='1' , status='Akceptacja' where Id='" + dane[2] + "' ";
                    st.executeUpdate(sql2);
                }else
                {
                    String sql2 = "status='Brak akceptacji' where Id='" + dane[2] + "' ";
                    st.executeUpdate(sql2);
                }

            }catch (Exception e)
            {
                Log.i("myTag", "8" + e);
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

   // odczytywanie zdjęcia z bazy danych
   public void readimage() {

       podlaczenieDB();

       czesci_p=0;
       uslugi_p=0;
       punkty_p=0;
       if (connection != null) {
           try {

               PreparedStatement stmt3 = connection.prepareStatement("select * from samochod sam " +
                       "Left join zgloszenie zgl on sam.nr_rejestracyjny=zgl.nr_rejestracyjny " +
                       "where zgl.Id= '"+dane[2]+"' ");
               rs = stmt3.executeQuery();

               while (rs.next()) {
                   String zm = rs.getString("zgl.Id");
                   if (zm != null) {

                       czesci_p = rs.getInt("zgl.cena_czesci");
                       uslugi_p = rs.getInt("zgl.cena_uslugi");
                       dane[13] = rs.getString("zgl.uwagi");
                       zdjecie_przed = rs.getBlob("zgl.zdjecie_przed");
                       zdjecie_po = rs.getBlob("zgl.zdjecie_po");
                       kategoria = rs.getString("kategoria");
                       punkty_p = rs.getInt("punkty");
                       status = rs.getString("status");
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
            // showToast("Brak podłączenia do intrernetu");
        }

    }

    public void InsertLoginDataMysql() {

        podlaczenieDB();

        if (connection != null) {

            dane[5]="";
            dane[4]="";
            dane[6]="";
            dane[7]="";
            if(!czesci1.getText().toString().equals("")) {
                czesci_p = Integer.parseInt(czesci1.getText().toString());
                dane[5] = String.valueOf(czesci_p / 10);
            }
            dane[4] = uwagi.getText().toString();
            dane[6] = czesci1.getText().toString();
            dane[7] = uslugi.getText().toString();

            if(dane[5].equals(""))
            {
                dane[5]="0";
            }
            if(dane[6].equals(""))
            {
                dane[6]="0";
            }
            if(dane[7].equals(""))
            {
                dane[7]="0";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy_HH:mm");
            data =sdf.format(new Date());

            //save data base
            try {
                if(file!=null) {
                    fis = new FileInputStream(file);
                    plik=true;
                }
            } catch (FileNotFoundException e) {
                Log.i("koniec_pojaz",""+e);
                plik=false;
            }


            if(dane[1].equals("0") & dane[10].equals("1")) {
                if(file!=null ) {

                     sql1 = "INSERT INTO zgloszenie (data_dod,zdjecie_przed,cena_czesci,cena_uslugi,uwagi," +
                            "data_wykonania,status,nr_rejestracyjny,akceptacja,kategoria,punkty) VALUES (?,?,?,?,?,?,?,?,?,?,?)";


                    try {
                        ps = connection.prepareStatement(sql1);
                        ps.setString(1, data);
                        if(file!=null) {
                            ps.setBinaryStream(2, fis, (int) file.length());
                        }
                        ps.setString(3, dane[6]);
                        ps.setString(4, dane[7]);
                        ps.setString(5, dane[4]);
                        ps.setString(6, "");
                        ps.setString(7, "Nowy");
                        ps.setString(8, dane[3]);
                        ps.setString(9, "0");
                        ps.setString(10,dane[2]);
                        ps.setString(11,dane[5]);
                        ps.executeUpdate();

                    } catch (SQLException e) {
                        Log.i("koniecpojazd", "" + e);
                    }

                    showToast("Dane zostały zapisane");

                }else
                {

                     sql1 = "INSERT INTO zgloszenie (data_dod,cena_czesci,cena_uslugi,uwagi," +
                            "data_wykonania,status,nr_rejestracyjny,akceptacja,kategoria,punkty) VALUES (?,?,?,?,?,?,?,?,?,?)";


                    try {
                        ps = connection.prepareStatement(sql1);
                        ps.setString(1, data);
                        ps.setString(2, dane[6]);
                        ps.setString(3, dane[7]);
                        ps.setString(4, dane[4]);
                        ps.setString(5, "");
                        ps.setString(6, "Nowy");
                        ps.setString(7, dane[3]);
                        ps.setString(8, "0");
                        ps.setString(9,dane[2]);
                        ps.setString(10,dane[5]);
                        ps.executeUpdate();

                    } catch (SQLException e) {
                        Log.i("koniecpojazd", "" + e);
                    }

                    showToast("Dane zostały zapisane");

                }

                //ekran mainmenu update zgłoszenie po
            }else if(dane[1].equals("1") & dane[10].equals("1") & !przelacznik.isChecked())
            {

                if (plik==false)
                {
                    dane[14] = "update zgloszenie SET cena_czesci=?,cena_uslugi=?,uwagi=?,data_dod=?,status=?,punkty=? where Id='"+dane[2]+"' ";
                }else if (plik==true){
                    dane[14] = "update zgloszenie SET zdjecie_przed=?,cena_czesci=?,cena_uslugi=?,uwagi=?,data_dod=?,status=?,punkty=? where Id='" + dane[2] + "' ";
                }

                try {
                    ps = connection.prepareStatement(dane[14]);
                    if(plik==true) {
                        ps.setBinaryStream(1, fis, (int) file.length());
                        ps.setString(2, dane[6]);
                        ps.setString(3, dane[7]);
                        ps.setString(4, dane[4]);
                        ps.setString(5, data);
                        ps.setString(6, "Nowy");
                        ps.setString(7, dane[5]);
                    }else if (plik==false)
                    {
                        ps.setString(1, dane[6]);
                        ps.setString(2, dane[7]);
                        ps.setString(3, dane[4]);
                        ps.setString(4, data);
                        ps.setString(5, "Nowy");
                        ps.setString(6, dane[5]);
                    }

                    ps.executeUpdate();


                } catch (SQLException e) {
                    Log.i("koniec pojazd", "" + e);
                    showToast("" + e);
                }

                showToast("Dane zostały zapisane");

            }

            //ekran mainmenu update zgłoszenie przed
            else if(dane[1].equals("1") & dane[10].equals("1") & przelacznik.isChecked()) {
                try {
                    st = connection.createStatement();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                punkty_baza = "";
                czy_zapis = "";

                PreparedStatement stmt1 = null;
                try {
                    stmt1 = connection.prepareStatement("Select * from uzytkownik uzy where uzy.qr_code= '" + qrcode + "'");
                    rs = stmt1.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    while (rs.next()) {
                        String zm = rs.getString("uzy.Id");

                        if (zm != null) {
                            czy_zapis = (rs.getString("uzy.czy_zapis"));


                        }
                    }
                } catch (SQLException e) {
                    Log.i("koniecpojazd", "" + e);
                }


                if (plik == false) {
                    dane[14] = "update zgloszenie SET cena_czesci=?,cena_uslugi=?,uwagi=?,data_wykonania=?,status=?,punkty=? where Id='" + dane[2] + "' ";
                } else if (plik == true) {
                    dane[14] = "update zgloszenie SET zdjecie_po=?,cena_czesci=?,cena_uslugi=?,uwagi=?,data_wykonania=?,status=?,punkty=? where Id='" + dane[2] + "' ";
                }

                if(czy_zapis.equals("1") || status.equals("Akceptacja"))
                {
                try {
                    ps = connection.prepareStatement(dane[14]);
                    if (plik == true) {
                        ps.setBinaryStream(1, fis, (int) file.length());
                        ps.setString(2, dane[6]);
                        ps.setString(3, dane[7]);
                        ps.setString(4, dane[4]);
                        ps.setString(5, data);
                        ps.setString(6, "Zakończony");
                        ps.setString(7, dane[5]);
                    } else if (plik == false) {
                        ps.setString(1, dane[6]);
                        ps.setString(2, dane[7]);
                        ps.setString(3, dane[4]);
                        ps.setString(4, data);
                        ps.setString(5, "Zakończony");
                        ps.setString(6, dane[5]);
                    }

                    ps.executeUpdate();

                } catch (SQLException e) {
                    Log.i("koniec pojazd", "" + e);
                    showToast("" + e);
                }
                akceptacja=false;
                showToast("Dane zostały zapisane");
            }else {
                    akceptacja=true;
                    showToast("Zgłoszenie nie zostało zaakceptowane");
                }

            }
            try {
                if (connection != null)
                    plik=false;
                connection.close();
            } catch (SQLException se) {
                Log.i("New user",""+se);
                //  showToast("brak połączenia z internetem" +se);
            }
        }
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koniec_pojazd);

        zdjecie =(ImageView) findViewById(R.id.imageButton);
        galeria = (ImageView) findViewById(R.id.imageButton2);
        dodaj =(Button) findViewById(R.id.dodaj);
        anuluj =(Button) findViewById(R.id.anuluj);
        zapis = (Button) findViewById(R.id.zapisz_p);

        czesci1 = (EditText) findViewById(R.id.czesci);
        uslugi = (EditText) findViewById(R.id.editText123);
        punkty1 = (EditText) findViewById(R.id.editText2);

        uwagi = (AutoCompleteTextView) findViewById(R.id.uwagi);
        naprawa = (TextView) findViewById(R.id.naprawa);
        opis = (TextView) findViewById(R.id.textView8);
        przelacznik = (Switch) findViewById(R.id.switch2);

        layout = (RelativeLayout) findViewById(R.id.activity_koniec_pojazd_galeria);
        simpleProgressBar_new=(ProgressBar) findViewById(R.id.progressBar1);

        try {
            dane[1] = getIntent().getStringExtra("status");
            dane[2] = getIntent().getStringExtra("pozycja2");
            dane[3] = getIntent().getStringExtra("rejestracyjny");
            dane[9] = getIntent().getStringExtra("menu");
            dane[10] = getIntent().getStringExtra("admin");
            qrcode = getIntent().getStringExtra("qr_code");

        }catch (Exception e)
        {
            Log.i("koniecpojazd",""+e);
        }

        if(dane[9].equals("") & dane[10].equals("1")) {
            naprawa.setText(dane[2]);
            zdjecie.setVisibility(View.VISIBLE);
        }

        if (!dane[10].equals("1"))
        {
            opis.setText("Podgląd zgłoszenia");
            dodaj.setText("Akceptacja kosztów");
            zapis.setText("Nie Akceptacja kosztów");
            przelacznik.setVisibility(View.VISIBLE);
        }

        if(dane[9].equals("edit") || dane[10].equals("1"))
        {
            readimage();

            czesci1.setText(String.valueOf(czesci_p));
            uslugi.setText(String.valueOf(uslugi_p));
            uwagi.setText(dane[13]);
            punkty1.setText(String.valueOf(punkty_p));
            naprawa.setText(kategoria);
            if(dane[10].equals("1")) {
                zdjecie.setVisibility(View.VISIBLE);
            }
        }

        try {
            if(zdjecie_przed!=null) {
                is = zdjecie_przed.getBinaryStream();
                //galeria.setImageBitmap(BitmapFactory.decodeStream(is));
                Drawable d = Drawable.createFromStream(is , "src");
                galeria.setImageDrawable(d);
            }


        } catch (SQLException e) {
            Log.i("koniecpojazd",""+e);
        }

        verifyStoragePermissions(koniec_pojazd.this);

        if(checkPermission()==false) {
            requestPermission();
        }

        punkty1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!czesci1.getText().toString().equals("")) {
                    czesci_p = Integer.parseInt(czesci1.getText().toString());
                    punkty1.setText(String.valueOf(czesci_p / 10));
                }
            }
        });


        zdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dane[9].equals("edit") & dane[10].equals("0"))
                {

                }else {
                    galeria.setImageResource(android.R.drawable.ic_search_category_default);
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                }
            }
        });


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        layout.setVisibility(View.INVISIBLE);
                        dodaj.setVisibility(View.VISIBLE);
                        anuluj.setVisibility(View.VISIBLE);
                        zapis.setVisibility(View.VISIBLE);

                        if(zdjecie_przed!=null) {
                            try {
                                is = zdjecie_przed.getBinaryStream();
                                galeria.setImageBitmap(BitmapFactory.decodeStream(is));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });


        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dane[9].equals("edit") & dane[10].equals("0"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if(!przelacznik.isChecked() & zdjecie_przed!=null) {
                            layout.setVisibility(View.VISIBLE);
                            layout.setBackground(galeria.getDrawable());
                            dodaj.setVisibility(View.INVISIBLE);
                            anuluj.setVisibility(View.INVISIBLE);
                            zapis.setVisibility(View.INVISIBLE);
                        }else if (przelacznik.isChecked() & zdjecie_po!=null)
                        {
                            layout.setVisibility(View.VISIBLE);
                            layout.setBackground(galeria.getDrawable());
                            dodaj.setVisibility(View.INVISIBLE);
                            anuluj.setVisibility(View.INVISIBLE);
                            zapis.setVisibility(View.INVISIBLE);
                        }
                    }else
                    {
                        showToast("Brak wsparcia");
                    }
                }else {

                    zdjecie.setImageResource(android.R.drawable.ic_input_add);
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        });

        zapis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activeNetwork()) {

                    if (dane[10].equals("1")) {

                        //jeżeli nie ma zdjęcia był problem z zapisem dlatego tak :)
                        if (file == null) {
                            simpleProgressBar_new.setVisibility(View.VISIBLE);
                            InsertLoginDataMysql();

                            if (akceptacja == false) {
                                Intent i = new Intent(koniec_pojazd.this, MainMenu.class);
                                i.putExtra("qr_code", qrcode);
                                i.putExtra("admin", dane[10]);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    finishAffinity();
                                }

                                startActivity(i);
                            } else {
                                simpleProgressBar_new.setVisibility(View.INVISIBLE);

                            }
                        } else if (file != null)
                            simpleProgressBar_new.setVisibility(View.VISIBLE);
                        StartLog = true;

                    }else
                    {
                        akceptacja_kosztów();
                        showToast("Koszty nie zostały zaakceptowane");
                    }


                }
                else {
                    showToast("Brak dostępu do internetu");
                }

            }
        });

        anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(koniec_pojazd.this,MainMenu.class);
                i.putExtra("qr_code",qrcode);
                i.putExtra("admin", dane[10]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                startActivity(i);
            }
        });

        przelacznik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!przelacznik.isChecked() )
                {

                          galeria.setImageResource(android.R.drawable.ic_search_category_default);
                          if(zdjecie_przed!=null) {
                              try {
                                   is = zdjecie_przed.getBinaryStream();
                                   // galeria.setImageBitmap(BitmapFactory.decodeStream(is));
                                    Drawable d = Drawable.createFromStream(is , "src");
                                    galeria.setImageDrawable(d);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                    }
                   //showToast("Przed");
                }else if(przelacznik.isChecked())
                {

                            galeria.setImageResource(android.R.drawable.ic_search_category_default);
                            if(zdjecie_po!=null) {
                                Log.i("koniecpojazd","zdjęcie_po");
                                try {
                                    is = zdjecie_po.getBinaryStream();
                                    Drawable d = Drawable.createFromStream(is , "src");
                                    galeria.setImageDrawable(d);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                //galeria.setImageBitmap(BitmapFactory.decodeStream(is));

                            }
                }
            }
        });

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //odczyt zapisanego zdjęcia
               // readimage();

                if (!dane[10].equals("1"))
                {
                    akceptacja_kosztów();
                    showToast("Koszty zostały zaakceptowane");
                }
                else {
                    Intent i = new Intent(koniec_pojazd.this, lista_pojazd.class);
                    i.putExtra("pozycja", "");
                    i.putExtra("nazwa", "");
                    i.putExtra("ekran", "");
                    i.putExtra("kategoria", "kat_1");
                    i.putExtra("qr_code", qrcode);
                    i.putExtra("admin", dane[10]);
                    startActivity(i);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, PIERWSZY_ELEMENT, 0, "Zapisz zdjęcie");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case PIERWSZY_ELEMENT:
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmm");
                String data_zdj = sdf.format(new Date());

                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), File.separator + data_zdj + "_trustcar.jpg");

                //save and compres picture
                try {
                    is = zdjecie_przed.getBinaryStream();
                    Drawable d = Drawable.createFromStream(is , "src");
                    bitmap = ((BitmapDrawable)d).getBitmap();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //galeria.setImageBitmap(BitmapFactory.decodeStream(is));


                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                showToast("Zdjęcie zostało zapisane w katalogu" +String.valueOf(file));

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmm");
                String data_zdj = sdf.format(new Date());

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                zdjecie.setImageDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 500, 500, true)));


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                //to można zapisać do bazy danych
               // data1 = getBitmapAsByteArray(thumbnail); // this is a function

                //tutaj jest zapis na urządzeniu
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), File.separator + data_zdj + "_trustcar_dod.jpg");

                //przekazywanie danych do pliku
                dane[8] = String.valueOf(file);
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                //5
                fo.write(bytes.toByteArray());
                fo.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("koniecpojazd", "" + e);
            } catch (Exception e) {
                Log.i("koniecpojazd", "" + e);
            }
        }

        if (requestCode == 2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmm");
                String data_zdj = sdf.format(new Date());

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                dane[8] = picturePath;


                galeria.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                //file = new File(picturePath);




                //tutaj jest zapis na urządzeniu
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), File.separator + data_zdj + "_trustcar_dod.jpg");

                //save and compres picture
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath, options);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               // thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //tutaj jest zapis na urządzeniu
               // file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), File.separator + data_zdj + "_trustcar.jpg");



            } catch (Exception e) {
                Log.i("koniecpojazd", "" + e);
            }

        }


        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (StartLog==true) {
                    Log.i("koniecpojazd", "dupa");
                    InsertLoginDataMysql();
                    if (akceptacja==false) {
                        Intent i = new Intent(koniec_pojazd.this, MainMenu.class);
                        i.putExtra("qr_code", qrcode);
                        i.putExtra("admin", dane[10]);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        startActivity(i);
                    }else
                    {
                        simpleProgressBar_new.setVisibility(View.INVISIBLE);
                    }
                    StartLog=false;
                }

                    //  simpleProgressBar.setProgress(suma);
                    handler.sendEmptyMessageDelayed(0, 100);

            }
        };

        handler.sendEmptyMessage(0);
    }
}

