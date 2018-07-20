package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Historia_pojazd extends AppCompatActivity {

    Button szukaj1,powrót;
    EditText pole;
    ListView lista_new=null;
    String zm2[] = new String[6];
    String dane,email;

    private static final String SAMPLE_DB_NAME = "Baza";
    SQLiteDatabase sampleDB;

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }


    private void SelectDataUser()
    {
        try{

            sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE,null);

            Cursor c = sampleDB.rawQuery("Select * from samochod where email = '"+email+"' ",null);
            int i=0;
            while(c.moveToNext()) {
                zm2[i] = String.valueOf(c.getString(5));
                i++;
            }

            sampleDB.close();

            if(zm2[0].equals(""))
            {
                showToast("Brak pojazdów do wyświetlenia");
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


        szukaj1 = (Button) findViewById(R.id.b_szukaj);
        powrót = (Button) findViewById(R.id.b_powrot);
        pole = (EditText) findViewById(R.id.szukaj);

        Custom_row_pojazd adapter2=new Custom_row_pojazd(Historia_pojazd.this, zm2);
        lista_new = (ListView) findViewById(R.id.lista_new);
        lista_new.setAdapter(adapter2);

        try {

            email = getIntent().getStringExtra("email");
        }catch (Exception e)
        {

        }

        SelectDataUser();

        szukaj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectDataUser();

                dane=pole.getText().toString();
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
