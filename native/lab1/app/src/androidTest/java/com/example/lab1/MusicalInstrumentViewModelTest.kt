/*
 * Copyright (C) 2017 The Android Open Source Project
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
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*


/**
 * Unit test for [UserViewModel]
 */
class MusicalInstrumentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var dataSource: MusicalInstrumentDao

    @Mock
    lateinit var viewStateObserver: Observer<MyViewState>

    @Captor
    private lateinit var userArgumentCaptor: ArgumentCaptor<MusicalInstrument>

    private lateinit var viewModel: MusicalInstrumentViewModel

    private lateinit var application: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        application = ApplicationProvider.getApplicationContext()

        viewModel = MusicalInstrumentViewModel(application).apply {
            getStateLiveData().observeForever(viewStateObserver)
        }

        viewModel.repository.musicalInstrumentDao = dataSource
        viewModel.showSnackBar = false;
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testInsert() = testCoroutineRule.runBlockingTest {
        // Given that the UserDataSource returns a user
        val musicalInstrument = MusicalInstrument(
            -1, "GUITAR 1", "STRING", "a very very old guitar", 10,
            150.35F,
            "EUR"
        )
        `when`(dataSource.insert(musicalInstrument)).thenReturn(-1)

        viewModel.insert(musicalInstrument, application)

        verify(viewStateObserver).onChanged(MyViewState.Loading)
        verify(viewStateObserver).onChanged(MyViewState.Success(musicalInstrument))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testInsertError() = testCoroutineRule.runBlockingTest {
        // Given that the UserDataSource returns a user
        val musicalInstrument = MusicalInstrument(
            -1, "GUITAR 1", "STRING", "a very very old guitar", 10,
            150.35F,
            "EUR"
        )
        val error = Error()
        `when`(dataSource.insert(musicalInstrument)).thenThrow(error)

        viewModel.insert(musicalInstrument, application)

        verify(viewStateObserver).onChanged(MyViewState.Loading)
        verify(viewStateObserver).onChanged(MyViewState.Error(error))
    }
}
