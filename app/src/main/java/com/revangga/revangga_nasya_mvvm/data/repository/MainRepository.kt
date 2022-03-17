package com.revangga.revangga_nasya_mvvm.data.repository

import android.content.Context
import android.util.Log
import com.revangga.revangga_nasya_mvvm.data.local.DogsDao
import com.revangga.revangga_nasya_mvvm.data.local.DogsDatabase
import com.revangga.revangga_nasya_mvvm.data.model.Dog
import com.revangga.revangga_nasya_mvvm.data.model.DogsData
import com.revangga.revangga_nasya_mvvm.data.model.DogsResponse
import com.revangga.revangga_nasya_mvvm.data.network.ApiClient
import com.revangga.revangga_nasya_mvvm.data.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val dogsDao: DogsDao,
    private val ioDispatcher: CoroutineDispatcher
    )

    suspend fun getAllDogs(): DogsResponse? {
        var result: DogsResponse?

        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            result = try {
                retrofit.getAllDogs(limit = 20).body()
            } catch (e: Exception) {
                Log.d("Exception", e.message.toString())
                null
            }
        }
        return result
    }

    suspend fun saveAllDogs(dogsData: List<DogsData>): Boolean {
        var successInsert: Boolean
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val dogs = dogsData.map {
                Dog(null, it.url)
            }
            successInsert = try {
                database.dog().insertAllDogs(dogs)
                true
            } catch (e: Exception) {
                false
            }
        }
        return successInsert
    }

    suspend fun loadAllDogs(): List<Dog> {
        var result: List<Dog>
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            result = database.dog().getAllDogs()
        }
        return result
    }
}