package com.illeyrocci.centralcurrencies.data

import android.app.Application
import com.illeyrocci.centralcurrencies.data.remote.BASE_URL
import com.illeyrocci.centralcurrencies.data.remote.NetworkAPI
import com.illeyrocci.centralcurrencies.data.remote.mapper.CurrencyMapper
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.model.Resource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CurrencyRepositoryImpl @OptIn(DelicateCoroutinesApi::class) constructor(
    application: Application, //will needed later
    //a scope that outlives viewModelScope so data will update when user is out of the app
    //private val coroutineScope: CoroutineScope = GlobalScope
) : CurrencyRepository {

    private val networkAPI: NetworkAPI
    private val mapper = CurrencyMapper()
    //private val currencyDao = CurrencyDatabase.getInstance(application).currencyDao()

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

    override suspend fun getCurrencies(): Resource<List<CurrencyItem>> {
        //just wrapping a response<T> up into Resource<T>
        return withContext(Dispatchers.IO) {
            try {
                val response = networkAPI.getCurrencies()
                if (response.isSuccessful) {
                    Resource.Success(data = mapper.mapValuteResponseToCurrencyItemList(response.body()!!))
                } else {
                    Resource.Error("Something went wrong with network")
                }
            } catch (e: HttpException) {
                Resource.Error(e.message() ?: "Something went wrong (Http)")
            } catch (e: IOException) {
                Resource.Error("Check your network connection")
            } catch (e: Exception) {
                Resource.Error("Something went wrong")
            }
        }
    }

    companion object {
        private var INSTANCE: CurrencyRepositoryImpl? = null
        private val LOCK = Any()

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(LOCK) {
                INSTANCE
                    ?: CurrencyRepositoryImpl(application)
            }
    }
}
