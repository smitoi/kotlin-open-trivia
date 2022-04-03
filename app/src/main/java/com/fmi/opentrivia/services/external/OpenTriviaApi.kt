package com.fmi.opentrivia.services.external

import retrofit2.Call
import retrofit2.http.GET

interface OpenTriviaApi {

    @GET("api_category.php")
    fun fetchAllCategories(): Call<CategoriesApiResponse>

}