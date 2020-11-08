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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [MusicalInstrument::class], version = 1)
abstract class MusicalInstrumentsRoomDatabase : RoomDatabase() {

  abstract fun musicalInstrumentDao(): MusicalInstrumentDao

  companion object {
    @Volatile
    private var INSTANCE: MusicalInstrumentsRoomDatabase? = null

    fun getDatabase(
        context: Context,
        scope: CoroutineScope
    ): MusicalInstrumentsRoomDatabase {
      // if the INSTANCE is not null, then return it,
      // if it is, then create the database
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            MusicalInstrumentsRoomDatabase::class.java,
            "musical_instruments_database"
        )
            .addCallback(MusicalInstrumentsDatabaseCallback(scope))
            .build()
        INSTANCE = instance
        // return instance
        instance
      }
    }

    private class MusicalInstrumentsDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      /**
       * Override the onOpen method to populate the database.
       * For this sample, we clear the database every time it is created or opened.
       */
      override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        // If you want to keep the data through app restarts,
        // comment out the following line.
//        INSTANCE?.let { database ->
//          scope.launch(Dispatchers.IO) {
//            populateDatabase(database.musicalInstrumentDao())
//          }
//        }
      }
    }

    suspend fun populateDatabase(musicalInstrumentDao: MusicalInstrumentDao) {
      // Start the app with a clean database every time.
      // Not needed if you only populate on creation.
      musicalInstrumentDao.deleteAll()

//      var musicalInstrument = MusicalInstrument( "GUITAR 1", "STRING", "a very very old guitar", 10,
//        150.35F, "EUR");
//      musicalInstrumentDao.insert(musicalInstrument)
//        musicalInstrument = MusicalInstrument("PIANO 1", "KEYBOARD", "a very very old piano", 23,
//        144.22F, "EUR");
//      musicalInstrumentDao.insert(musicalInstrument)
//      var count = musicalInstrumentDao.findAll().value?.size
    }
  }

}
