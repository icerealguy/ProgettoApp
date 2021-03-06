package com.example.progettoapp

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import java.util.*

class MainActivity : AppCompatActivity(), OnItemSelectedListener, SensorEventListener {
    var listView: ListView? = null
    var textView: TextView? = null
    lateinit var listItem: Array<String>
    var giorno: String? = null

    //Contapassi
    var tvPassi: TextView? = null
    var sensorManager: SensorManager? = null
    var running = false
    var numpassi = 10000 //default
    var day = 0
    var passioggi=0
    var passiieri=0

    //Calendario
    var textViewGiorno: TextView? = null
    var textViewMese: TextView? = null
    var textViewSettimana: TextView? = null

    //conta calorie
    var contaCal: TextView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //chiede permessi sensore movimento
        setupPermissions()

        val tb = findViewById<Toolbar>(R.id.app_bar)
        setSupportActionBar(tb)
        textViewGiorno = findViewById(R.id.giornoTV)
        textViewMese = findViewById(R.id.meseTV)
        textViewSettimana = findViewById(R.id.settimanaTV)
        println("SetDate\n")
        setDate()
        println("DOPO SetDate\n")

        // Spinner
        val spinner = findViewById<Spinner>(R.id.spinner)

        // Spinner click listener
        spinner.onItemSelectedListener = this

        // elementi mostrati dallo spinner
        val categories: MutableList<String> = ArrayList()
        categories.add("Lunedì")
        categories.add("Martedì")
        categories.add("Mercoledì")
        categories.add("Giovedì")
        categories.add("Venerdì")
        categories.add("Sabato")
        categories.add("Domenica")

        // Crea l'adattatore per lo spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        // Come mostrare i dati nello spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Attacca l'adattatore allo spinner
        spinner.adapter = dataAdapter
        listView = findViewById(R.id.listView)
        textView = findViewById(R.id.textView)
        listItem = resources.getStringArray(R.array.array_dieta)
        tvPassi = findViewById(R.id.conteggio)
        contaCal = findViewById(R.id.contaCal)


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val peso = sharedPref.getString("peso", "60")

        val cal = calcolaCalorie(peso, passioggi)
        println("calorie$cal\n")
        setCal(peso, passioggi);

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem)
        listView!!.adapter = adapter
        listView!!.onItemClickListener = OnItemClickListener { adapterView: AdapterView<*>?, view: View, position: Int, l: Long ->
            // TODO Auto-generated method stub
            val value = adapter.getItem(position)
            Toast.makeText(applicationContext, value, Toast.LENGTH_SHORT).show()
            open(view, giorno, value)
        }
    }


    /**
     * Setta la view relativa alle calorie bruciate, richiamata nel ccontapassi si aggiorna
     * @param peso utente
     * @param passioggi passi effettuati in giornata
     */
    private fun setCal(peso: String?, passioggi: Int) {

        val cal = calcolaCalorie(peso, passioggi)
        println("calorie$cal\n")
        contaCal!!.setText(cal.toString())

    }

    /**
     * Permessi per sensore movimento per contapassi
     */
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                123)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            123 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
        }
    }

    /**
     * Calcola le calorie bruciate in base al peso e i passi
     * @param peso peso utente
     * @param passi passi effettuati
     * @return
     */
    private fun calcolaCalorie(peso: String?, passi: Int): Double {
        val peso1 = peso.toString().toInt()
        return peso1 * passi * 0.0005
    }

    /**
     * Prende la data attuale e assegna alle view il rispettivo valore
     */
    private fun setDate() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val day = calendar[Calendar.DATE]
        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ITALY)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALY)
        textViewGiorno!!.text = Integer.toString(day)
        textViewMese!!.text = month
        textViewSettimana!!.text = dayOfWeek
    }

    /**
     * Avvia l'activity Dieta
     * @param view
     * @param giorno Giorno della settimana richiesto
     * @param dieta  Pasto della giornata richiesto
     */
    private fun open(view: View, giorno: String?, dieta: String?) {
        val intent = Intent(this, Dieta::class.java)
        intent.putExtra("giorno", giorno)
        intent.putExtra("dieta", dieta)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    /**
     * Tiene traccia dell'elemento selezionato dal menù
     * @param item elemento selezionato dal menù
     * @return boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.userData -> {
                val intent = Intent(applicationContext, UserDataActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.attivaNot -> {
                Toast.makeText(this, "Attivate", Toast.LENGTH_SHORT).show()
                setNotification()
                true
            }
            R.id.disattivaNot -> {
                Toast.makeText(this, "Disattivate", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Tiene traccia dell'elemento selezionato dall'utente nello spinner.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        // On selecting a spinner item
        val item = parent.getItemAtPosition(position).toString()

        // Showing selected spinner item
        Toast.makeText(parent.context, "Selected1: $item", Toast.LENGTH_LONG).show()
        giorno = item
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO Auto-generated method stub
    }

    /**
     * Richiede l'uso del sensore contapassi
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()
        running = true
        val countSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (countSensor != null) {
            sensorManager!!.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            tvPassi!!.text = numpassi.toString()
            Toast.makeText(this, "Sensor non trovato", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        //se tolgo i commenti smette di contare i passi
        //sensorManager.unregisterListener(this);
    }



    /**
     * Si occupa del conteggio passi giornaliero.
     * Ogni giorno vengono azzerati.
     * @param event
     */
    override fun onSensorChanged(event: SensorEvent) {



        if (running) {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)



            if ((day != sharedPref.getInt("giorno", 0)) || (day ==0)) {
                day = calendar[Calendar.DATE]
                val editor = sharedPref.edit()
                editor.putInt("giorno", day)
                passiieri = passioggi
                passioggi = 0
                editor.apply()
            }
            else
            {
                passioggi = event.values[0].toInt() - passiieri
            }
            //tvPassi!!.setText((event.values[0]).toString())
            setCal(sharedPref.getString("peso","60"),passioggi)
            tvPassi!!.setText(passioggi.toString())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    /**
     * Crea 10 notifiche giornaliere considerando che in media sono necessari 10 bicchieri d'acqua
     * al giorno per raggiungere i due litri.
     */
    fun setNotification() {
        println("SetNotification\n")
        scheduleNotification(9, 0, 0, 100)
        scheduleNotification(10, 0, 0, 101)
        scheduleNotification(10, 54, 0, 102)
        scheduleNotification(12, 0, 0, 103)
        scheduleNotification(13, 0, 0, 104)
        scheduleNotification(14, 0, 0, 105)
        scheduleNotification(16, 0, 0, 106)
        scheduleNotification(18, 0, 0, 107)
        scheduleNotification(21, 0, 0, 108)
        scheduleNotification(23, 0, 0, 109)
    }

    /**
     * Si occupa di schedulare le singole notifiche
     * @param hour ora in cui ricevere la notifica
     * @param minute minuto in cui ricevere la notifica
     * @param second secondo in cui ricevere la notifica
     * @param reqCode codice di richiesta della notifica
     */
    fun scheduleNotification(hour: Int, minute: Int, second: Int, reqCode: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }
}
