package com.axeldreemurr.splatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class GearDetailsActivity : AppCompatActivity() {
    private lateinit var tvGearId: TextView
    private lateinit var tvGearName: TextView
    private lateinit var tvGearDesc: TextView
    private lateinit var tvGearPhoto: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gear_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("gearId").toString(),
                intent.getStringExtra("gearName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("gearId").toString()
            )
        }
    }

    private fun openUpdateDialog(
        gearId: String,
        gearName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.gear_update_dialog, null)

        mDialog.setView(mDialogView)

        val etGearName = mDialogView.findViewById<EditText>(R.id.etGearName)
        val etGearDesc = mDialogView.findViewById<EditText>(R.id.etGearDesc)
        val etGearPhoto = mDialogView.findViewById<EditText>(R.id.etGearPhoto)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etGearName.setText(intent.getStringExtra("gearName").toString())
        etGearDesc.setText(intent.getStringExtra("gearDesc").toString())
        etGearPhoto.setText(intent.getStringExtra("gearPhoto").toString())

        mDialog.setTitle("Actualizando el artículo '$gearName'...")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateWpnData(
                gearId,
                etGearName.text.toString(),
                etGearDesc.text.toString(),
                etGearPhoto.text.toString()
            )

            Toast.makeText(applicationContext, "¡Se actualizaron los datos del artículo!", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvGearName.text = etGearName.text.toString()
            tvGearDesc.text = etGearDesc.text.toString()
            tvGearPhoto.text = etGearPhoto.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateWpnData(
        id: String,
        name: String,
        desc: String,
        photo: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Artículos").child(id)
        val gearInfo = GearModel(id, name, desc, photo)
        dbRef.setValue(gearInfo)
    }

    private fun initView() {
        tvGearId = findViewById(R.id.tvGearId)
        tvGearName = findViewById(R.id.tvGearName)
        tvGearDesc = findViewById(R.id.tvGearDesc)
        tvGearPhoto = findViewById(R.id.tvGearPhoto)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvGearId.text = intent.getStringExtra("gearId")
        tvGearName.text = intent.getStringExtra("gearName")
        tvGearDesc.text = intent.getStringExtra("gearDesc")
        tvGearPhoto.text = intent.getStringExtra("gearPhoto")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Artículos").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Artículo eliminado exitosamente", Toast.LENGTH_LONG).show()

            val intent = Intent(this, GearFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Error al eliminar: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}