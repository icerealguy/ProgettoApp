package com.example.progettoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SensorEventListener {

    ListView listView;
    TextView textView;
    String[] listItem;
    String giorno;

    //Contapassi
    TextView tvPassi;
    SensorManager sensorManager;
    boolean running = false;
    int numpassi=10000; //default
    int day=0;

    //Calendario
    TextView textViewGiorno;
    TextView textViewMese;
    TextView textViewSettimana;

    //conta calorie
    TextView contaCal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar ( tb );

        textViewGiorno = findViewById(R.id.giornoTV);
        textViewMese = findViewById(R.id.meseTV);
        textViewSettimana = findViewById(R.id.settimanaTV);

        System.out.println("SetDate\n");
        setDate();
        System.out.println("DOPO SetDate\n");

        // Spinner
        Spinner spinner = findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // elementi mostrati dallo spinner
        List<String> categories = new ArrayList<String>();
        categories.add("Lunedì");
        categories.add("Martedì");
        categories.add("Mercoledì");
        categories.add("Giovedì");
        categories.add("Venerdì");
        categories.add("Sabato");
        categories.add("Domenica");

        // Crea l'adattatore per lo spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Come mostrare i dati nello spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attacca l'adattatore allo spinner
        spinner.setAdapter(dataAdapter);

        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView);
        listItem = getResources().getStringArray(R.array.array_dieta);
        tvPassi = findViewById(R.id.conteggio);
        contaCal = findViewById(R.id.contaCal);

        final SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String peso = sharedPref.getString("peso", "60" );

        double cal = calcolaCalorie(peso, sharedPref.getInt("passi", numpassi));
        System.out.println("calorie" +cal+"\n");

        contaCal.setText(String.valueOf(cal));
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            // TODO Auto-generated method stub
            String value = adapter.getItem(position);
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

            open(view, giorno, value);
        });
    }

    /**
     * Calcola le calorie bruciate in base al peso e i passi
     * @param peso peso utente
     * @param passi passi effettuati
     * @return
     */
    private double calcolaCalorie(String peso, int passi) {

        int peso1= Integer.parseInt(String.valueOf(peso));
        return peso1*passi * 0.0005;
    }

    /**
     * Prende la data attuale e assegna alle view il rispettivo valore
     */
    private void setDate() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        int day = calendar.get(Calendar.DATE);
        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ITALY);
        String month = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.ITALY);

        textViewGiorno.setText(Integer.toString(day));
        textViewMese.setText(month);
        textViewSettimana.setText(dayOfWeek);
    }


    /**
     * Avvia l'activity Dieta
     * @param view
     * @param giorno Giorno della settimana richiesto
     * @param dieta  Pasto della giornata richiesto
     */
    private void open(View view, String giorno, String dieta) {

            Intent intent = new Intent(this, Dieta.class);

            intent.putExtra("giorno", giorno);
            intent.putExtra("dieta", dieta);
            startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mymenu,menu);
        return true;
    }

    /**
     * Tiene traccia dell'elemento selezionato dal menù
     * @param item elemento selezionato dal menù
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.userData:
                Intent intent= new Intent(getApplicationContext(),UserDataActivity.class);
                startActivity(intent);
                return true;

            case R.id.attivaNot :

                Toast.makeText(this, "Attivate", Toast.LENGTH_SHORT).show();

                setNotification();
                return true;

            case R.id.disattivaNot :

                Toast.makeText(this, "Disattivate", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Tiene traccia dell'elemento selezionato dall'utente nello spinner.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected1: " + item, Toast.LENGTH_LONG).show();

        this.giorno=item;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    /**
     * Richiede l'uso del sensore contapassi
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor =sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor!= null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            tvPassi.setText(String.valueOf(numpassi));
            Toast.makeText(this,"Sensor non trovato", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running=false;
        //se tolgo i commenti smette di contare i passi
        //sensorManager.unregisterListener(this);
    }

    /**
     * Si occupa del conteggio passi giornaliero.
     * Ogni giorno vengono azzerati.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(running){

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            final SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

            if (day==0)
            {
                day = calendar.get(Calendar.DATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("giorno",day);
                editor.putInt("passi",(int) event.values[0]);
                editor.apply();
            }
            else if(day != sharedPref.getInt("giorno",0))
            {
                day = calendar.get(Calendar.DATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("giorno",day);
                editor.putInt("passi",(int) (sharedPref.getInt("passi",0)-event.values[0]));
                editor.apply();

            }
           // tvPassi.setText(String.valueOf(event.values[0]));
            tvPassi.setText(sharedPref.getInt("passi",0));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Crea 10 notifiche giornaliere considerando che in media sono necessari 10 bicchieri d'acqua
     * al giorno per raggiungere i due litri.
     */
    public void setNotification(){
        System.out.println("SetNotification\n");
        scheduleNotification(9,0,0, 100);
        scheduleNotification(10,0,0, 101);
        scheduleNotification(11,0,0, 102);
        scheduleNotification(12,0,0, 103);
        scheduleNotification(13,0,0, 104);
        scheduleNotification(14,0,0, 105);
        scheduleNotification(16,0,0, 106);
        scheduleNotification(18,0,0, 107);
        scheduleNotification(21,0,0, 108);
        scheduleNotification(23,0,0, 109);
    }


    /**
     * Si occupa di schedulare le singole notifiche
     * @param hour ora in cui ricevere la notifica
     * @param minute minuto in cui ricevere la notifica
     * @param second secondo in cui ricevere la notifica
     * @param reqCode codice di richiesta della notifica
     */
    public void scheduleNotification(int hour, int minute, int second, int reqCode) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }
}