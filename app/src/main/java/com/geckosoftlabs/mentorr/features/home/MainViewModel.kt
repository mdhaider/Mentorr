package com.geckosoftlabs.mentorr.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geckosoftlabs.mentorr.features.home.MainRepository
import kotlinx.coroutines.launch


class MainViewModel (
    private val mainRepository: MainRepository
) : ViewModel() {
    fun setMemo(memo: String) {
        viewModelScope.launch {
            mainRepository.setMemo(memo)
        }
    }
}
