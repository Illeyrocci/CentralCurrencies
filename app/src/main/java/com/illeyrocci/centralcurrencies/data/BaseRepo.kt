package com.illeyrocci.centralcurrencies.data

import com.illeyrocci.centralcurrencies.presentation.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
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
}