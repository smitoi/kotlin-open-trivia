package com.fmi.opentrivia.models

data class ApiQuestion(
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: MutableList<String>,
)

data class Question(
    val question: String,
    val correct_answer: String,
    val answers: List<String>,
)
