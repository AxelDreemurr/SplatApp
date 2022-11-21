package com.axeldreemurr.splatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    private lateinit var etWpnName: EditText
    private lateinit var etWpnDesc: EditText
    private lateinit var etWpnPhoto: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        etWpnName = findViewById(R.id.etWpnName)
        etWpnDesc = findViewById(R.id.etWpnDesc)
        etWpnPhoto = findViewById(R.id.etWpnPhoto)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Armas")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }
    private fun saveEmployeeData() {

        //getting values
        val wpnName = etWpnName.text.toString()
        val wpnDesc = etWpnDesc.text.toString()
        val wpnPhoto = etWpnPhoto.text.toString()

        if (wpnName.isEmpty()) {
            etWpnName.error = "Por favor, ingresa un nombre para el arma."
        }
        if (wpnDesc.isEmpty()) {
            etWpnDesc.error = "Por favor, ingresa una descripción."
        }
        if (wpnPhoto.isEmpty()) {
            etWpnPhoto.error = "Por favor, ingresa la URL de la imagen."
        }

        val wpnId = dbRef.push().key!!

        val weapon = WeaponModel(wpnId, wpnName, wpnDesc, wpnPhoto)

        dbRef.child(wpnId).setValue(weapon)
            .addOnCompleteListener {
                Toast.makeText(this, "¡Guardado con éxito!", Toast.LENGTH_LONG).show()

                etWpnName.text.clear()
                etWpnDesc.text.clear()
                etWpnPhoto.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Tenemos un problema: ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}

