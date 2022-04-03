package com.fmi.opentrivia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.adapters.CategoriesAdapter
import com.fmi.opentrivia.services.external.CategoriesApiResponse
import com.fmi.opentrivia.models.Category
import com.fmi.opentrivia.services.external.OpenTriviaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var takeProfilePictureButton: Button
    private lateinit var profilePicture: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.recyclerView = findViewById(R.id.categories_list)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
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
                    d("msi", "Found ${categoriesList.size} categories in response")
                    showCategories(response.body()!!.trivia_categories)
                } else {
                    d("msi", "Wrong code ${response.code()} from OpenTrivia API")
                }
            }

            override fun onFailure(call: Call<CategoriesApiResponse>, t: Throwable) {
                d("msi", "Failure $t while trying to reach OpenTrivia API")
            }
        })

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val takenImage = result.data?.extras?.get("data") as Bitmap
                    this.profilePicture.setImageBitmap(takenImage)
                }
            }

        this.takeProfilePictureButton = findViewById(R.id.profile_picture_button)
        this.takeProfilePictureButton.setOnClickListener {
            d("msi", "Profile picture button clicked")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(takePictureIntent)
        }

        this.profilePicture = findViewById(R.id.profile_picture)

        d("msi", "Initialised take profile picture button")
    }

    private fun showCategories(categories: List<Category>) {
        this.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CategoriesAdapter(categories)
        }
    }
}