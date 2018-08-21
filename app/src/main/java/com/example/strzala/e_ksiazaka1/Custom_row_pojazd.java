package com.example.strzala.e_ksiazaka1;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom_row_pojazd extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> zm1 = new ArrayList<String>();
    ArrayList<String> zm2 = new ArrayList<String>();
    ArrayList<String> zm3 = new ArrayList<String>();
    ImageView txtTitle;
    TextView marka_b,nr_rejestracyjny;
    CheckBox checkBox2;
    int pozycja;

    public Custom_row_pojazd(Activity context,
                             ArrayList<String> zma, ArrayList<String> zma1,ArrayList<String> zma2) {
        super(context, R.layout.activity_custom_row_pojazd, zma);
        this.context = context;
        this.zm3 = zma2;
        this.zm2 = zma;
        this.zm1 = zma1;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView2=inflater.inflate(R.layout.activity_custom_row_pojazd, null,true);

        txtTitle = (ImageView) rowView2.findViewById(R.id.zdjecieP);
        marka_b = (TextView) rowView2.findViewById(R.id.email_lista);
        nr_rejestracyjny = (TextView) rowView2.findViewById(R.id.nr_rejestracyjny);
        checkBox2 = (CheckBox) rowView2.findViewById(R.id.checkBox2);

        pozycja=position;
        marka_b.setText(zm1.get(position));
        nr_rejestracyjny.setText(zm2.get(position));

        if(zm3.get(position).equals("1") & !zm3.get(position).equals("nie")) {

            ///TODO do sprawdzenia status checkbox
            checkBox2.setVisibility(View.VISIBLE);
            checkBox2.setChecked(true);
            //

        }else if(!zm3.get(position).equals("nie"))
        {
            checkBox2.setVisibility(View.VISIBLE);

        }


        //Log.i("Marka",zm2.get(position));
        if(zm2.get(position).contains("fiat") || zm2.get(position).contains("Fiat"))
        {
            txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.fiat));
        }
        else
        {
            txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.car4));
        }

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked )
                {
                    Log.i("Custom_row_pojazd",zm1.get(pozycja));
                    Historia_pojazd.getInstance().update_konfiguracj(zm1.get(pozycja), "1");
                }
                else if (!isChecked )
                {
                    Log.i("Custom_row_pojazd",zm1.get(pozycja));
                    Historia_pojazd.getInstance().update_konfiguracj(zm1.get(pozycja), "0");
                }
            }
        });


        return rowView2;
    }
}