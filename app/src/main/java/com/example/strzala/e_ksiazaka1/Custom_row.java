package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom_row extends ArrayAdapter<String> {

private final Activity context;
    ArrayList<String> zm1 = new ArrayList<String>();
    ArrayList<String> zm2 = new ArrayList<String>();
    TextView tekst1,tekst2;

public Custom_row(Activity context, ArrayList<String> zma1, ArrayList<String> zma2) {
        super(context, R.layout.activity_custom_row, zma1);
        this.context = context;
        this.zm1 = zma1;
        this.zm2 = zma2;
        }
public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_custom_row, null,true);

     tekst1 = (TextView) rowView.findViewById(R.id.model);
     tekst2 = (TextView) rowView.findViewById(R.id.email_punkty);

     //Log.i("uzytkownik",zm1.get(position)+zm2.get(position));
     tekst1.setText(zm1.get(0));
     try {
         tekst2.setText("Punkty: " + zm2.get(0));
     }catch (Exception e)
     {
         tekst2.setText("Punkty: 0");
         Log.i("CustomRow",""+e);
     }


    return rowView;
}
}


