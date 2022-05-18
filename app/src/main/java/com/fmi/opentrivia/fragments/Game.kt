package com.fmi.opentrivia.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fmi.opentrivia.R
import com.fmi.opentrivia.adapters.AnswersAdapter
import com.fmi.opentrivia.models.Question
import com.fmi.opentrivia.services.external.OpenTriviaApi
import com.fmi.opentrivia.services.external.QuestionsApiResponse
import com.fmi.opentrivia.services.internal.AppDatabase
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.fmi.opentrivia.models.Game as GameModel


class Game : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var questionText: TextView
    private lateinit var imageView: ImageView
    private lateinit var currentQuestion: Question
    private var selectedAnswer: String? = null

    private var questions: MutableList<Question> = mutableListOf()

    private var questionIndex: Int = 0

    val args: GameArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_game, container, false)

        val category = args.categoryId
        this.getQuestions(
            category = category
        )

        var rightAnswers = 0
        var wrongAnswers = 0

        this.recyclerView = binding.findViewById(R.id.answers_list)
        this.questionText = binding.findViewById(R.id.questions_text)
        this.imageView = binding.findViewById(R.id.question_image)

        binding.findViewById<MaterialButton>(R.id.submit_answer)
            .setOnClickListener { view: View ->
                if (selectedAnswer != null) {
                    Log.d("msi", "$selectedAnswer == ${currentQuestion.correct_answer}")
                    if (selectedAnswer == currentQuestion.correct_answer) {
                        rightAnswers++
                        this.rotateQuestionsLogo()
                    } else {
                        wrongAnswers++
                        this.wiggleQuestionsLogo()
                    }

                    questionIndex++
                    if (questionIndex < questions.size) {
                        currentQuestion = questions[questionIndex]
                        showQuestion(currentQuestion)
                        selectedAnswer = null
                    } else {
                        val game = GameModel(
                            rightAnswers = rightAnswers,
                            wrongAnswers = wrongAnswers,
                            category = category,
                        )
                        AppDatabase.getInstance(requireContext()).gameDao().insert(game)
                        val action = GameDirections.actionGameToCategoryList()
                        view.findNavController().navigate(action)
                    }
                }
            }

        return binding
    }

    private fun wiggleQuestionsLogo()
    {
        val objectAnimator = ObjectAnimator.ofFloat(
            this.imageView,
            "rotation", 0f, 60f, 0f, -60f, 0f
        )
        objectAnimator.duration = 1000
        objectAnimator.start()
    }

    private fun rotateQuestionsLogo()
    {
        val objectAnimator = ObjectAnimator.ofFloat(
            this.imageView,
            "rotation", 0f, 360f,
        )
        objectAnimator.duration = 1000
        objectAnimator.start()
    }

    private fun getQuestions(count: Int = 10, category: Int? = null, type: String? = null) {
        val retrofit = Retrofit.Builder()
            .baseUrl(OpenTriviaApi.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OpenTriviaApi::class.java)
        Log.d("msi", "Searching for $count questions of type $type in $category")

        api.getQuestions(category, count, type).enqueue(object : Callback<QuestionsApiResponse> {
            override fun onResponse(
                call: Call<QuestionsApiResponse>,
                response: Response<QuestionsApiResponse>
            ) {
                if (response.isSuccessful()) {
                    val questionsList = response.body()!!.results
                    Log.d("msi", "Found ${questionsList.size} questions in response")
                    for (question in questionsList) {
                        val answers = question.incorrect_answers
                        answers.add(question.correct_answer)
                        questions.add(
                            Question(
                                question = question.question,
                                correct_answer = question.correct_answer,
                                answers = answers,
                            )
                        )
                    }

                    currentQuestion = questions[questionIndex]
                    showQuestion(currentQuestion)
                } else {
                    Log.d("msi", "Wrong code ${response.code()} from OpenTrivia API")
                }
            }

            override fun onFailure(call: Call<QuestionsApiResponse>, t: Throwable) {
                Log.d("msi", "Failure $t while trying to reach OpenTrivia API")
            }
        })
    }

    private fun onAnswerItemClick(position: Int) {
        Log.d("msi", "Pressed ${currentQuestion.answers[position]}")
        selectedAnswer = currentQuestion.answers[position]
    }

    private fun showQuestion(question: Question) {
        questionText.text = question.question

        this.recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = AnswersAdapter(question.answers) { position -> onAnswerItemClick(position) }
        }
    }
}