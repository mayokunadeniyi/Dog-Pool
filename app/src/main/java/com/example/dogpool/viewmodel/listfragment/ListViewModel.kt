package com.example.dogpool.viewmodel.listfragment

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dogpool.data.DogDatabase
import com.example.dogpool.model.DogBreed
import com.example.dogpool.network.DogApiService
import com.example.dogpool.utils.NotificationsHelper
import com.example.dogpool.utils.SharedPreferenceHelper
import com.example.dogpool.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NumberFormatException

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */
class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferenceHelper.getInstance(application)
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val disposable = CompositeDisposable()
    private val dogService = DogApiService()
    private val dao = DogDatabase.getInstance(getApplication()).dogDao

    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>>
        get() = _dogs

    private val _dogError = MutableLiveData<Boolean>()
    val dogError: LiveData<Boolean>
        get() = _dogError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cacheDurationInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cacheDurationInt.times(1000 * 1000 * 1000L)
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }

    fun refreshBypassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        _loading.value = true
        launch {
            withContext(Dispatchers.IO){
                val dogs = dao.getAllDogs()
                dogsRetrieved(dogs)
            }
            Toast.makeText(getApplication(), "Data retrieved from database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFromRemote() {
        _loading.value = true
        disposable.add(
            dogService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                        Toast.makeText(
                            getApplication(),
                            "Data retrieved from remote",
                            Toast.LENGTH_SHORT
                        ).show()
                        NotificationsHelper(getApplication()).createNotification()

                    }

                    override fun onError(e: Throwable) {
                        _dogError.value = true
                        _loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>) {
        _dogs.postValue(dogList)
        _dogError.postValue(false)
        _loading.postValue(false)
    }

    private fun storeDogsLocally(dogList: List<DogBreed>) {
        launch {
            withContext(Dispatchers.IO){
                dao.deleteAllDogs()
                val result = dao.insertAll(*dogList.toTypedArray())
                var i = 0
                while (i < dogList.size) {
                    dogList[i].uuid = result[i].toInt()
                    ++i
                }
                dogsRetrieved(dogList)
            }
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}