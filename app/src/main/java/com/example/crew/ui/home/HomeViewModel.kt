package com.example.crew.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.crew.data.Hero
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class HomeViewModel(private val _application: Application) : AndroidViewModel(_application) {

    private fun readAsset(): String {
        Log.d("ViewModel", "readasset")
        return _application.applicationContext.assets.open("heroes.json").bufferedReader()
            .use { it.readText() }
    }

    private fun jsonStringToList(jsonString: String): List<Hero> {
        val type: Type = Types.newParameterizedType(List::class.java, Hero::class.java)
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<List<Hero>> = moshi.adapter(type)
        return jsonAdapter.fromJson(jsonString) ?: emptyList()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun readJsonSuspending(): List<Hero> =
        withContext(Dispatchers.IO) {
            Log.d("ViewModel", "readInputStream")
            val jsonString = readAsset()
            jsonStringToList(jsonString)
        }

    val listOfHeroes: LiveData<List<Hero>> = liveData {
        Log.d("ViewModel", "live data")
        emit(readJsonSuspending())
    }
}