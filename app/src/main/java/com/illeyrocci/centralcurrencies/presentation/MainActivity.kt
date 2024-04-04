package com.illeyrocci.centralcurrencies.presentation

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.illeyrocci.centralcurrencies.R
import com.illeyrocci.centralcurrencies.databinding.ActivityMainBinding
import com.illeyrocci.centralcurrencies.domain.model.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //paints status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getResources().getColor(R.color.primary, theme)

        val viewModel =
            ViewModelProvider(this, CurrencyViewModelFactory())[CurrencyViewModel::class.java]

        val currencyAdapter = CurrencyAdapter(this@MainActivity)

        binding.recycler.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recycler.adapter = currencyAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currenciesStateStream.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            currencyAdapter.submitData(it.data ?: emptyList())
                            val lastUpdate = Date().toString()

                            viewModel.setLastUpdatedTime(lastUpdate)
                            binding.time.text = getString(R.string.update_time, Date().toString())

                        }
                        is Resource.Loading -> {
                            Toast.makeText(this@MainActivity, "LOADING", Toast.LENGTH_LONG).show()
                        }
                        is Resource.Error -> {
                            Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}