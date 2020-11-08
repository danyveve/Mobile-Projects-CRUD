package com.example.lab1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.network.NetworkStateReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.IntentFilter
import android.view.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics


class MainActivity : AppCompatActivity(), MusicalInstrumentListAdapter.MyClickingEvents,
    NetworkStateReceiver.NetworkStateReceiverListener {
    private val newMusicalInstrumentActivityRequestCode = 1
    private val updateMusicalInstrumentActivityRequestCode = 2
    private lateinit var musicalInstrumentViewModel: MusicalInstrumentViewModel
    private lateinit var adapter: MusicalInstrumentListAdapter
    private var networkStateReceiver: NetworkStateReceiver? = null

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //instantiate network listener
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION)
        )

        super.onCreate(savedInstanceState)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)


        // Get a new or existing ViewModel from the ViewModelProvider.
        musicalInstrumentViewModel = ViewModelProvider(this).get(MusicalInstrumentViewModel::class.java)

        adapter = MusicalInstrumentListAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        musicalInstrumentViewModel.fetchData(applicationContext)

        observeModel()
        musicalInstrumentViewModel.allMusicalInstruments

        val fab = findViewById<ExtendedFloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewMusicalInstrumentActivity::class.java)
            startActivityForResult(intent, newMusicalInstrumentActivityRequestCode)
        }

        val fabRefresh = findViewById<ExtendedFloatingActionButton>(R.id.fabRefresh)
        fabRefresh.setOnClickListener {
            musicalInstrumentViewModel.fetchData(applicationContext)
        }

        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.fillAfter = true;
        fabRefresh.startAnimation(rotation);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if ((requestCode == newMusicalInstrumentActivityRequestCode || requestCode == updateMusicalInstrumentActivityRequestCode) &&
            resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val musicalInstrument = MusicalInstrument(0, data.getStringExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_NAME),
                    data.getStringExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_CATEGORY),
                    data.getStringExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_DESCRIPTION),
                    data.getIntExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_QUANTITY, 0),
                    data.getFloatExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_PRICE, 0F),
                    data.getStringExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_CURRENCY))
                if (requestCode == updateMusicalInstrumentActivityRequestCode){
                    musicalInstrument.id = data.getLongExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_ID, 0)
                    musicalInstrumentViewModel.update(musicalInstrument, applicationContext)
                } else {
                    musicalInstrumentViewModel.insert(musicalInstrument, applicationContext)
                }
                Unit
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun doDelete(musicalInstrument: MusicalInstrument) {
        val bundle = Bundle()
        bundle.putString("musical_instrument_name", musicalInstrument.name)
        mFirebaseAnalytics?.logEvent("deleting_instrument", bundle)

        if(MusicalInstrumentViewModel.isOnline){
            musicalInstrumentViewModel.delete(musicalInstrument, applicationContext)
            bundle.putString("musical_instrument_name", musicalInstrument.name)
            mFirebaseAnalytics?.logEvent("successfull_delete_of_instrument", bundle)
        } else {
            Toast.makeText(
                applicationContext,
                R.string.operation_not_available_untill_online,
                Toast.LENGTH_LONG
            ).show()
            bundle.putString("musical_instrument_name", musicalInstrument.name)
            mFirebaseAnalytics?.logEvent("error_on_delete_of_instrument", bundle)
        }
    }

    override fun doUpdate(musicalInstrument: MusicalInstrument) {
        val bundle = Bundle()
        bundle.putString("musical_instrument_name", musicalInstrument.name)
        mFirebaseAnalytics?.logEvent("updating_instrument", bundle)

        if(MusicalInstrumentViewModel.isOnline){

            val intent = Intent(this@MainActivity, NewMusicalInstrumentActivity::class.java)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_ID, musicalInstrument.id)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_NAME, musicalInstrument.name)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_DESCRIPTION, musicalInstrument.description)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_CATEGORY, musicalInstrument.category)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_QUANTITY, musicalInstrument.quantityOnStock)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_PRICE, musicalInstrument.price)
            intent.putExtra(NewMusicalInstrumentActivity.EXTRA_REPLY_CURRENCY, musicalInstrument.currency)
            startActivityForResult(intent, updateMusicalInstrumentActivityRequestCode)

            bundle.putString("musical_instrument_name", musicalInstrument.name)
            mFirebaseAnalytics?.logEvent("successfull_update_of_instrument", bundle)
        } else {
            Toast.makeText(
                applicationContext,
                R.string.operation_not_available_untill_online,
                Toast.LENGTH_LONG
            ).show()

            bundle.putString("musical_instrument_name", musicalInstrument.name)
            mFirebaseAnalytics?.logEvent("error_on_update_of_instrument", bundle)
        }
    }

    private fun observeModel(){
        // Add an observer on the LiveData returned by findAll method.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        musicalInstrumentViewModel.allMusicalInstruments.observe(this, Observer { musicalInstruments ->
            // Update the cached copy of the musical instruments in the adapter.
            musicalInstruments?.let { adapter.setMusicalInstruments(it) }
        })
    }

    override fun networkAvailable() {
        MusicalInstrumentViewModel.setIsOnline(true)
        musicalInstrumentViewModel.fetchData(applicationContext)
    }

    override fun networkUnavailable() {
        MusicalInstrumentViewModel.setIsOnline(false)
    }

    override fun onDestroy() {
        super.onDestroy();
        networkStateReceiver?.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }
}
