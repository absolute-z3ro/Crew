package com.example.crew.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.crew.data.FirebaseQueryLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeViewModel(_application: Application) : AndroidViewModel(_application) {

    private val heroesDatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference

    private val firebaseQueryLiveData: FirebaseQueryLiveData =
        FirebaseQueryLiveData(heroesDatabaseReference)

    val dataSnapshotLiveData: FirebaseQueryLiveData = firebaseQueryLiveData
}