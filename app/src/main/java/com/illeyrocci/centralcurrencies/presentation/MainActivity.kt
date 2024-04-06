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
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
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
            retryImageButton.setOnClickListener { viewModel.refreshUiModel() }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currenciesStateStream.collectLatest { state ->
                    binding.toggleVisibility(state)
                    if (state is Resource.Success && !state.data.isNullOrEmpty()) {
                        currencyAdapter.submitData(state.data)
                        val lastUpdate = getCurrentTime()
                        viewModel.saveLastUpdateTime(lastUpdate)
                        binding.time.text = getString(R.string.update_time, lastUpdate)
                    }
                    if (state is Resource.Error) {
                        if (state.message != null) binding.errorVerbose.text =
                            resources.getString(R.string.error_verbose_message, state.message)
                        if (!state.data.isNullOrEmpty()) {
                            currencyAdapter.submitData(state.data)
                        }
                    }
                }
            }
        }
    }

    /*cases:
     *Resource.Loading - just progressbar
     *Resource.Success, data == null - display full error
     *Resource.Success, data is empty - display full error
     *Resource.Success, data is OK - display list
     *Resource.Error, data is empty - display full error
     *Resource.Error, data is null - display full error
     *Resource.Error, data is OK - display outdated and list
    */
    private fun ActivityMainBinding.toggleVisibility(state: Resource<List<CurrencyItem>>) {
        val stateLoading = state is Resource.Loading
        val stateSuccess = state is Resource.Success
        val stateError = state is Resource.Error

        val isStorageEmpty = state.data.isNullOrEmpty()

        progressBar.isVisible = state is Resource.Loading

        errorVerbose.isVisible = isStorageEmpty && !stateLoading
        emptyList.isVisible = isStorageEmpty && !stateLoading
        retryButton.isVisible = isStorageEmpty && !stateLoading

        recycler.isVisible = (stateSuccess || stateError) && !isStorageEmpty
        tableHeader.isVisible = (stateSuccess || stateError) && !isStorageEmpty

        retryImageButton.isVisible = stateError && !isStorageEmpty
        outdated.isVisible = stateError && !isStorageEmpty
    }

    private fun getCurrentTime() =
        DateFormat.getDateTimeInstance().format(Date().time)
}


