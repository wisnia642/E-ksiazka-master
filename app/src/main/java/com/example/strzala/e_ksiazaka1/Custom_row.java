package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

public class Custom_row extends ArrayAdapter<String> {

private final Activity context;
public final String[] zm;
    TextView txtTitle;

public Custom_row(Activity context, String[] zma) {
        super(context, R.layout.activity_custom_row, zma);
        this.context = context;
        this.zm = zma;
        }
public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_custom_row, null,true);

     txtTitle = (TextView) rowView.findViewById(R.id.textView4);

     txtTitle.setText(zm[position]);


    return rowView;
}
}


