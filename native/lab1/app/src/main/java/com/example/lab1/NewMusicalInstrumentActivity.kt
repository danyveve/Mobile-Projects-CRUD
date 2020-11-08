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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Activity for entering a musical instrument.
 */

class NewMusicalInstrumentActivity : AppCompatActivity() {

  private lateinit var editMusicalInstrumentNameView: EditText
  private lateinit var editMusicalInstrumentCategoryView: EditText
  private lateinit var editMusicalInstrumentDescriptionView: EditText
  private lateinit var editMusicalInstrumentQuantityView: EditText
  private lateinit var editMusicalInstrumentPriceView: EditText
  private lateinit var editMusicalInstrumentCurrencyView: EditText
  private lateinit var saveButtonView : Button
  private var id: Long = 0


  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)



    setContentView(R.layout.activity_new_musical_instrument)
    editMusicalInstrumentNameView = findViewById(R.id.new_name)
    editMusicalInstrumentCategoryView = findViewById(R.id.new_category)
    editMusicalInstrumentDescriptionView = findViewById(R.id.new_description)
    editMusicalInstrumentQuantityView = findViewById(R.id.new_quantity)
    editMusicalInstrumentPriceView = findViewById(R.id.new_price)
    editMusicalInstrumentCurrencyView = findViewById(R.id.new_currency)
    saveButtonView = findViewById(R.id.button_save)

    val bundle: Bundle? = intent.extras
    if(bundle != null){
      id = bundle.getLong(EXTRA_REPLY_ID, 0)
      if (id != 0L){
        editMusicalInstrumentNameView.setText(bundle.getString(EXTRA_REPLY_NAME))
        editMusicalInstrumentDescriptionView.setText(bundle.getString(EXTRA_REPLY_DESCRIPTION))
        editMusicalInstrumentCategoryView.setText(bundle.getString(EXTRA_REPLY_CATEGORY))
        editMusicalInstrumentQuantityView.setText(bundle.getInt(EXTRA_REPLY_QUANTITY).toString())
        editMusicalInstrumentPriceView.setText(bundle.getFloat(EXTRA_REPLY_PRICE).toString())
        editMusicalInstrumentCurrencyView.setText(bundle.getString(EXTRA_REPLY_CURRENCY))
        saveButtonView.setText(R.string.button_update)
      }
    }


    val button = findViewById<Button>(R.id.button_save)
    button.setOnClickListener {
      val replyIntent = Intent()
      if (TextUtils.isEmpty(editMusicalInstrumentNameView.text) ||
        TextUtils.isEmpty(editMusicalInstrumentDescriptionView.text) ||
        TextUtils.isEmpty(editMusicalInstrumentCategoryView.text) ||
        TextUtils.isEmpty(editMusicalInstrumentQuantityView.text) ||
        TextUtils.isEmpty(editMusicalInstrumentPriceView.text) ||
        TextUtils.isEmpty(editMusicalInstrumentCurrencyView.text)) {
        setResult(Activity.RESULT_CANCELED, replyIntent)
      } else {
        val name = editMusicalInstrumentNameView.text.toString()
        val category = editMusicalInstrumentCategoryView.text.toString()
        val description = editMusicalInstrumentDescriptionView.text.toString()
        val quantity = editMusicalInstrumentQuantityView.text.toString().toInt()
        val price = editMusicalInstrumentPriceView.text.toString().toFloat()
        val currency = editMusicalInstrumentCurrencyView.text.toString()

        if(id != 0L){
          replyIntent.putExtra(EXTRA_REPLY_ID, id)
        }
        replyIntent.putExtra(EXTRA_REPLY_NAME, name)
        replyIntent.putExtra(EXTRA_REPLY_CATEGORY, category)
        replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, description)
        replyIntent.putExtra(EXTRA_REPLY_QUANTITY, quantity)
        replyIntent.putExtra(EXTRA_REPLY_PRICE, price)
        replyIntent.putExtra(EXTRA_REPLY_CURRENCY, currency)
        setResult(Activity.RESULT_OK, replyIntent)
      }
      finish()
    }
  }

  companion object {
    const val EXTRA_REPLY_ID = "lab1.android.musicalInstruments.REPLY_ID"
    const val EXTRA_REPLY_NAME = "lab1.android.musicalInstruments.REPLY_NAME"
    const val EXTRA_REPLY_CATEGORY = "lab1.android.musicalInstruments.REPLY_CATEGORY"
    const val EXTRA_REPLY_DESCRIPTION = "lab1.android.musicalInstruments.REPLY_DESCRIPTION"
    const val EXTRA_REPLY_QUANTITY = "lab1.android.musicalInstruments.REPLY_QUANTITY"
    const val EXTRA_REPLY_PRICE = "lab1.android.musicalInstruments.REPLY_PRICE"
    const val EXTRA_REPLY_CURRENCY = "lab1.android.musicalInstruments.REPLY_CURRENCY"

  }
}

