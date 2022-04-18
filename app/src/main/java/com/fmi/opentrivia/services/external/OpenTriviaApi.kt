package com.fmi.opentrivia.services.external

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTriviaApi {

    @GET("api_category.php")
    fun fetchAllCategories(): Call<CategoriesApiResponse>

    @GET("api.php")
    fun getQuestions(
        @Query("category") category: Int? = null,
        @Query("amount") amount: Int? = null,
        @Query("type") type: String? = null,
    ): Call<QuestionsApiResponse>

    companion object {
        const val URL = "https://opentdb.com/"
    }

}