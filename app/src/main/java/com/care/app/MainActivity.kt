package com.care.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var tvLatitude:TextView
    private lateinit var tvLongitude:TextView
    private lateinit var tvDistance:TextView
    private lateinit var tvMap:CardView
    private lateinit var tvRadius:TextView
    private lateinit var btChange:Button
    private lateinit var tvStatus:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var radius=getSharedPreferences("MAIN", Context.MODE_PRIVATE).getInt("RADIUS",100)
        tvLatitude= findViewById(R.id.latitude)
        tvLongitude=findViewById(R.id.longitude)
        tvRadius=findViewById(R.id.radius)
        btChange=findViewById(R.id.change)
        tvMap=findViewById(R.id.map)
        tvDistance=findViewById(R.id.distance)
        tvStatus=findViewById(R.id.status)
        tvMap.setOnClickListener {
            val intent=Intent(this, MapsActivity::class.java)
            startActivity(intent)}
        val viewModel= ViewModelProviders.of(this)[ViewModelData::class.java]
        viewModel.getLat().observe(this, Observer<Double> {latitude-> tvLatitude.text = latitude.toString() })
        viewModel.getLong().observe(this, Observer<Double>{ longitude-> tvLongitude.text = longitude.toString() })
        viewModel.getDist().observe(this, Observer<Double> { distance->tvDistance.text=distance.toUInt().toString()})
        viewModel.getObjectThreat().observe(this, Observer <Int>{i->
            if(i==1)
            tvStatus.text="Object Threat"
            else
            tvStatus.text="Safe"
        })
        viewModel.getFire().observe(this, Observer { i->
            if(i==1)
                tvStatus.text="Fire Threat"
            else
                tvStatus.text="Safe"
        })
        tvRadius.text=radius.toString()
        btChange.setOnClickListener { val alert=AlertDialog.Builder(this)
        alert.setTitle("Set Radius")
        val view =layoutInflater.inflate(R.layout.alert_lay,LinearLayout(this))
            alert.setView(view)
            alert.setPositiveButton("Set"){dialogInterface, i ->
                val r:Int=Integer.parseInt(view.findViewById<EditText>(R.id.et_radius)!!.text.toString())
                tvRadius.text=r.toString()
                radius=r
                getSharedPreferences("MAIN", Context.MODE_PRIVATE).edit().putInt("RADIUS",r).apply()}
        alert.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout)
        {
            val auth=FirebaseAuth.getInstance()
            auth.signOut()
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return true
    }
}
