package com.example.dogpool.viewmodel.detailfragment

import android.app.Application
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dogpool.data.DogDatabase
import com.example.dogpool.model.DogBreed
import com.example.dogpool.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 09/01/2020.
 */
class DetailViewModel(
    private val dogUid: Int,
    application: Application
) : BaseViewModel(application) {

    private val dao = DogDatabase.getInstance(getApplication()).dogDao

    private val _dogBreed = MutableLiveData<DogBreed>()
    val dogBreed: LiveData<DogBreed>
        get() = _dogBreed

    private val _smsStarted = MutableLiveData<Boolean>()
    val smsStarted: LiveData<Boolean>
        get() = _smsStarted


    init {
        setDogBreed()
        _smsStarted.value = false
    }

    private fun setDogBreed() {
        launch {
            withContext(Dispatchers.IO) {
                Timber.i("The main thread ${Looper.myLooper() == Looper.getMainLooper()}")
                _dogBreed.postValue(dao.getDog(dogUid))
            }
        }
    }

    fun startSms(){
        _smsStarted.value = true
    }

    fun doneSendingSms(){
        _smsStarted.value = false
    }
}