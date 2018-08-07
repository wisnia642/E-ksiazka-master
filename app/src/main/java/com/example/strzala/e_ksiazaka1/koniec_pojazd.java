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
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Date;

import static android.Manifest.permission.CAMERA;

public class koniec_pojazd extends AppCompatActivity {

    ImageButton zdjecie,galeria;
    private static final int CAMERA_PIC_REQUEST = 1111;
    public byte[] data1;
    Button zapis,anuluj,dodaj;
    String data="",qrcode;
    Blob blob;
    EditText czesci1,uslugi,punkty1;
    AutoCompleteTextView uwagi;
    TextView naprawa;
    Switch przelacznik;

    String dane[] = new String[10];
    public int polaczenie=0;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    FileOutputStream fos =null;
    Connection connection = null;
    InputStream is;

    private static final int REQUEST_CAMERA = 1;

    File file;

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }



   // odczytywanie zdjęcia z bazy danych
   public void readimage() {

       podlaczenieDB();

       if (connection != null) {
           try {

               PreparedStatement stmt4 = connection.prepareStatement("select * from zgloszenie where Id='4' ");
               rs = stmt4.executeQuery();

               while (rs.next()) {
                   String zm = rs.getString("Id");
                   if (zm != null) {
                       Blob blob = rs.getBlob("zdjecie_po");
                       is = blob.getBinaryStream();
                       try {
                           fos = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ file.separator + "image1.jpg");


                       int b = 0;
                       while ((b = is.read()) != -1)
                       {
                           fos.write(b);
                       }

                       } catch (FileNotFoundException e) {
                           Log.i("koniec_pojazd",""+e);
                       } catch (IOException e) {
                           Log.i("koniec_pojazd",""+e);
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

            dane[4] = uwagi.getText().toString();
            dane[5] = punkty1.getText().toString();
            dane[6] = czesci1.getText().toString();
            dane[7] = uslugi.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy_HH:mm");
            data =sdf.format(new Date());

            //save data base
            try {
                fis= new FileInputStream(file);
            } catch (FileNotFoundException e) {
                Log.i("koniec_pojaz",""+e);
            }


            try {
                st = connection.createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if(dane[1].equals("0")) {

                String sql1 = "INSERT INTO zgloszenie (data_dod,zdjecie_przed,cena_czesci,cena_uslugi,uwagi," +
                        "data_wykonania,status,nr_rejestracyjny,akceptacja) VALUES (?,?,?,?,?,?,?,?,?)";

                try {
                    ps = connection.prepareStatement(sql1);
                    ps.setString(1, data);
                    ps.setBinaryStream(2, fis, (int) file.length());
                    ps.setString(3, dane[6]);
                    ps.setString(4, dane[7]);
                    ps.setString(5, dane[4]);
                    ps.setString(6, "");
                    ps.setString(7, "Nowy");
                    ps.setString(8, dane[3]);
                    ps.setString(9, "0");
                    ps.executeUpdate();


                } catch (SQLException e) {
                    Log.i("New user", "" + e);
                }

            }else if(dane[1].equals("1"))
            {
                String sql1 = "INSERT INTO zgloszenie (data_dod,zdjecie_po,cena_czesci,cena_uslugi,uwagi," +
                        "data_wykonania,status,nr_rejestracyjny,akceptacja) VALUES (?,?,?,?,?,?,?,?,?)";

                try {
                    ps = connection.prepareStatement(sql1);
                    ps.setString(1, data);
                    ps.setBinaryStream(2, fis, (int) file.length());
                    ps.setString(3, dane[6]);
                    ps.setString(4, dane[7]);
                    ps.setString(5, dane[4]);
                    ps.setString(6, "");
                    ps.setString(7, "Zakończony");
                    ps.setString(8, dane[3]);
                    ps.setString(9, "0");
                    ps.executeUpdate();


                } catch (SQLException e) {
                    Log.i("koniec pojazd", "" + e);
                    showToast("" + e);
                }
            }
            try {
                if (connection != null)

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

        zdjecie =(ImageButton) findViewById(R.id.imageButton);
        galeria = (ImageButton) findViewById(R.id.imageButton2);
        dodaj =(Button) findViewById(R.id.dodaj);
        anuluj =(Button) findViewById(R.id.anuluj);
        zapis = (Button) findViewById(R.id.zapisz_p);

        czesci1 = (EditText) findViewById(R.id.czesci);
        uslugi = (EditText) findViewById(R.id.editText123);
        punkty1 = (EditText) findViewById(R.id.editText2);

        uwagi = (AutoCompleteTextView) findViewById(R.id.uwagi);
        naprawa = (TextView) findViewById(R.id.naprawa);
        przelacznik = (Switch) findViewById(R.id.switch2);

        try {
            dane[1] = getIntent().getStringExtra("status");
            dane[2] = getIntent().getStringExtra("pozycja2");
            dane[3] = getIntent().getStringExtra("rejestracyjny");
            qrcode = getIntent().getStringExtra("qr_code");
            naprawa.setText(dane[2]);

        }catch (Exception e)
        {
            Log.i("koniecpojazd",""+e);
        }

        verifyStoragePermissions(koniec_pojazd.this);

        if(checkPermission()==false) {
            requestPermission();
        }

        if(dane[1].equals("1"))
        {
            przelacznik.setChecked(true);
        }


        zdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeria.setImageResource(android.R.drawable.ic_search_category_default);
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zdjecie.setImageResource(android.R.drawable.ic_input_add);
                Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        zapis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InsertLoginDataMysql();
            showToast("Zgłoszenie naprawy zostało dodane");

            Intent i = new Intent(koniec_pojazd.this,MainMenu.class);
            i.putExtra("qr_code",qrcode);
            startActivity(i);
            }
        });

        anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(koniec_pojazd.this,MainMenu.class);
                i.putExtra("qr_code",qrcode);
                startActivity(i);
            }
        });

        przelacznik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(przelacznik.isChecked())
                {
                    Intent i = new Intent(koniec_pojazd.this,koniec_pojazd.class);
                    i.putExtra("pozycja2",dane[2]);
                    i.putExtra("qr_code",qrcode);
                    i.putExtra("status","1");
                    startActivity(i);
                }else if(!przelacznik.isChecked())
                {
                    Intent i = new Intent(koniec_pojazd.this,koniec_pojazd.class);
                    i.putExtra("pozycja2",dane[2]);
                    i.putExtra("qr_code",qrcode);
                    i.putExtra("status","0");
                    startActivity(i);
                }
            }
        });

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //odczyt zapisanego zdjęcia
               // readimage();

                Intent i = new Intent(koniec_pojazd.this,lista_pojazd.class);
                i.putExtra("pozycja", "");
                i.putExtra("nazwa", "");
                i.putExtra("ekran", "");
                i.putExtra("kategoria","kat_1");
                i.putExtra("qr_code",qrcode);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmm");
            String data_zdj =sdf.format(new Date());

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            zdjecie.setImageDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 500, 500, true)));


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            //to można zapisać do bazy danych
            data1 = getBitmapAsByteArray(thumbnail); // this is a function

            //tutaj jest zapis na urządzeniu
             file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) ,file.separator+data_zdj+"_trustcar.jpg");
            try {



                //przekazywanie danych do pliku
                dane[8]=String.valueOf(file);
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                //5
                fo.write(bytes.toByteArray());
                fo.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        if (requestCode == 2) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            dane[8] =picturePath;
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            galeria.setImageDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 500, 500, true)));
            file= new File(picturePath);

        }
    }
}
