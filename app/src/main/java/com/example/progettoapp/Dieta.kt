package com.example.progettoapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettoapp.R
import android.widget.TextView
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.progettoapp.VideoActivity
import java.io.*
import java.lang.StringBuilder

class Dieta : AppCompatActivity() {
    var strCosa: String? = null //variabile necessaria per salvare cosa cercare
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dieta_activity)
        val giornoView: TextView
        val categoriaView: TextView
        val inserisciBtn: Button
        val modificaBtn: Button
        val cercaBtn: Button

        // Associa le view alle TextView rispettive
        categoriaView = findViewById(R.id.textView3)
        giornoView = findViewById(R.id.textView2)

        // Associa i bottoni
        inserisciBtn = findViewById(R.id.inserisciBtn)
        modificaBtn = findViewById(R.id.modificaBtn)
        cercaBtn = findViewById(R.id.cercaBtn)
        val intent = intent
        val giorno = intent.getStringExtra("giorno")
        val dieta = intent.getStringExtra("dieta")

        ///////////////////////////////////////////////////////////////////////
        val pathFile = applicationContext.filesDir
        val path = "$pathFile/dieta.txt"
        val file = File(path)
        if (file.exists()) println("Il file $path esiste") else {
            try {
                if (file.createNewFile()) println("Il file $path è stato creato") else println("Il file $path non può essere creato")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        strCosa = stampaDieta(file, giorno + dieta)

        giornoView.text = giorno
        categoriaView.text = dieta

        //Bottoni
        inserisciBtn.setOnClickListener { v: View? -> inserisci(path, giorno + dieta) }
        modificaBtn.setOnClickListener { v: View? -> modifica(path, giorno + dieta) }
        cercaBtn.setOnClickListener { v: View? -> cerca(strCosa) }
    }

    /**
     *  Mostra la dieta richiesta e la stampa nella view
     * @param file file contenente la dieta
     * @param dieta combinazione pasto+giorno
     */
    private fun stampaDieta(file: File, dieta: String): String {
        val risView: TextView
        risView = findViewById(R.id.textView4)
        var str = ""
        val stringBuilder1 = StringBuilder()
        try {
            val fr = FileReader(file)
            val br = BufferedReader(fr)
            while (true) {
                try {
                    if (br.readLine().also { str = it } == null) break
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (str == dieta) {
                    try {
                        str = br.readLine()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    stringBuilder1.append(str).append("\n")
                    //Commentato così stampa tutta l'alimentaione associata ad un giorno e tipo di dieta
                    //break;
                }
            }
            print("Il contenuto del file è il seguente: $stringBuilder1")
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        risView.text = stringBuilder1
        println("return print $stringBuilder1")
        return stringBuilder1.toString()
    }

    /**
     * Modifica il file inserendo la dieta associata ad un determinato giorno e pasto della giornata
     * @param path Path del file della dieta
     * @param dieta Pasto della giornata
     */
    private fun modifica(path: String, dieta: String) {
        val editText: EditText
        editText = findViewById(R.id.enterText)
        var str = ""
        val stringBuilder1 = StringBuilder()
        try {
            val file = File(path)
            val fr = FileReader(file)
            val br = BufferedReader(fr)
            while (true) {
                try {
                    if (br.readLine().also { str = it } == null) break
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (str == dieta) {
                    try {
                        str = br.readLine()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    stringBuilder1.append(dieta).append("\n").append(editText.text.toString()).append("\n")
                } else {
                    stringBuilder1.append(str).append("\n")
                }
            }
            print("Il contenuto del file è il seguente: $stringBuilder1")
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val file = File(path)
            val fw = FileWriter(file, false)
            val bw = BufferedWriter(fw)
            bw.append(stringBuilder1)
            bw.flush()
            bw.close()
            strCosa = stampaDieta(file, dieta)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Inseriscenel file la dieta associata ad un determinato giorno e pasto della giornata
     * @param path Path del file della dieta
     * @param dieta Pasto della giornata
     */
    private fun inserisci(path: String, dieta: String) {
        val editText: EditText
        editText = findViewById(R.id.enterText)
        try {
            val file = File(path)
            val fw = FileWriter(file, true)
            val bw = BufferedWriter(fw)
            bw.append("""
    $dieta
    ${editText.text}
    
    """.trimIndent())
            bw.flush()
            bw.close()
            strCosa = stampaDieta(file, dieta)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Fa partire l'activity per cercare la ricetta su youtube
     * @param pasto cosa cercare
     */
    private fun cerca(pasto: String?) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("cosa", pasto)
        startActivity(intent)
    }
}