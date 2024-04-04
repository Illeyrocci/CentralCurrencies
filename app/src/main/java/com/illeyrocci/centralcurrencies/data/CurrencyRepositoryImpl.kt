package com.illeyrocci.centralcurrencies.data

import android.app.Application
import com.illeyrocci.centralcurrencies.data.remote.BASE_URL
import com.illeyrocci.centralcurrencies.data.remote.NetworkAPI
import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse
import com.illeyrocci.centralcurrencies.data.remote.mapper.CurrencyMapper
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyRepositoryImpl @OptIn(DelicateCoroutinesApi::class) constructor(
    application: Application, //will needed later
    //a scope that outlives viewModelScope so data will update when user is out of the app
    //private val coroutineScope: CoroutineScope = GlobalScope
) : BaseRepo(), CurrencyRepository {

    private val networkAPI: NetworkAPI
    private val mapper = CurrencyMapper()
    //TODO("make it singleton and implement")

    //private val mapper = CurrencyMapper()
    //private val currencyDao=Database.getInstance(application).currencyDao

    //override fun s

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        networkAPI = retrofit.create(NetworkAPI::class.java)
    }

    override suspend fun getCurrencies(): ValuteResponse = networkAPI.getCurrencies()
        //safeApiCall { networkAPI.getCurrencies() }



}