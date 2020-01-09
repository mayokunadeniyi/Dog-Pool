package com.example.aboutdogs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aboutdogs.model.DogBreed
import com.example.aboutdogs.network.DogApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */
class ListViewModel: ViewModel() {

    private val disposable = CompositeDisposable()
    private val dogService = DogApiService()


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
                        _dogs.value = dogList
                        _dogError.value = false
                        _loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        _dogError.value = true
                        _loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}