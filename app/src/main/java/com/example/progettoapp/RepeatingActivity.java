package com.example.progettoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RepeatingActivity extends AppCompatActivity {

    Button btnBevuto;
    ProgressBar progressBar;
    int progress;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeating);

        btnBevuto = findViewById(R.id.btnBevuto);
        progressBar = findViewById(R.id.progressBar);
        progress = getProgress();
        progressBar.setProgress(progress,true);

        //Bottone premuto dall'utente dopo aver bevuto un bicchiere d'acqua
        btnBevuto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                progress= incrementaProgresso();
                progressBar.setProgress(progress, true);
            }
        });

    }

    /**
     * Incrementa il progresso perchè l'utente ha bevuto un bicchiere d'acqua a seguito
     * di una notifica
     * @return progresso incrementato
     */
    private int incrementaProgresso() {
        int progresso = 0;

        File pathFile = getApplicationContext().getFilesDir();
        String path= pathFile+"/acqua.txt";
        File file = new File(path);
        String str = "";
        StringBuilder stringBuilder1 = new StringBuilder();
        try {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        str = br.readLine();
        stringBuilder1.append(str).append("\n");
        str = br.readLine();
        progresso=Integer.parseInt(str)+10;
        stringBuilder1.append(progresso).append("\n");
        System.out.print("Il contenuto del file è il seguente: "+ stringBuilder1);
        br.close();
    } catch(IOException e) {
        e.printStackTrace();
    }

        try {
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(stringBuilder1);
            bw.flush();
            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    return progresso;
    }

    /**
     * Crea il file dove viene salvato il progresso dei bicchieri d'acqua se non esiste,
     * se esiste già prende il valore del progresso
     * @return progresso bicchieri d'acqua
     */
    private int getProgress() {

        int progresso = 0;

        File pathFile = getApplicationContext().getFilesDir();
        String path= pathFile+"/acqua.txt";
        File file = new File(path);
        if (file.exists())
        {
            System.out.println("Il file " + path + " esiste");
            String str = "";
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                System.out.print("\nSTR prima readline  "+ str);
                str = br.readLine();
                System.out.print("\nSTR dopo readline  "+ str);
                System.out.print("\nSTR int value readline  "+ Integer.valueOf(str));
                if(Integer.valueOf(str).equals(getGiornoAttuale())){
                    str = br.readLine();
                    progresso =Integer.parseInt(str);
                }
                else{
                    modificaGiorno();
                }


                System.out.print("Il contenuto del file è il seguente: "+ str);
                br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                if (file.createNewFile())
                {
                    System.out.println("Il file " + path + " è stato creato");
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.append(getGiornoAttuale()+"\n");
                    bw.append(0+"\n");
                    bw.flush();
                    bw.close();
                }

                else
                    System.out.println("Il file " + path + " non può essere creato");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return progresso;
    }

    /**
     * Modifica il giorno nel file azzerando il progresso dei bicchieri d'acqua
     * @throws IOException
     */
    private void modificaGiorno() throws IOException {

        File pathFile = getApplicationContext().getFilesDir();
        String path= pathFile+"/acqua.txt";
        File file = new File(path);
        System.out.println("modifico il giorno");
        FileWriter fw = new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.append(getGiornoAttuale()+"\n");
        bw.append(0+"\n");
        bw.flush();
        bw.close();

    }

    /**
     * Prende il giorno attuale
     * @return giorno attuale
     */
    private Integer getGiornoAttuale() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return calendar.get(Calendar.DATE);
    }


}