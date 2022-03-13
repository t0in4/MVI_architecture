package com.eyehail.mvi_architecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eyehail.mvi_architecture.api.AnimalService
import com.eyehail.mvi_architecture.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var adapter = AnimalListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupObservables()

    }

    private fun setupUI() {
        mainViewModel = ViewModelProviders
            .of(this, ViewModelFactory(AnimalService.api))
            .get(MainViewModel::class.java)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        recyclerView.adapter = adapter
        findViewById<Button>(R.id.buttonFetchAnimals).setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }
    }


    private fun setupObservables() {
        lifecycleScope.launch {
            mainViewModel.state.collect { collector ->
                when(collector) {
                    is MainState.Idle -> {

                    }
                    is MainState.Loading -> {
                        buttonFetchAnimals.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                    is MainState.Animals -> {
                        progressBar.visibility = View.GONE
                        buttonFetchAnimals.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        collector.animals.let {
                            adapter.newAnimals(it)
                        }
                    }
                    is MainState.Error -> {
                        progressBar.visibility = View.GONE
                        buttonFetchAnimals.visibility = View.GONE
                        Toast.makeText(this@MainActivity, collector.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}