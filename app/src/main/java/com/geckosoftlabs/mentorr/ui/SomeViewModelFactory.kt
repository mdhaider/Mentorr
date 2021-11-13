package com.geckosoftlabs.mentorr.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SomeViewModelFactory(private val someString: MainRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(someString) as T
} 