package com.geckosoftlabs.mentorr.api

import com.geckosoftlabs.mentorr.utils.NwConstants.Companion.BACKENDLESS_BASE_URL
import com.geckosoftlabs.mentorr.utils.NwConstants.Companion.BACKENDLESS_REST_API_KEY
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
        companion object {
            private val retrofit by lazy {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()
                Retrofit.Builder()
                    .baseUrl("$BACKENDLESS_BASE_URL/$BACKENDLESS_REST_API_KEY")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            }
            val api: NewsApi by lazy {
                retrofit.create(NewsApi::class.java)
            }
        }
}