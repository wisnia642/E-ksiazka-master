package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class Regulamin extends AppCompatActivity {

    Button powrot;
    TextView tekst;
    String dane[] = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulamin);

        powrot = (Button) findViewById(R.id.powrot);
        tekst = (TextView) findViewById(R.id.regula);

        try {

            dane[0] = getIntent().getStringExtra("email");
            dane[1] = getIntent().getStringExtra("haslo");
            dane[2] = getIntent().getStringExtra("haslo_pow");
            dane[3] = getIntent().getStringExtra("qr_code");
            dane[4] = getIntent().getStringExtra("menu");

        }catch (Exception e)
        {
            Log.i("Regulamin",""+e);
        }

        tekst.setText("Regulamin serwisu www musi spełniać wiele wymogów formalnych." +
                " Bez względu na to, czy jesteś osobą prawną czy osobą fizyczną – regulamin" +
                " jest umową, na podstawie której realizowana jest Twoja usługa, dlatego " +
                "tak ważne jest precyzyjne określenie praw i obowiązków obu stron transakcji. " +
                "Dopasowany do indywidualnego profilu strony i charakteru działalności " +
                "regulamin serwisu www, wolny od klauzul niedozwolonych, przygotowany " +
                "zgodnie z obowiązującymi przepisami prawa, pozwoli skutecznie chronić " +
                "interesy obu stron uczestniczących w transakcji.Regulamin serwisu www musi " +
                "spełniać wiele wymogów formalnych. Bez względu na to, czy jesteś osobą" +
                "prawną czy osobą fizyczną – regulamin jest umową, na podstawie której" +
                " realizowana jest Twoja usługa, dlatego tak ważne jest precyzyjne określenie " +
                "praw i obowiązków obu stron transakcji. Dopasowany do indywidualnego profilu " +
                "strony i charakteru działalności regulamin serwisu www, wolny od klauzul" +
                " niedozwolonych, przygotowany zgodnie z obowiązującymi przepisami prawa," +
                " pozwoli skutecznie chronić interesy obu stron uczestniczących w transakcji.");

        powrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Regulamin.this,New_user.class);
                i.putExtra("email",dane[0]);
                i.putExtra("haslo",dane[1]);
                i.putExtra("haslo_pow",dane[2]);
                i.putExtra("qr_code",dane[3]);
                i.putExtra("menu",dane[4]);
                startActivity(i);
            }
        });


    }
}
