package com.example.aboutdogs.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aboutdogs.database.DogDao
import com.example.aboutdogs.database.DogDatabase
import com.example.aboutdogs.model.DogBreed
import com.example.aboutdogs.network.DogApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */
class ListViewModel(application: Application): BaseViewModel(application) {

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

    fun refresh(){
       fetchFromRemote()
    }

    private fun fetchFromRemote() {
        _loading.value = true
        disposable.add(
            dogService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogList: List<DogBreed>) {
                      storeDogsLocally(dogList)
                    }

                    override fun onError(e: Throwable) {
                        _dogError.value = true
                        _loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>){
        _dogs.value = dogList
        _dogError.value = false
        _loading.value = false
    }
    private fun storeDogsLocally(dogList: List<DogBreed>) {
        launch {
            dao.deleteAllDogs()
            val result = dao.insertAll(*dogList.toTypedArray())
            var i = 0
            while (i < dogList.size){
                dogList[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(dogList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}