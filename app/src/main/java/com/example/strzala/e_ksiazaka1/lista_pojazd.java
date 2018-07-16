package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class lista_pojazd extends AppCompatActivity {

    ListView lista_napraw;
    TextView tekst;

    String zm2[]={"Hamulce","Układ kierowniczy","Silnik","Turbo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pojazd);

        lista_napraw = (ListView) findViewById(R.id.kategoria_naprawy);
        tekst = (TextView) findViewById(R.id.tekst);

        tekst.setText("Kategoria naprawy");


        Custom_row_kategoriaNaprawy adapter2=new Custom_row_kategoriaNaprawy(this, zm2);
        lista_napraw.setAdapter(adapter2);


        lista_napraw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(lista_pojazd.this, koniec_pojazd.class);
                startActivity(i);

            }
        });



    }
}
