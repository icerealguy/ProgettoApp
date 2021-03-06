package com.example.progettoapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.util.*

class RepeatingActivity : AppCompatActivity() {
    var btnBevuto: Button? = null
    var progressBar: ProgressBar? = null
    var waterProg = 0

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeating)
        btnBevuto = findViewById(R.id.btnBevuto)
        progressBar = findViewById(R.id.progressBar)
        waterProg = getWaterProgress()
        progressBar!!.setProgress(waterProg, true)

        //Bottone premuto dall'utente dopo aver bevuto un bicchiere d'acqua
        btnBevuto!!.setOnClickListener(View.OnClickListener {
            waterProg = incrementaProgresso()
            progressBar!!.setProgress(waterProg, true)
        })
    }

    /**
     * Incrementa il progresso perchè l'utente ha bevuto un bicchiere d'acqua a seguito
     * di una notifica
     * @return progresso incrementato
     */
    private fun incrementaProgresso(): Int {
        var progresso = 0
        val pathFile = applicationContext.filesDir
        val path = "$pathFile/acqua.txt"
        val file = File(path)
        var str = ""
        val stringBuilder1 = StringBuilder()
        try {
            val fr = FileReader(file)
            val br = BufferedReader(fr)
            str = br.readLine()
            stringBuilder1.append(str).append("\n")
            str = br.readLine()
            progresso = str.toInt() + 10
            stringBuilder1.append(progresso).append("\n")
            print("Il contenuto del file è il seguente: $stringBuilder1")
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val fw = FileWriter(file, false)
            val bw = BufferedWriter(fw)
            bw.append(stringBuilder1)
            bw.flush()
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return progresso
    }

    /**
     * Crea il file dove viene salvato il progresso dei bicchieri d'acqua se non esiste,
     * se esiste già prende il valore del progresso
     * @return progresso bicchieri d'acqua
     */
    private fun getWaterProgress(): Int {
        var progresso = 0
        val pathFile = applicationContext.filesDir
        val path = "$pathFile/acqua.txt"
        val file = File(path)
        if (file.exists()) {
            println("Il file $path esiste")
            var str = ""
            try {
                val fr = FileReader(file)
                val br = BufferedReader(fr)
                print("\nSTR prima readline  $str")
                str = br.readLine()
                print("\nSTR dopo readline  $str")
                print("""
    
    STR int value readline  ${Integer.valueOf(str)}
    """.trimIndent())
                if (Integer.valueOf(str) == giornoAttuale) {
                    str = br.readLine()
                    progresso = str.toInt()
                } else {
                    modificaGiorno()
                }
                print("Il contenuto del file è il seguente: $str")
                br.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            try {
                if (file.createNewFile()) {
                    println("Il file $path è stato creato")
                    val fw = FileWriter(file, true)
                    val bw = BufferedWriter(fw)
                    bw.append("""
    $giornoAttuale
    
    """.trimIndent())
                    bw.append("""
    0
    
    """.trimIndent())
                    bw.flush()
                    bw.close()
                } else println("Il file $path non può essere creato")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return progresso
    }

    /**
     * Modifica il giorno nel file azzerando il progresso dei bicchieri d'acqua
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun modificaGiorno() {
        val pathFile = applicationContext.filesDir
        val path = "$pathFile/acqua.txt"
        val file = File(path)
        println("modifico il giorno")
        val fw = FileWriter(file, false)
        val bw = BufferedWriter(fw)
        bw.append("""
    $giornoAttuale
    
    """.trimIndent())
        bw.append("""
    0
    
    """.trimIndent())
        bw.flush()
        bw.close()
    }

    /**
     * Prende il giorno attuale
     * @return giorno attuale
     */
    private val giornoAttuale: Int
        private get() {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            return calendar[Calendar.DATE]
        }
}