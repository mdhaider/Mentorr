package com.geckosoftlabs.mentorr

import com.geckosoftlabs.mentorr.api.RetrofitInstance

class RegUserRepository {
    suspend fun getUser() = RetrofitInstance.api.getRegUser()

}