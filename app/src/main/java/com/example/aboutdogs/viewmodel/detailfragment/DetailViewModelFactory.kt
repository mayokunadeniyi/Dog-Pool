package com.example.aboutdogs.viewmodel.detailfragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 * Created by Mayokun Adeniyi on 13/01/2020.
 */
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val dogUid: Int,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(dogUid,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}