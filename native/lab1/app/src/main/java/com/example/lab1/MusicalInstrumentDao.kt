package com.example.lab1

import androidx.lifecycle.LiveData;
import androidx.room.*

@Dao
interface MusicalInstrumentDao {
  @Query("SELECT * from musical_instruments")
  fun findAll(): LiveData<List<MusicalInstrument>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(musicalInstrument: MusicalInstrument) : Long

  @Delete
  suspend fun delete(musicalInstrument: MusicalInstrument)

  @Update
  suspend fun update(musicalInstrument: MusicalInstrument)

  @Query("DELETE FROM musical_instruments")
  suspend fun deleteAll();
}
