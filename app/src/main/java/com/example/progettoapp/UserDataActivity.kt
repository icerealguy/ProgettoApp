package com.example.progettoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class UserDataActivity : AppCompatActivity() {
    var comeBackBtn: Button? = null
    var txtPeso: TextView? = null
    var editPeso: EditText? = null
    var txtAltezza: TextView? = null
    var editAltezza: EditText? = null
    var txtEta: TextView? = null
    var editEta: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdata)
        comeBackBtn = findViewById(R.id.comeBackBtn)
        txtAltezza = findViewById(R.id.txtAltezza)
        txtEta = findViewById(R.id.txtEta)
        txtPeso = findViewById(R.id.txtPeso)
        editAltezza = findViewById(R.id.altezzaEdit)
        editPeso = findViewById(R.id.pesoEdit)
        editEta = findViewById(R.id.etaEdit)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this@UserDataActivity)
        txtAltezza!!.setText(sharedPref.getString("altezza", ""))
        txtEta!!.setText(sharedPref.getString("eta", ""))
        txtPeso!!.setText(sharedPref.getString("peso", ""))
        comeBackBtn!!.setOnClickListener(View.OnClickListener {
            setData()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        })
    }

    /**
     * Salva nelle SharedPreferences i dati di peso altezza ed età inseriti dall'utente
     */
    private fun setData() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this@UserDataActivity)
        val editor = sharedPref.edit()
        editor.putString("peso", editPeso!!.text.toString())
        editor.putString("altezza", editAltezza!!.text.toString())
        editor.putString("eta", editEta!!.text.toString())
        editor.apply()
    }
}