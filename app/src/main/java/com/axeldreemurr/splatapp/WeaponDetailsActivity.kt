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

class WeaponDetailsActivity : AppCompatActivity() {
    private lateinit var tvWpnId: TextView
    private lateinit var tvWpnName: TextView
    private lateinit var tvWpnDesc: TextView
    private lateinit var tvWpnPhoto: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weapon_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("wpnId").toString(),
                intent.getStringExtra("wpnName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("wpnId").toString()
            )
        }
    }

    private fun openUpdateDialog(
        wpnId: String,
        wpnName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etWpnName = mDialogView.findViewById<EditText>(R.id.etWpnName)
        val etWpnDesc = mDialogView.findViewById<EditText>(R.id.etWpnDesc)
        val etWpnPhoto = mDialogView.findViewById<EditText>(R.id.etWpnPhoto)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etWpnName.setText(intent.getStringExtra("wpnName").toString())
        etWpnDesc.setText(intent.getStringExtra("wpnDesc").toString())
        etWpnPhoto.setText(intent.getStringExtra("wpnPhoto").toString())

        mDialog.setTitle("Actualizando el arma '$wpnName'...")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateWpnData(
                wpnId,
                etWpnName.text.toString(),
                etWpnDesc.text.toString(),
                etWpnPhoto.text.toString()
            )

            Toast.makeText(applicationContext, "¡Se actualizaron los datos del arma!", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvWpnName.text = etWpnName.text.toString()
            tvWpnDesc.text = etWpnDesc.text.toString()
            tvWpnPhoto.text = etWpnPhoto.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateWpnData(
        id: String,
        name: String,
        desc: String,
        photo: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Armas").child(id)
        val wpnInfo = WeaponModel(id, name, desc, photo)
        dbRef.setValue(wpnInfo)
    }

    private fun initView() {
        tvWpnId = findViewById(R.id.tvWpnId)
        tvWpnName = findViewById(R.id.tvWpnName)
        tvWpnDesc = findViewById(R.id.tvWpnDesc)
        tvWpnPhoto = findViewById(R.id.tvWpnPhoto)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvWpnId.text = intent.getStringExtra("wpnId")
        tvWpnName.text = intent.getStringExtra("wpnName")
        tvWpnDesc.text = intent.getStringExtra("wpnDesc")
        tvWpnPhoto.text = intent.getStringExtra("wpnPhoto")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Armas").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Arma eliminada exitosamente", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Error al eliminar: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}