package com.geckosoftlabs.mentorr.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
