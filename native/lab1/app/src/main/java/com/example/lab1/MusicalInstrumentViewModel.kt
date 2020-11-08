/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lab1

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lab1.network.NetworkRepository
import kotlinx.coroutines.*

/**
 * View Model to keep a reference to the musical instrument repository and
 * an up-to-date list of all musical instruments.
 */
class MusicalInstrumentViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private var id: Long = 0
        fun useOneId(): Long {
            id -= 1
            return id
        }

        var isOnline: Boolean = false
        fun setIsOnline(status: Boolean) {
            isOnline = status
        }
    }

    // The ViewModel maintains a reference to the repository to get data.
    var repository: MusicalInstrumentRepository
    // LiveData gives us updated musical instruments when they change.
    val allMusicalInstruments: LiveData<List<MusicalInstrument>>

    val stateLiveData = MutableLiveData<MyViewState>()
    fun getStateLiveData(): LiveData<MyViewState> = stateLiveData

    var showSnackBar = true

    init {
        // Gets reference to MusicalInstrumentDao from MusicalInstrumentRoomDatabase to construct
        // the correct MusicalInstrumentRepository.
        val musicalInstrumentDao =
            MusicalInstrumentsRoomDatabase.getDatabase(application, viewModelScope)
                .musicalInstrumentDao()
        repository = MusicalInstrumentRepository(musicalInstrumentDao)
        allMusicalInstruments = repository.allMusicalInstruments
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on the mainthread, blocking
     * the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called viewModelScope which we
     * can use here.
     */
    fun insert(musicalInstrument: MusicalInstrument, applicationContext: Context) = viewModelScope.launch {
        stateLiveData.value = MyViewState.Loading
        try{
            if (isOnline) {
                val id = saveMusicalInstrument(musicalInstrument)
                musicalInstrument.id = id
                musicalInstrument.synchronisedWithServer = true
            } else {
                musicalInstrument.id = useOneId()
                musicalInstrument.synchronisedWithServer = false
            }

            val result = repository.insert(musicalInstrument)
            stateLiveData.value = MyViewState.Success(musicalInstrument)
        } catch (e: Exception){
            if(showSnackBar){
                Toast.makeText(
                    applicationContext,
                    R.string.fetch_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
            stateLiveData.value = MyViewState.Error(e)
        } catch (e: Error){
            if(showSnackBar){
                Toast.makeText(
                    applicationContext,
                    R.string.fetch_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
            stateLiveData.value = MyViewState.Error(e)
        }
    }

    fun delete(musicalInstrument: MusicalInstrument, applicationContext: Context) =
        viewModelScope.launch {
            try{
                deleteMusicalInstrumentById(musicalInstrument.id)
                repository.delete(musicalInstrument)
            } catch(e: Exception){
                Toast.makeText(
                    applicationContext,
                    R.string.operation_not_available_untill_online,
                    Toast.LENGTH_LONG
                ).show()
            } catch(e: Error){
                Toast.makeText(
                    applicationContext,
                    R.string.operation_not_available_untill_online,
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    fun update(musicalInstrument: MusicalInstrument, applicationContext: Context) =
        viewModelScope.launch {
            try{
                updateMusicalInstrument(musicalInstrument)
                repository.update(musicalInstrument)
            } catch(e: Exception){
                Toast.makeText(
                    applicationContext,
                    R.string.operation_not_available_untill_online,
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    fun fetchData(applicationContext: Context) {
        viewModelScope.launch {
            try {
                //insert from local db the ones that were not synchronised
                allMusicalInstruments.value?.forEach {
                    if(!it.synchronisedWithServer){
                        saveMusicalInstrument(it)
                    }
                }
                //reload from server
                val instrumentsFromServer: List<MusicalInstrument> = getNewMusicalInstruments()
                repository.deleteAll()
                instrumentsFromServer.forEach {
                    repository.insert(it)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    R.string.fetch_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * @throws IllegalStateException
     */
    private suspend fun getNewMusicalInstruments() = withContext(Dispatchers.IO) {
        NetworkRepository.getMusicalInstruments()
    }

    /**
     * @throws IllegalStateException
     */
    private suspend fun saveMusicalInstrument(musicalInstrument: MusicalInstrument): Long =
        withContext(Dispatchers.IO) {
            NetworkRepository.saveMusicalInstrument(musicalInstrument)
        }

    /**
     * @throws IllegalStateException
     */
    private suspend fun updateMusicalInstrument(musicalInstrument: MusicalInstrument): MusicalInstrument =
        withContext(Dispatchers.IO) {
            NetworkRepository.updateMusicalInstrument(musicalInstrument)
        }

    /**
     * @throws IllegalStateException
     */
    private suspend fun deleteMusicalInstrumentById(id: Long) = withContext(Dispatchers.IO) {
        NetworkRepository.deleteMusicalInstrumentById(id)
    }
}

sealed class MyViewState {
    object Loading : MyViewState()
    data class Error(val throwable: Throwable) : MyViewState()
    data class Success(val data: Any) : MyViewState()
}
