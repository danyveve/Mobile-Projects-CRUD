package com.example.lab1

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

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class MusicalInstrumentDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var musicalInstrumentDao: MusicalInstrumentDao
    private lateinit var db: MusicalInstrumentsRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MusicalInstrumentsRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        musicalInstrumentDao = db.musicalInstrumentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMusicalInstrument() = runBlocking {
        val musicalInstrument = MusicalInstrument(
            -1, "GUITAR 1", "STRING", "a very very old guitar", 10,
            150.35F,
            "EUR"
        )
        musicalInstrumentDao.insert(musicalInstrument)
        val allMusicalInstruments = musicalInstrumentDao.findAll().waitForValue()
        assertEquals(allMusicalInstruments[0].name, musicalInstrument.name)
        assertEquals(allMusicalInstruments[0].id, musicalInstrument.id)
        assertEquals(allMusicalInstruments[0].synchronisedWithServer, true)
        assertEquals(allMusicalInstruments[0].currency, musicalInstrument.currency)
        assertEquals(allMusicalInstruments[0].category, musicalInstrument.category)
        assertEquals(allMusicalInstruments[0].description, musicalInstrument.description)
        assertEquals(allMusicalInstruments[0].price, musicalInstrument.price)
        assertEquals(allMusicalInstruments[0].quantityOnStock, musicalInstrument.quantityOnStock)
    }
}
