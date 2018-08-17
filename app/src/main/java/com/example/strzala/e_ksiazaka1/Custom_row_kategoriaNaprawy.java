package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom_row_kategoriaNaprawy extends ArrayAdapter<String> {

    TextView kategoria;
    private final Activity context;
    ArrayList<String> zm2 = new ArrayList<String>();
   // public final String[] zm2;

    public Custom_row_kategoriaNaprawy(Activity context,
                                       ArrayList<String> zma) {
        super(context, R.layout.activity_custom_row_kategoria_naprawy, zma);
        this.context = context;
        this.zm2 = zma;

    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView2=inflater.inflate(R.layout.activity_custom_row_kategoria_naprawy, null,true);


        kategoria = (TextView) rowView2.findViewById(R.id.email_punkty);

        kategoria.setText(zm2.get(position));


        return rowView2;
    }
}
