package com.example.progettoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Dieta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dieta_activity);

        TextView giornoView;
        TextView categoriaView;
        TextView risView = null;

        Button inserisciBtn;
        Button modificaBtn;
        Button cercaBtn;

        // Associa le view alle TextView rispettive
        categoriaView = findViewById(R.id.textView3);
        giornoView = findViewById(R.id.textView2);
        risView = findViewById(R.id.textView4);

        // Associa i bottoni
        inserisciBtn = findViewById(R.id.inserisciBtn);
        modificaBtn = findViewById(R.id.modificaBtn);
        cercaBtn = findViewById(R.id.cercaBtn);

        Intent intent = getIntent();
        String giorno = intent.getStringExtra("giorno");
        String dieta = intent.getStringExtra("dieta");

        ///////////////////////////////////////////////////////////////////////

        File pathFile = getApplicationContext().getFilesDir();
        String path= pathFile+"/dieta.txt";
        File file = new File(path);
        if (file.exists())
            System.out.println("Il file " + path + " esiste");
        else {
            try {
                if (file.createNewFile())
                    System.out.println("Il file " + path + " è stato creato");
                else
                    System.out.println("Il file " + path + " non può essere creato");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String str = "";
        StringBuilder stringBuilder1 = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while (true) {
                try {
                    if ((str = br.readLine()) == null) break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if(str.equals(giorno + dieta))
                {
                    try {
                        str=br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stringBuilder1.append(str).append("\n");
                    //Commentato così stampa tutta l'alimentaione associata ad un giorno e tipo di dieta
                    //break;
                }
            }

            System.out.print("Il contenuto del file è il seguente: "+ stringBuilder1);
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////

        giornoView.setText(giorno);
        categoriaView.setText(dieta);
        risView.setText(stringBuilder1);

        //Bottoni
        inserisciBtn.setOnClickListener(v -> inserisci(path,giorno+dieta));
        modificaBtn.setOnClickListener(v -> modifica(path,giorno+dieta));
        cercaBtn.setOnClickListener(v -> cerca(stringBuilder1.toString()));

    }

    /**
     * Modifica il file inserendo la dieta associata ad un determinato giorno e pasto della giornata
     * @param path Path del file della dieta
     * @param dieta Pasto della giornata
     */
    private void modifica(String path, String dieta) {

        EditText editText;
        editText = findViewById(R.id.enterText);

        String str = "";
        StringBuilder stringBuilder1 = new StringBuilder();

        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while (true) {
                try {
                    if ((str = br.readLine()) == null) break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if(str.equals(dieta))
                {
                    try {
                        str=br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stringBuilder1.append(dieta).append("\n").append(editText.getText().toString()).append("\n");
                }else{
                    stringBuilder1.append(str).append("\n");
                }


            }

            System.out.print("Il contenuto del file è il seguente: "+ stringBuilder1);
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }


        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(stringBuilder1);
            bw.flush();
            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }



    }

    /**
     * Inseriscenel file la dieta associata ad un determinato giorno e pasto della giornata
     * @param path Path del file della dieta
     * @param dieta Pasto della giornata
     */
    private void inserisci(String path, String dieta) {

        EditText editText;
        editText = findViewById(R.id.enterText);

        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(dieta+"\n"+editText.getText().toString()+"\n");
            bw.flush();
            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Fa partire l'activity per cercare la ricetta su youtube
     * @param pasto cosa cercare
     */
    private void cerca(String pasto) {

        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("cosa", pasto);

        startActivity(intent);


    }
}