package com.fmi.opentrivia.services.external

import com.fmi.opentrivia.models.Category

data class CategoriesApiResponse(
    val trivia_categories: List<Category>
)
