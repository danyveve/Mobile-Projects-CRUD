package com.example.lab1.network

import com.example.lab1.MusicalInstrument

object NetworkRepository {
  suspend fun getMusicalInstruments(): List<MusicalInstrument> {
    val musicalInstruments =
      getMusicalInstrumentsFromNetwork()
    musicalInstruments.size > 0 || return emptyList()
    return musicalInstruments
  }

  suspend fun saveMusicalInstrument(musicalInstrument: MusicalInstrument): Long{
    return saveMusicalInstrumentTroughNetwork(
      musicalInstrument
    );
  }

  suspend fun updateMusicalInstrument(musicalInstrument: MusicalInstrument): MusicalInstrument {
    return updateMusicalInstrumentTroughNetwork(
      musicalInstrument
    );
  }

  suspend fun deleteMusicalInstrumentById(id: Long){
    deleteMusicalInstrumentByIdTroughNetwork(id);
  }

  private suspend fun getMusicalInstrumentsFromNetwork(): List<MusicalInstrument> =
    MusicalInstrumentApi.service.getMusicalInstruments()

  private suspend fun saveMusicalInstrumentTroughNetwork(musicalInstrument: MusicalInstrument) =
    MusicalInstrumentApi.service.saveMusicalInstrument(musicalInstrument)

  private suspend fun updateMusicalInstrumentTroughNetwork(musicalInstrument: MusicalInstrument) =
    MusicalInstrumentApi.service.updateMusicalInstrument(musicalInstrument)

  private suspend fun deleteMusicalInstrumentByIdTroughNetwork(id: Long) =
    MusicalInstrumentApi.service.deleteMusicalInstrumentById(id)
}