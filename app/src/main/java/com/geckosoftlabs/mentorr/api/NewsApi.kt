package com.geckosoftlabs.mentorr.api

import com.geckosoftlabs.mentorr.model.UserRegResponse
import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {
    @GET("/data/RegisteredUser")
    suspend fun getRegUser(
    ): Response<List<UserRegResponse>>
}