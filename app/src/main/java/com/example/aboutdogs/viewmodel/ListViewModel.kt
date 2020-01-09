package com.example.aboutdogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aboutdogs.model.DogBreed

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */
class ListViewModel: ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        val dog1 = DogBreed("1","Corgi","5 years","xx","xx","xx","xx")
        val dog2 = DogBreed("2","Labrador","6 years","xx","xx","xx","xx")
        val dog3 = DogBreed("3","Rotwiler","7 years","xx","xx","xx","xx")

        val dogList = arrayListOf(dog1,dog2,dog3)
        dogs.value = dogList
        dogError.value = false
        loading.value = false
    }
}