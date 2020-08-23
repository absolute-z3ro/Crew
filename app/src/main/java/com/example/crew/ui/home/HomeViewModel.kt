package com.example.crew.ui.home

import androidx.lifecycle.ViewModel
import com.example.crew.data.FirebaseQueryLiveData
import com.example.crew.data.Hero
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val heroesDatabaseReference: DatabaseReference? =
        FirebaseDatabase.getInstance().reference

    val firebaseQueryLiveData = FirebaseQueryLiveData(heroesDatabaseReference!!)

    suspend fun getList(dataSnapshot: DataSnapshot): List<Hero> = withContext(Dispatchers.Default) {
        val list = mutableListOf<Hero>()
        dataSnapshot.children.forEach {
            list.add(it.getValue(Hero::class.java)!!)
        }
        list
    }

    var list = emptyList<Hero>()

}