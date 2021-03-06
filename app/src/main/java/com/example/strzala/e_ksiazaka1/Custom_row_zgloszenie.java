package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class Custom_row_zgloszenie extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> zm1 = new ArrayList<String>();
    ArrayList<String> zm2 = new ArrayList<String>();
    ArrayList<String> zm3 = new ArrayList<String>();
    ArrayList<java.sql.Blob> zm4 = new ArrayList<java.sql.Blob>();
    TextView tekst1,tekst2,tekst3;
    ImageView zdjecie;
    InputStream is;

    public Custom_row_zgloszenie(Activity context,
                      ArrayList<String> zma, ArrayList<String> zma1,ArrayList<String> zma2, ArrayList<java.sql.Blob> zma3) {
        super(context, R.layout.activity_custom_row_zgloszenie, zma);
        this.context = context;
        this.zm1 = zma;
        this.zm2 = zma1;
        this.zm3 = zma2;
        this.zm4 = zma3;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView1=inflater.inflate(R.layout.activity_custom_row_zgloszenie, null,true);

        tekst1 = (TextView) rowView1.findViewById(R.id.model);
        tekst2 = (TextView) rowView1.findViewById(R.id.email_punkty);
        tekst3 = (TextView) rowView1.findViewById(R.id.status);
        zdjecie = (ImageView) rowView1.findViewById(R.id.imageView);

        if(zm2.get(position)!=null) {

            if (zm4.get(position) != null) {

                try {
                    is = zm4.get(position).getBinaryStream();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
               // Drawable d = Drawable.createFromStream(is , "src");

                //teraz to zakomentowałem
               // BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
              //  Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                //mega rozwiązanie wprowadzone testowo
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap preview_bitmap = BitmapFactory.decodeStream(is, null, options);

                zdjecie.setImageBitmap(preview_bitmap);
               // zdjecie.setImageDrawable(d);


                //Bitmap bm = BitmapFactory.decodeByteArray(zm4.get(position), 0, zm4.size());

            } else {
                zdjecie.setImageDrawable(zdjecie.getResources().getDrawable(R.mipmap.car4));
            }


            tekst1.setText(zm1.get(position));
            tekst2.setText(zm2.get(position));
            tekst3.setText(zm3.get(position));
            if(zm3.get(position).equals("Nowy") || zm3.get(position).equals("Brak Akceptacji"))
            {
                tekst3.setTextColor(ContextCompat.getColor(context, R.color.close));
            }
            else if(zm3.get(position).equals("Zakończony"))
            {
                tekst3.setTextColor(ContextCompat.getColor(context, R.color.akcept));
            }
            else if(zm3.get(position).equals("Akceptacja")  )
            {
                tekst3.setTextColor(ContextCompat.getColor(context, R.color.bufor));
            }
        }

        return rowView1;
    }
}