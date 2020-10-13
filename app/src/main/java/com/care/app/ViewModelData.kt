package com.care.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.*


class ViewModelData: ViewModel() {
    init {
        getData()
    }

    private fun getData() {
        val db=FirebaseFirestore.getInstance()
        db.collection("data").document("0").addSnapshotListener { snapshot, e ->if (e != null) {
            return@addSnapshotListener
        }

            if (snapshot != null && snapshot.exists()) {
                val model:Model= snapshot.toObject(Model::class.java)!!
                latitude.value=model.latitude
                longitude.value=model.longitude
                distance.value=getDistance(latitude.value!!,23.814110,longitude.value!!,86.441207,0.0,0.0)
            }
        }
        db.collection("fire").document("0").addSnapshotListener{snapshot,e->if (e != null) {
            return@addSnapshotListener
        }

            if (snapshot != null && snapshot.exists()) {
                fire.value=snapshot.getLong("value")!!.toInt()

            }}
        db.collection("fire").document("1").addSnapshotListener{snapshot,e->if (e != null) {
            return@addSnapshotListener
        }
            if (snapshot != null && snapshot.exists()) {
                objectThreat.value=snapshot.getLong("value")!!.toInt()

            }}
    }

    private var distance=MutableLiveData<Double>()

    private var latitude=MutableLiveData<Double>()

    private var longitude=MutableLiveData<Double>()

    private var fire= MutableLiveData<Int>()

    private var objectThreat= MutableLiveData<Int>()

    fun getObjectThreat():LiveData<Int>{
        return objectThreat
    }

    fun getFire():LiveData<Int>{
        return fire
    }

    fun getLat(): LiveData<Double> {
        return latitude
    }
    fun getLong(): LiveData<Double> {
        return longitude
    }
    fun getDist(): LiveData<Double> {
       return distance
    }

    private fun getDistance(lat1: Double, lat2: Double, lon1: Double,
                            lon2: Double, el1: Double, el2: Double): Double {
        val r = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
                * sin(lonDistance / 2) * sin(lonDistance / 2)))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = r * c * 1000 // convert to meters
        val height = el1 - el2
        distance = distance.pow(2.0) + Math.pow(height, 2.0)
        return sqrt(distance)
    }
}