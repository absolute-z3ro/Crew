package com.example.crew.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.beust.klaxon.Klaxon
import com.example.crew.data.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(private val _application: Application) : AndroidViewModel(_application) {

    private fun readAsset(): String {
        Log.d("ViewModel", "readasset")
        return _application.applicationContext.assets.open("heroes.json").bufferedReader()
            .use { it.readText() }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun readJsonSuspending(): List<Hero> =
        withContext(Dispatchers.IO) {
            Log.d("ViewModel", "readInputStream")
            val jsonString = readAsset()
            Klaxon().parseArray<Hero>(jsonString) ?: emptyList()
        }

    val listOfHeroes: LiveData<List<Hero>> = liveData {
        Log.d("ViewModel", "live data")
        emit(readJsonSuspending())
    }
}