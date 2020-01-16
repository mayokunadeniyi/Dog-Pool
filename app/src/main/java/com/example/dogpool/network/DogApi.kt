package com.example.dogpool.network

import com.example.dogpool.model.DogBreed
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Mayokun Adeniyi on 09/01/2020.
 */
interface DogApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs() : Single<List<DogBreed>>
}