package com.example.tomi.akasztofa23api;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.StrictMath.abs;

public class MainActivity extends AppCompatActivity {

    private ImageView IVakasztofa;
    private TextView TVHibaszamlalo, TVrejtettszoveg, valaszthatobetu;
    private Button Plusz, Minusz, tippelGomb, reset;

    int kepek[]= {R.drawable.akasztofa00, R.drawable.akasztofa01, R.drawable.akasztofa02, R.drawable.akasztofa03, R.drawable.akasztofa04, R.drawable.akasztofa05, R.drawable.akasztofa06, R.drawable.akasztofa07, R.drawable.akasztofa08, R.drawable.akasztofa09, R.drawable.akasztofa10, R.drawable.akasztofa11, R.drawable.akasztofa12, R.drawable.akasztofa13};
    private String[] szavak = {"RAKLAP", "LÁDA", "SZEMETES", "MORZSA PORSZÍVÓ", "HÁTIZSÁK", "TÉRKÉP", "MONITOR", "ÜDÍTŐ", "DOBOZ", "BORÍTÉK", "DEZODOR"};
    Random rnd = new Random();
    private boolean[] betuSzin = new boolean[35];
    private boolean[] betuSzinHelyes = new boolean[35];
    private String[] betu = {"A", "Á", "B", "C","D","E","É","F","G","H","I","Í","J","K","L","M","N","O","Ó","Ö","Ő","P","Q","R","S","T","U","Ú","Ü","Ű","V","W","X","Y","Z"}; //0...34
    private int betuAllas;
    private String gondolt;
    private List<Character> allas = new ArrayList<>();
    private String rejtett = "";
    private int hibak;
    private boolean jatekVege = false;
    private List<Character> betuk = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        Plusz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hibak > 0) {
                    betuAllas++;
                    if (betuAllas > 34) {
                        betuAllas = 0;
                    }
                    BetuAllas();
                }
            }
        });

        Minusz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hibak > 0) {
                    betuAllas--;
                    if (betuAllas < 0) {
                        betuAllas = 34;
                    }
                    BetuAllas();
                }
            }
        });

        tippelGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hibak > 0) {
                    if (betuk.contains(betu[betuAllas].charAt(0))) {
                        if (!betuSzinHelyes[betuAllas]) {
                            //allas.set(betuk.indexOf(betu[betuAllas].charAt(0)), betu[betuAllas].charAt(0));
                            for (int i = 0; i < betuk.size(); i++) {
                                if (betuk.get(i) == betu[betuAllas].charAt(0)) {
                                    allas.set(i, betu[betuAllas].charAt(0));
                                }
                            }
                        }
                        betuSzinHelyes[betuAllas] = true;
                        kiIratas();
                    } else if (!betuk.contains(betu[betuAllas].charAt(0))) {
                        if (!betuSzin[betuAllas]) {
                            hibak--;
                        }
                        betuSzin[betuAllas] = true;
                        kiIratas();
                    }
                    BetuAllas();
                }
                if (hibak<=0){
                    JatekVege(false);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });





    }

    @SuppressLint("ResourceAsColor")
    private void BetuAllas(){
        valaszthatobetu.setText(betu[betuAllas]);
        if (betuSzin[betuAllas]){
            valaszthatobetu.setTextColor(getResources().getColor(R.color.voltDeHibas));
        }else if (betuSzinHelyes[betuAllas]){
            valaszthatobetu.setTextColor(getResources().getColor(R.color.voltFelhasznalva));
        }else {
            valaszthatobetu.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    private void kiIratas(){
        rejtett = "";
        for (int i = 0; i < betuk.size(); i++) {
            if (allas.get(i)=="-".charAt(0)){
                rejtett += ("\n");
            }else {
                rejtett += (" " + Character.toString(allas.get(i)));
            }

        }
        TVrejtettszoveg.setText(rejtett);
        TVHibaszamlalo.setText("lehetőségek: "+hibak);

        IVakasztofa.setImageResource(kepek[abs (hibak-13)]);


        if (!allas.contains("_".charAt(0))){
            JatekVege(true);
        }
    }

    private void gondol(){
        gondolt = szavak[rnd.nextInt(10)];

        for (int il = 0; il < gondolt.length(); il++) {
            betuk.add(gondolt.charAt(il));
            if (Character.isWhitespace(gondolt.charAt(il))){
                allas.add("-".charAt(0));
            }else {
                allas.add("_".charAt(0));
            }
            kiIratas();
        }

        TVrejtettszoveg.setText(rejtett);
    }

    private void JatekVege(boolean nyert){
        jatekVege = true;
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(nyert ? "Helyes megfejtés!" : "A helyes megfejtés: "+gondolt )
                .setMessage("Szeretnél még egyet játszani?")
                .setCancelable(false)
                .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        init();
                    }
                })
                .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .show();
    }

    private void init(){
        IVakasztofa = (ImageView) findViewById(R.id.IVakasztofa);
        TVHibaszamlalo = (TextView) findViewById(R.id.TVHibaszamlalo);
        TVrejtettszoveg = (TextView) findViewById(R.id.TVrejtettszoveg);
        valaszthatobetu = (TextView) findViewById(R.id.valaszthatobetu);
        tippelGomb = (Button) findViewById(R.id.tippelGomb);
        Minusz = (Button) findViewById(R.id.Minusz);
        Plusz = (Button) findViewById(R.id.Plusz);
        reset = (Button) findViewById(R.id.reset);

        jatekVege = false;
        IVakasztofa.setImageResource(kepek[0]);
        betuAllas = 0;
        hibak = 13;
        for (int i=0; i < betuSzin.length; i++) betuSzin[i]=false;
        for (int i=0; i < betuSzinHelyes.length; i++) betuSzinHelyes[i]=false;
        allas.clear();
        betuk.clear();
        valaszthatobetu.setText(betu[betuAllas]);
        valaszthatobetu.setTextColor(getResources().getColor(R.color.colorAccent));
        TVHibaszamlalo.setText("lehetőségek: "+hibak);
        gondol();
    }


}
