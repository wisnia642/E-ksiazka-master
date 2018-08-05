package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom_row_zgloszenie extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> zm1 = new ArrayList<String>();
    ArrayList<String> zm2 = new ArrayList<String>();
    ArrayList<String> zm3 = new ArrayList<String>();
    TextView tekst1,tekst2,tekst3;

    public Custom_row_zgloszenie(Activity context,
                      ArrayList<String> zma, ArrayList<String> zma1,ArrayList zma2) {
        super(context, R.layout.activity_custom_row_zgloszenie, zma);
        this.context = context;
        this.zm1 = zma;
        this.zm2 = zma1;
        this.zm3 = zma2;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView1=inflater.inflate(R.layout.activity_custom_row_zgloszenie, null,true);

        tekst1 = (TextView) rowView1.findViewById(R.id.marka);
        tekst2 = (TextView) rowView1.findViewById(R.id.data);
        tekst3 = (TextView) rowView1.findViewById(R.id.status);


        tekst1.setText(zm1.get(position));
        tekst2.setText(zm2.get(position));
        tekst3.setText(zm3.get(position));



        return rowView1;
    }
}