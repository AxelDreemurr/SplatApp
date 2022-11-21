package com.axeldreemurr.splatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class GearFetchingActivity : AppCompatActivity() {
    private lateinit var gearRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var gearList: ArrayList<GearModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gear_fetching)

        gearRecyclerView = findViewById(R.id.rvEmp)
        gearRecyclerView.layoutManager = LinearLayoutManager(this)
        gearRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        gearList = arrayListOf<GearModel>()

        getEmployeesData()

    }

    private fun getEmployeesData() {

        gearRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Artículos")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gearList.clear()
                if (snapshot.exists()){
                    for (gearSnap in snapshot.children){
                        val gearData = gearSnap.getValue(GearModel::class.java)
                        gearList.add(gearData!!)
                    }
                    val mAdapter = GearAdapter(gearList)
                    gearRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : GearAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@GearFetchingActivity, GearDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("gearId", gearList[position].gearId)
                            intent.putExtra("gearName", gearList[position].gearName)
                            intent.putExtra("gearType", gearList[position].gearType)
                            intent.putExtra("gearDesc", gearList[position].gearDesc)
                            intent.putExtra("gearPhoto", gearList[position].gearPhoto)
                            startActivity(intent)
                        }

                    })

                    gearRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}