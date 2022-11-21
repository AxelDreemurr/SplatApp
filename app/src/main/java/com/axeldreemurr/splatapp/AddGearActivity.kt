package com.axeldreemurr.splatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddGearActivity : AppCompatActivity() {
    private lateinit var etGearName: EditText
    private lateinit var etGearType: EditText
    private lateinit var etGearDesc: EditText
    private lateinit var etGearPhoto: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gear)

        etGearName = findViewById(R.id.etGearName)
        etGearType = findViewById(R.id.etGearType)
        etGearDesc = findViewById(R.id.etGearDesc)
        etGearPhoto = findViewById(R.id.etGearPhoto)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Artículos")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }
    private fun saveEmployeeData() {

        //getting values
        val gearName = etGearName.text.toString()
        val gearType = etGearType.text.toString()
        val gearDesc = etGearDesc.text.toString()
        val gearPhoto = etGearPhoto.text.toString()

        if (gearName.isEmpty()) {
            etGearName.error = "Por favor, ingresa un nombre para el arma."
        }
        if (gearType.isEmpty()) {
            etGearType.error = "Por favor, ingresa una descripción."
        }
        if (gearDesc.isEmpty()) {
            etGearDesc.error = "Por favor, ingresa una descripción."
        }
        if (gearPhoto.isEmpty()) {
            etGearPhoto.error = "Por favor, ingresa la URL de la imagen."
        }

        val gearId = dbRef.push().key!!

        val gear = GearModel(gearId, gearName, gearType, gearDesc, gearPhoto)

        dbRef.child(gearId).setValue(gear)
            .addOnCompleteListener {
                Toast.makeText(this, "¡Artículo guardado con éxito!", Toast.LENGTH_LONG).show()

                etGearName.text.clear()
                etGearType.text.clear()
                etGearDesc.text.clear()
                etGearPhoto.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Tenemos un problema: ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}