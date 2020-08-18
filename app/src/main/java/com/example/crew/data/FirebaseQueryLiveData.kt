package com.example.crew.data


import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*


class FirebaseQueryLiveData : LiveData<DataSnapshot?> {
    private val query: Query
    private val listener = MyValueEventListener()
    private var listenerRemovePending = false
    private val handler = Handler()
    private val removeListener: Runnable


    constructor(query: Query) {
        this.query = query
        removeListener = Runnable {
            query.removeEventListener(listener)
            listenerRemovePending = false
        }
    }

    constructor(ref: DatabaseReference) {
        query = ref
        removeListener = Runnable {
            query.removeEventListener(listener)
            listenerRemovePending = false
        }
    }

    override fun onActive() {
        Log.d(LOG_TAG, "onActive")
        if (listenerRemovePending) handler.removeCallbacks(removeListener)
        else query.addValueEventListener(listener)
        listenerRemovePending = false
    }

    override fun onInactive() {
        Log.d(LOG_TAG, "onInactive")
        handler.postDelayed(removeListener, 2000L)
        listenerRemovePending = true
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
            Log.d(LOG_TAG, "Can't listen to query $query", databaseError.toException())
            value = null
        }
    }

    companion object {
        private const val LOG_TAG = "FirebaseQueryLiveData"
    }
}