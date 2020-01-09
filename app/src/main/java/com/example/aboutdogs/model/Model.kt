package com.example.aboutdogs.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 08/01/2020.
 */

data class DogBreed(
    @SerializedName("id")
    val breedId: String?,

    @SerializedName("name")
    val dogBreed: String?,

    @SerializedName("life_span")
    val lifeSpan: String?,

    @SerializedName("breed_group")
    val breedGroup: String?,

    @SerializedName("bred_for")
    val bredFor: String?,

    @SerializedName("temperament")
    val temperament: String?,

    @SerializedName("url")
    val imageUrl: String?
)