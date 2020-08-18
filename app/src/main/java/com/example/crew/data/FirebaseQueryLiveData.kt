package com.example.crew.data


import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*


class FirebaseQueryLiveData : LiveData<DataSnapshot?> {
    private val query: Query
    private val listener =
        MyValueEventListener()

    constructor(query: Query) {
        this.query = query
    }

    constructor(ref: DatabaseReference) {
        query = ref
    }

    override fun onActive() {
        Log.d(LOG_TAG, "onActive")
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        Log.d(LOG_TAG, "onInactive")
        query.removeEventListener(listener)
    }

    fun stopListener() {
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.d(LOG_TAG, "Listening to $query")
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d(
                LOG_TAG,
                "Can't listen to query $query",
                databaseError.toException()
            )
            value = null
        }
    }

    companion object {
        private const val LOG_TAG = "FirebaseQueryLiveData"
    }
}