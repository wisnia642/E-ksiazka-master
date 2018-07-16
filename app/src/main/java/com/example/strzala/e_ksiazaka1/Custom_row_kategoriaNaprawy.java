package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Custom_row_kategoriaNaprawy extends ArrayAdapter<String> {

    TextView kategoria;
    private final Activity context;
    public final String[] zm2;

    public Custom_row_kategoriaNaprawy(Activity context,
                             String[] zma) {
        super(context, R.layout.activity_custom_row_kategoria_naprawy, zma);
        this.context = context;
        this.zm2 = zma;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView2=inflater.inflate(R.layout.activity_custom_row_kategoria_naprawy, null,true);


        kategoria = (TextView) rowView2.findViewById(R.id.kategoria);

        kategoria.setText(zm2[position]);

        return rowView2;
    }
}
