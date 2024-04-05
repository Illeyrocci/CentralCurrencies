package com.illeyrocci.centralcurrencies.data

import android.app.Application
import com.illeyrocci.centralcurrencies.data.local.database.CurrencyDatabase
import com.illeyrocci.centralcurrencies.data.local.mapper.CurrencyMapperLocal
import com.illeyrocci.centralcurrencies.data.remote.BASE_URL
import com.illeyrocci.centralcurrencies.data.remote.NetworkAPI
import com.illeyrocci.centralcurrencies.data.remote.mapper.CurrencyMapperRemote
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CurrencyRepositoryImpl(
    application: Application,
) : CurrencyRepository {

    private val networkAPI: NetworkAPI
    private val remoteSourcesMapper = CurrencyMapperRemote()
    private val localStoragesMapper = CurrencyMapperLocal()
    private val currencyDao = CurrencyDatabase.getInstance(application).currencyDao()

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

    override suspend fun getCurrenciesFromNetwork(): Resource<List<CurrencyItem>> {
        //just wrapping up a response<T> into Resource<T>
        return try {
            val response = networkAPI.getCurrencies()
            if (response.isSuccessful) {
                Resource.Success(
                    data = remoteSourcesMapper.mapValuteResponseToCurrencyItemList(
                        response.body()!!
                    )
                )
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

    override suspend fun getCurrenciesFromDb(): Resource<List<CurrencyItem>> {
        return try {
            Resource.Success(
                localStoragesMapper.mapCurrencyDbModelListToCurrencyItemList(
                    currencyDao.getCurrencies()
                )
            )
        } catch (e: Exception) {
            Resource.Error("Something went wrong with local storage")
        }
    }

    override suspend fun saveCurrenciesInDb(list: List<CurrencyItem>) {
        currencyDao.insertAll(localStoragesMapper.mapCurrencyItemListToCurrencyDbModelList(list))
    }
}
