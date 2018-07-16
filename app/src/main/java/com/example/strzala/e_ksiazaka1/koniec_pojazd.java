package com.example.strzala.e_ksiazaka1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.data;

public class koniec_pojazd extends AppCompatActivity {

    ImageButton zdjecie;
    private static final int CAMERA_PIC_REQUEST = 1111;
    Bitmap thumbnail;
    public byte[] data1;
    Button zapis,anuluj,dodaj;

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_LONG).show();
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koniec_pojazd);

        zdjecie =(ImageButton) findViewById(R.id.imageButton);
        dodaj =(Button) findViewById(R.id.dodaj);
        anuluj =(Button) findViewById(R.id.anuluj);
        zapis = (Button) findViewById(R.id.zapisz_p);

        zdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });

        zapis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showToast("Zgłoszenie naprawy zostało dodane");
            }
        });

        anuluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(koniec_pojazd.this,MainMenu.class);
                startActivity(i);
            }
        });

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(koniec_pojazd.this,lista_pojazd.class);
                startActivity(i);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            zdjecie.setImageDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 500, 500, true)));


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            //to można zapisać do bazy danych
            data1 = getBitmapAsByteArray(thumbnail); // this is a function

            //tutaj jest zapis na urządzeniu
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image1.jpg");
            try {
                //przekazywanie danych do pliku
                //tablica[30]=String.valueOf(file);
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
    }
}
