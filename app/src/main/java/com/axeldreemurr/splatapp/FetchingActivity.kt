package com.axeldreemurr.splatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var wpnRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var wpnList: ArrayList<WeaponModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        wpnRecyclerView = findViewById(R.id.rvEmp)
        wpnRecyclerView.layoutManager = LinearLayoutManager(this)
        wpnRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        wpnList = arrayListOf<WeaponModel>()

        getEmployeesData()

    }

    private fun getEmployeesData() {

        wpnRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Armas")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                wpnList.clear()
                if (snapshot.exists()){
                    for (wpnSnap in snapshot.children){
                        val wpnData = wpnSnap.getValue(WeaponModel::class.java)
                        wpnList.add(wpnData!!)
                    }
                    val mAdapter = WpnAdapter(wpnList)
                    wpnRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : WpnAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, WeaponDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("wpnId", wpnList[position].wpnId)
                            intent.putExtra("wpnName", wpnList[position].wpnName)
                            intent.putExtra("wpnDesc", wpnList[position].wpnDesc)
                            intent.putExtra("wpnPhoto", wpnList[position].wpnPhoto)
                            startActivity(intent)
                        }

                    })

                    wpnRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}