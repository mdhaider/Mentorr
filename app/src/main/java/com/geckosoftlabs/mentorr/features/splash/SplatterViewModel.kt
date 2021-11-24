package com.geckosoftlabs.mentorr.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geckosoftlabs.mentorr.utils.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplatterViewModel(val dataStoreManager: DataStoreManager, val repository: SplatterRepository): ViewModel() {

    private fun setValue(loggedIn: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                dataStoreManager.saveLoggedInState(loggedIn)
            }
        }
    }
}