package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_row_pojazd extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] zm2;
    ImageView txtTitle;
    TextView texkst;

    public Custom_row_pojazd(Activity context,
                                 String[] zma) {
        super(context, R.layout.activity_custom_row_pojazd, zma);
        this.context = context;
        this.zm2 = zma;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView2=inflater.inflate(R.layout.activity_custom_row_pojazd, null,true);

        txtTitle = (ImageView) rowView2.findViewById(R.id.zdjecieP);
        texkst = (TextView) rowView2.findViewById(R.id.textView9);

        texkst.setText("Fiat 126p");


        return rowView2;
    }
}