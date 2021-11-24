package com.geckosoftlabs.mentorr.features.splash

import com.geckosoftlabs.mentorr.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SplatterRepository(private val dataStoreManager: DataStoreManager) {
     /*suspend fun getSavedState(): Flow<Boolean> {
         return {
             val state= dataStoreManager.getLoggedInState()
            // emit(state)
        }
    }
*/
    suspend fun saveLoggedInState(isLoggedIn: Boolean){
        dataStoreManager.saveLoggedInState(isLoggedIn)
    }
}