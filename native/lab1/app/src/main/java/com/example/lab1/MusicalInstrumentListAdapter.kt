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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext


class MusicalInstrumentListAdapter internal constructor(
  context: Context, private var myClickingEvents: MyClickingEvents
) : RecyclerView.Adapter<MusicalInstrumentListAdapter.MusicalInstrumentViewHolder>() {

  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var musicalInstruments = emptyList<MusicalInstrument>() // Cached copy of words

  inner class MusicalInstrumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val musicalInstrumentItemViewName: TextView = itemView.findViewById(R.id.name)
    val musicalInstrumentItemViewDescription: TextView = itemView.findViewById(R.id.description)
    val musicalInstrumentItemViewPriceAndCurrency: TextView = itemView.findViewById(R.id.priceAndCurrency)
    val musicalInstrumentUpdate: ImageView = itemView.findViewById(R.id.ivUpdate)
    val musicalInstrumentDelete: ImageView = itemView.findViewById(R.id.ivDelete)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicalInstrumentViewHolder {
    val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
    return MusicalInstrumentViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: MusicalInstrumentViewHolder, position: Int) {
    val current = musicalInstruments[position]
    holder.musicalInstrumentItemViewName.text = current.name
    holder.musicalInstrumentItemViewDescription.text = current.description
    holder.musicalInstrumentItemViewPriceAndCurrency.text = current.price.toString().plus(" ".plus(current.currency))

    holder.musicalInstrumentDelete.setOnClickListener {
      myClickingEvents.doDelete(current)
    }

    holder.musicalInstrumentUpdate.setOnClickListener {
      myClickingEvents.doUpdate(current)
    }
  }

  internal fun setMusicalInstruments(musicalInstruments: List<MusicalInstrument>) {
    this.musicalInstruments = musicalInstruments
    notifyDataSetChanged()
  }

  override fun getItemCount() = musicalInstruments.size

  interface MyClickingEvents{
    fun doDelete(musicalInstrument: MusicalInstrument)
    fun doUpdate(musicalInstrument: MusicalInstrument)
  }
}




