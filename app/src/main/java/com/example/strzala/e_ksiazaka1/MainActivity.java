package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.net.Uri;
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
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    Button logowanie,logowanie_qr,konto,przypmnienie_hasla;
    EditText email,haslo;
    CheckBox checkBox;
    String login,password,qr_code,hash,zapis_hasla,pamiec,login_pamiec;
    String login_act,password_act;
    private static final String SAMPLE_DB_NAME = "Baza";
    SQLiteDatabase sampleDB;

    String subject = "";
    String message = "";


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }

    //hash password
    private void hash()
    {
      try{
          hash = "%02320%xwc48" + String.valueOf(password_act.hashCode());


      }catch (Exception e)
      {
          Log.i("hash",""+e);
      }
    }


    //select data from table user
    private void SelectDataUser()
    {

        try{

            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS uzytkownik (Id INTEGER PRIMARY KEY AUTOINCREMENT,data_dod VARCHAR," +
                    " email VARCHAR, haslo VARCHAR, zapisz_haslo VARCHAR, qr_code VARCHAR, punkty VARCHAR,admin VARCHAR)");

            Cursor c = sampleDB.rawQuery("Select * from uzytkownik where email = '"+login_act+"' ",null);

            while(c.moveToNext())
            {
                pamiec = String.valueOf(c.getString(1));
                if(pamiec != null)
                {
                    login = String.valueOf(c.getString(2));
                    password = String.valueOf(c.getString(3));
                    qr_code = String.valueOf(c.getString(5));
                    Log.i("MainActivity",login);
                }
            }

            sampleDB.close();

        }catch (Exception e)
        {
            Log.i("baza",""+e);
        }
    }

    private void pamiec_hasla()
    {
        sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS pamiec (Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email VARCHAR, haslo VARCHAR)");
        sampleDB.execSQL("Delete from pamiec");
        sampleDB.execSQL("Insert into pamiec (email,haslo) VALUES ('"+login+"','"+hash+"') ");
        sampleDB.close();

    }

    private void nie_pamiec_hasla()
    {
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
                    " Otrzymałeś ten email ponieważ skorzystałeś z opcji przypomnienia hasła. \n" +
                    " Twoje hasło to: "+zapis_hasla+" \n"+
                    " Pozdrawiam Zespół TrustCar \n";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare buttton and edittext
        konto = (Button) findViewById(R.id.konto);
        przypmnienie_hasla = (Button) findViewById(R.id.przypomnienie_hasla);
        logowanie = (Button) findViewById(R.id.logowanie);
        logowanie_qr = (Button) findViewById(R.id.logowanie_qr);

        email = (EditText) findViewById(R.id.login);
        haslo = (EditText) findViewById(R.id.password);

        checkBox = (CheckBox) findViewById(R.id.checkBoks);

        sprawdz_haslo();

        //logowanie przy użyciu danych logowania
        logowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                login_act = email.getText().toString();
                password_act = haslo.getText().toString();

                login_act = login_act.replace(" ","");
                password_act = password_act.replace(" ","");

                SelectDataUser();
                if(pamiec != null) {
                    hash();
                }

                    if (login_act.equals(login) & login_act.contains("@")) {
                        if (hash.equals(password) || (password_act.equals(password))) {

                                //sprawdzanie czy mam zapamiętać hasło
                                if(checkBox.isChecked())
                                {
                                    pamiec_hasla();
                                }
                                Intent c = new Intent(MainActivity.this,MainMenu.class);
                                c.putExtra("email",login);
                                startActivity(c);
                                finish();
                                }else
                        {
                            showToast("Nie poprawne hasło");
                        }

                        }else
                    {
                        showToast("Nie poprawny email ");
                    }

            }
        });

        //logowanie przy użyciu QR kode
        logowanie_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,BarCodeScaner.class);
                i.putExtra("ekran","logowanie");
                startActivity(i);
            }
        });

        //wysyłanie maila z hasłem
        przypmnienie_hasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                login_act = email.getText().toString();

                if (login_act.contains("@"))
                {
                if(!login_act.equals(""))
                {
                    sendemail_execiut();

                }else
                {
                    showToast("Wypełnij pole z emailem");
                }
                }else
                {
                    showToast("Nie poprawny adres email");
                }

            }
        });



        //Tworzenie nowego użytkownika
        konto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,New_user.class);
                startActivity(i);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                login_act = email.getText().toString();
                if (isChecked & login_act!=null)
                {
                    pamiec_hasla();
                }
                else
                {
                    nie_pamiec_hasla();
                }
            }
        });
    }
}
