package com.fmi.opentrivia.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.R
import com.fmi.opentrivia.adapters.CategoriesAdapter
import com.fmi.opentrivia.models.Category
import com.fmi.opentrivia.services.external.CategoriesApiResponse
import com.fmi.opentrivia.services.external.OpenTriviaApi
import com.fmi.opentrivia.services.internal.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Profile : Fragment() {

    private lateinit var takeProfilePictureButton: Button
    private lateinit var profilePicture: ImageView
    private lateinit var wrongAnswersCount: TextView
    private lateinit var rightAnswersCount: TextView
    private lateinit var deleteDataButton: Button
    private lateinit var shareDataButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_profile, container, false)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val takenImage = result.data?.extras?.get("data") as Bitmap
                    this.profilePicture.setImageBitmap(takenImage)
                }
            }

        this.takeProfilePictureButton = binding.findViewById(R.id.profile_picture_button)
        this.takeProfilePictureButton.setOnClickListener {
            Log.d("msi", "Profile picture button clicked")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(takePictureIntent)
        }

        this.profilePicture = binding.findViewById(R.id.profile_picture)
        Log.d("msi", "Initialised take profile picture button")

        val games = AppDatabase.getInstance(requireContext()).gameDao().getAll()
        var rightAnswers = 0
        var wrongAnswers = 0

        for (game in games) {
            rightAnswers += game.rightAnswers
            wrongAnswers += game.wrongAnswers
        }

        this.rightAnswersCount = binding.findViewById(R.id.right_answers_count)
        this.rightAnswersCount.text = rightAnswers.toString()
        this.wrongAnswersCount = binding.findViewById(R.id.wrong_answers_count)
        this.wrongAnswersCount.text = wrongAnswers.toString()

        this.deleteDataButton = binding.findViewById(R.id.clear_data_button)
        this.deleteDataButton.setOnClickListener {
            this.deleteData()
        }

        this.shareDataButton = binding.findViewById(R.id.share_data_button)
        this.shareDataButton.setOnClickListener {
            this.shareData()
        }

        return binding
    }

    private fun deleteData() {
        AppDatabase.getInstance(requireContext()).gameDao().nukeTable()
        this.rightAnswersCount.text = 0.toString()
        this.wrongAnswersCount.text = 0.toString()
    }

    private fun shareData() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "I am playing OpenTrivia, you should try it too!")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(
            sendIntent,
            "Spread the word about OpenTrivia with the world!"
        )
        startActivity(shareIntent)
    }
}