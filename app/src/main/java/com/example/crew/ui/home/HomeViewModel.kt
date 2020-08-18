package com.example.crew.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.crew.data.FirebaseQueryLiveData
import com.example.crew.data.Hero
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(_application: Application) : AndroidViewModel(_application) {

    private val heroesDatabaseReference: DatabaseReference? =
        FirebaseDatabase.getInstance().reference

    private val firebaseQueryLiveData: FirebaseQueryLiveData =
        FirebaseQueryLiveData(heroesDatabaseReference!!)

    val dataSnapshotLiveData: FirebaseQueryLiveData = firebaseQueryLiveData

    suspend fun getList(dataSnapshot: DataSnapshot): List<Hero> =
        withContext(Dispatchers.Default) {
            val list = mutableListOf<Hero>()
            dataSnapshot.children.forEach {
                list.add(it.getValue(Hero::class.java)!!)
            }
            list
        }

}