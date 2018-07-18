package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Custom_row_zgloszenie extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] zm1;
    TextView txtTitle;

    public Custom_row_zgloszenie(Activity context,
                      String[] zma) {
        super(context, R.layout.activity_custom_row_zgloszenie, zma);
        this.context = context;
        this.zm1 = zma;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView1=inflater.inflate(R.layout.activity_custom_row_zgloszenie, null,true);

        txtTitle = (TextView) rowView1.findViewById(R.id.textView5);

        txtTitle.setText("Realizacja");



        return rowView1;
    }
}