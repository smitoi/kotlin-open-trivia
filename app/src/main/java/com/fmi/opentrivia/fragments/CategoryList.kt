package com.fmi.opentrivia.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.R
import com.fmi.opentrivia.adapters.CategoriesAdapter
import com.fmi.opentrivia.models.Category
import com.fmi.opentrivia.services.external.CategoriesApiResponse
import com.fmi.opentrivia.services.external.OpenTriviaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryList : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_category_list, container, false)

        this.recyclerView = binding.findViewById(R.id.categories_list)
        this.getAllCategories()

        return binding
    }

    private fun getAllCategories() {
        val retrofit = Retrofit.Builder()
            .baseUrl(OpenTriviaApi.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OpenTriviaApi::class.java)

        api.fetchAllCategories().enqueue(object : Callback<CategoriesApiResponse> {
            override fun onResponse(
                call: Call<CategoriesApiResponse>,
                response: Response<CategoriesApiResponse>
            ) {
                if (response.isSuccessful()) {
                    val categoriesList = response.body()!!.trivia_categories
                    Log.d("msi", "Found ${categoriesList.size} categories in response")
                    showCategories(categoriesList)
                } else {
                    Log.d("msi", "Wrong code ${response.code()} from OpenTrivia API")
                }
            }

            override fun onFailure(call: Call<CategoriesApiResponse>, t: Throwable) {
                Log.d("msi", "Failure $t while trying to reach OpenTrivia API")
            }
        })
    }

    private fun showCategories(categories: List<Category>) {
        this.recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = CategoriesAdapter(categories)
        }
    }
}