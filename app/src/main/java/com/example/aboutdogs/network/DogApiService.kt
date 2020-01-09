package com.example.aboutdogs.network

import com.example.aboutdogs.model.DogBreed
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mayokun Adeniyi on 09/01/2020.
 */
class DogApiService {

    private val BASE_URL = "https://raw.githubusercontent.com"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DogApi::class.java)

    fun getDogs(): Single<List<DogBreed>>{
        return api.getDogs()
    }
}