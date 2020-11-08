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

import androidx.lifecycle.LiveData

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
open class MusicalInstrumentRepository(var musicalInstrumentDao: MusicalInstrumentDao) {

  // Room executes all queries on a separate thread.
  // Observed LiveData will notify the observer when the data has changed.
  val allMusicalInstruments: LiveData<List<MusicalInstrument>> = musicalInstrumentDao.findAll()

  suspend fun insert(musicalInstrument: MusicalInstrument): Long {
    return musicalInstrumentDao.insert(musicalInstrument)
  }

  suspend fun delete(musicalInstrument: MusicalInstrument){
    musicalInstrumentDao.delete(musicalInstrument)
  }

  suspend fun update(musicalInstrument: MusicalInstrument){
    musicalInstrumentDao.update(musicalInstrument)
  }

  suspend fun deleteAll(){
    musicalInstrumentDao.deleteAll()
  }
}
