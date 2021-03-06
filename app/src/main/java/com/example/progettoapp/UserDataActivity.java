package com.example.progettoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserDataActivity extends AppCompatActivity {

    Button comeBackBtn;
    TextView txtPeso;
    EditText editPeso;
    TextView txtAltezza;
    EditText editAltezza;
    TextView txtEta;
    EditText editEta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);

        comeBackBtn = findViewById(R.id.comeBackBtn);
        txtAltezza = findViewById(R.id.txtAltezza);
        txtEta = findViewById(R.id.txtEta);
        txtPeso = findViewById(R.id.txtPeso);
        editAltezza = findViewById(R.id.altezzaEdit);
        editPeso = findViewById(R.id.pesoEdit);
        editEta = findViewById(R.id.etaEdit);


        final SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(UserDataActivity.this);
        txtAltezza.setText(sharedPref.getString("altezza", ""));
        txtEta.setText(sharedPref.getString("eta", ""));
        txtPeso.setText(sharedPref.getString("peso", ""));

        comeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData();


                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * Salva nelle SharedPreferences i dati di peso altezza ed età inseriti dall'utente
     */
    private void setData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(UserDataActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("peso", editPeso.getText().toString());
        editor.putString("altezza", editAltezza.getText().toString());
        editor.putString("eta", editEta.getText().toString());
        editor.apply();
    }
}