package com.example.strzala.e_ksiazaka1;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Build;
        import android.os.StrictMode;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.google.zxing.Result;

        import java.io.FileInputStream;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;

        import me.dm7.barcodescanner.zxing.ZXingScannerView;

        import static android.Manifest.permission.CAMERA;

public class BarCodeScaner extends AppCompatActivity implements ZXingScannerView.ResultHandler  {



    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    public String ekran="",myResult="";
    public String dane[] = new String[10];
  //  private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    public boolean activeNetwork () {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;

    }

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

    private void SelectDataUser(String tekst)
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
                PreparedStatement stmt1 = connection.prepareStatement("select * from qr_code where kod='"+tekst+"' and aktywne='1' ");
                rs = stmt1.executeQuery();


                while (rs.next()) {
                    String zm = rs.getString("kod");

                    if (zm != null) {
                        dane[8] = rs.getString("kod");

                    }else
                    {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            ekran = getIntent().getStringExtra("ekran");
            if(ekran.equals("uzytkownik")) {
                dane[0] = getIntent().getStringExtra("email");
                dane[1] = getIntent().getStringExtra("haslo");
                dane[2] = getIntent().getStringExtra("haslo_pow");
                dane[3] = getIntent().getStringExtra("qrcode");
            } else if(ekran.equals("pojazd_dane"))
            {
                dane[0] = getIntent().getStringExtra("marka");
                dane[1] = getIntent().getStringExtra("model");
                dane[2] = getIntent().getStringExtra("rocznik");
                dane[3] = getIntent().getStringExtra("silnik");
                dane[3] = getIntent().getStringExtra("rejestracyjny");
            }

        }catch (Exception e)
        {
            Log.i("BarCode",""+e);
        }


        try {
            scannerView = new ZXingScannerView(BarCodeScaner.this);
            setContentView(scannerView);
        }catch (Exception e)
        {
            Log.i("camera",""+e);
        }


        int currentApiVersion = Build.VERSION.SDK_INT;

            if(checkPermission())
                requestPermission();
            }




    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(BarCodeScaner.this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){

                     //   Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                     //   Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(BarCodeScaner.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {

        if(!result.equals("")) {
            myResult = result.getText();
            Log.d("QRCodeScanner", result.getText());
            Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
            Log.i("blad",""+myResult);

            //ekran uruchamiany z poziomu logowania
            if(ekran.equals("logowanie")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Twój zeskanowany kod to:");
                builder.setPositiveButton("Zaloguj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SelectDataUser(myResult);
                        if(dane[8].equals(myResult)) {
                                  // https://vicards.pl/pUcJCNC2
                                Intent i = new Intent(BarCodeScaner.this, MainMenu.class);
                                i.putExtra("qrkod", myResult);
                                startActivity(i);
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Podany kod jest nieprawidłowy lub został już aktywowany", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNeutralButton("Skanuj ponownie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(BarCodeScaner.this);
                    }
                });

                if(myResult.equals("https://vicards.pl")){
                    String new_result = myResult.replace("https://vicards.pl/","");
                    builder.setMessage(new_result);
                }else
                {
                    builder.setMessage(result.getText());
                }
                AlertDialog alert1 = builder.create();
                alert1.show();

            }
            //ekran uruchamiany z poziomu tworzenia nowego urzytkownika
            else if(ekran.equals("uzytkownik"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Twój zeskanowany kod to:");

                builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(BarCodeScaner.this, New_user.class);
                            i.putExtra("email", dane[0]);
                            i.putExtra("haslo", dane[1]);
                            i.putExtra("haslo_pow", dane[2]);
                            i.putExtra("qrcode", myResult);
                            startActivity(i);

                    }
                });
                builder.setNeutralButton("Skanuj ponownie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(BarCodeScaner.this);
                    }
                });
                if(myResult.equals("https://vicards.pl")){
                    String new_result = myResult.replace("https://vicards.pl/","");
                    builder.setMessage(new_result);
                }else
                {
                    builder.setMessage(result.getText());
                }
                AlertDialog alert1 = builder.create();
                alert1.show();
            }
            else if(ekran.equals("pojazd_dane"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Twój zeskanowany kod to:");
                builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(BarCodeScaner.this, New_user.class);
                        i.putExtra("qrcode",myResult);
                        i.putExtra("marka",dane[0]);
                        i.putExtra("model", dane[1]);
                        i.putExtra("rocznik",dane[2]);
                        i.putExtra("silnik",dane[3]);
                        i.putExtra("rejestracyjny",dane[5]);
                        startActivity(i);

                    }
                });
                builder.setNeutralButton("Skanuj ponownie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(BarCodeScaner.this);
                    }
                });
                if(myResult.equals("https://vicards.pl")){
                    String new_result = myResult.replace("https://vicards.pl/","");
                    builder.setMessage(new_result);
                }else
                {
                    builder.setMessage(result.getText());
                }
                AlertDialog alert1 = builder.create();
                alert1.show();
            }

        }

    }
}
