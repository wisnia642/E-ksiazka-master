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
    String tab[] = {"Acura","Alfa","Audi","Bmw","Chrysler","Citroen","Dacia","Dodge",
    "Fiat","Ford","Gm","Hiundai","Honda","Infiniti","Jaguar","Jeep","Kia","Lancia","Land",
    "Lexus","Mazda","Mercedes","Mini","Mitsubishi","Nissan","Opel","Peugeot","Porshe",
    "Renault","Saab","Seat","Skoda","Subaru","Suzuki","Toyota","Volvo","volkswag"};

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

        for(int i=0;i<tab.length;i++) {
            if(zm2.get(position).contains(tab[i])) {

                switch (i)
                {
                    case 0:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.acura));
                        break;
                    case 1:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.alfaromeo));
                        break;
                    case 2:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.audi));
                        break;
                    case 3:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.bmw));
                        break;
                    case 4:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.chrysler));
                        break;
                    case 5:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.citroen));
                        break;
                    case 6:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.dacia));
                        break;
                    case 7:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.dodge));
                        break;
                    case 8:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.fiat));
                        break;
                    case 9:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.ford));
                        break;
                    case 10:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.gm));
                        break;
                    case 11:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.hiundai));
                        break;
                    case 12:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.honda));
                        break;
                    case 13:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.infiniti));
                        break;
                    case 14:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.jaguar));
                        break;
                    case 15:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.jeep));
                        break;
                    case 16:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.kia));
                        break;
                    case 17:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.lancia));
                        break;
                    case 18:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.landrover));
                        break;
                    case 19:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.lexus));
                        break;
                    case 20:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.mazda));
                        break;
                    case 21:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.mercedes));
                        break;
                    case 22:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.mini));
                        break;
                    case 23:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.mitsubishi));
                        break;
                    case 24:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.nissan));
                        break;
                    case 25:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.opel));
                        break;
                    case 26:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.peugot));
                        break;
                    case 27:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.porshe));
                        break;
                    case 28:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.renault));
                        break;
                    case 29:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.saab));
                        break;
                    case 30:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.seat));
                        break;
                    case 31:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.skoda));
                        break;
                    case 32:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.subaru));
                        break;
                    case 33:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.suzuki));
                        break;
                    case 34:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.toyota));
                        break;
                    case 35:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.volvo));
                        break;
                    case 36:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.vw));
                        break;

                    default:
                        txtTitle.setImageDrawable(txtTitle.getResources().getDrawable(R.mipmap.car4));
                }
            }
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