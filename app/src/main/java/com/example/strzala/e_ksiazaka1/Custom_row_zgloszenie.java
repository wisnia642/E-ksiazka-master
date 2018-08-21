package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        tekst1 = (TextView) rowView1.findViewById(R.id.email_lista);
        tekst2 = (TextView) rowView1.findViewById(R.id.email_punkty);
        tekst3 = (TextView) rowView1.findViewById(R.id.status);
        zdjecie = (ImageView) rowView1.findViewById(R.id.imageView);

        if(zm2.get(position)!=null) {
            if (zm4.get(position) != null) {

                try {
                    is = zm4.get(position).getBinaryStream();
                    zdjecie.setImageBitmap(BitmapFactory.decodeStream(is));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Bitmap bm = BitmapFactory.decodeByteArray(zm4.get(position), 0, zm4.size());
                // zdjecie.setImageBitmap(bm);
            } else {
                zdjecie.setImageDrawable(zdjecie.getResources().getDrawable(R.mipmap.car4));
            }


            tekst1.setText(zm1.get(position));
            tekst2.setText(zm2.get(position));
            tekst3.setText(zm3.get(position));
            if(zm3.get(position).equals("Nowy"))
            {
                tekst3.setTextColor(ContextCompat.getColor(context, R.color.close));
            }
            else if(zm3.get(position).equals("Zakończony"))
            {
                tekst3.setTextColor(ContextCompat.getColor(context, R.color.akcept));
            }
        }

        return rowView1;
    }
}