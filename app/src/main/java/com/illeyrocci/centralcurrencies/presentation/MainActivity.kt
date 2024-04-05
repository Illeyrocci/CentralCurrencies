package com.illeyrocci.centralcurrencies.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
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
import java.text.DateFormat
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

        val currencyAdapter = CurrencyAdapter(this)

        with(binding) {
            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            recycler.adapter = currencyAdapter
            time.text =
                resources.getString(R.string.update_time, resources.getString(R.string.unknown))
            retryButton.setOnClickListener { viewModel.refreshUiModel() }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currenciesStateStream.collectLatest { state ->
                    binding.toggleVisibility(state)
                    if (state is Resource.Success && state.data != null) {
                        currencyAdapter.submitData(state.data)
                        val lastUpdate = getCurrentTime()
                        viewModel.setLastUpdatedTime(lastUpdate)
                        binding.time.text = getString(R.string.update_time, lastUpdate)
                    }
                    if (state is Resource.Error && state.message != null)
                        binding.errorVerbose.text =
                            resources.getString(R.string.error_verbose_message, state.message)
                }
            }
        }
    }

    private fun <T> ActivityMainBinding.toggleVisibility(state: Resource<T>) {
        val stateLoading = state is Resource.Loading
        val stateError = state is Resource.Error
        val stateSuccess = state is Resource.Success

        val isSuccessDataNull = stateSuccess && state.data == null

        progressBar.isVisible = stateLoading
        errorVerbose.isVisible = stateError

        emptyList.isVisible = stateError || isSuccessDataNull
        retryButton.isVisible = stateError || isSuccessDataNull
        recycler.isVisible = stateSuccess && !isSuccessDataNull
        tableHeader.isVisible = stateSuccess && !isSuccessDataNull
    }

    private fun getCurrentTime() =
        DateFormat.getDateTimeInstance().format(Date().time)
}


