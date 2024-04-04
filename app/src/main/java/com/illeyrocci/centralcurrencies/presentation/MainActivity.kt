package com.illeyrocci.centralcurrencies.presentation

import android.os.Bundle
import android.view.WindowManager
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
import com.illeyrocci.centralcurrencies.data.remote.mapper.CurrencyMapper
import com.illeyrocci.centralcurrencies.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
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
            ViewModelProvider(
                this,
                CurrencyViewModelFactory(application)
            )[CurrencyViewModel::class.java]


        val mapper = CurrencyMapper()
        val currencyAdapter = CurrencyAdapter(this@MainActivity)

        binding.recycler.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recycler.adapter = currencyAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currenciesStream.collectLatest {
                    currencyAdapter.submitData(
                        mapper.mapDtoListToModelList(it.valute.values.toList())
                    )
                    binding.time.text = getString(R.string.update_time, Date().toString())
                }
            }
        }
    }
}