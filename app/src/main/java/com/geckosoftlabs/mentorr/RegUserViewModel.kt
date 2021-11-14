package com.geckosoftlabs.mentorr

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.geckosoftlabs.mentorr.model.UserRegResponse
import com.geckosoftlabs.mentorr.utils.Resource
import retrofit2.Response
import java.io.IOException

class RegUserViewModel(app: Application, private val newsRepository: RegUserRepository): AndroidViewModel(app) {
    val breakingNews: MutableLiveData<Resource<UserRegResponse>> = MutableLiveData()
    var breakingNewsResponse: List<UserRegResponse>? = null

    fun getUser(){

    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getUser()
                //breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("Нет подключения к сети"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Сбой сети"))
                else -> breakingNews.postValue(Resource.Error("Ошибка конвертации"))
            }
        }
    }


    private fun handleBreakingNewsResponse(response: Response<List<UserRegResponse>>): Resource<List<UserRegResponse>>? {
        if (response.isSuccessful) {

            response.body()?.let { result ->
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = result
                }
                return Resource.Success(breakingNewsResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MentorrApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
    }
}