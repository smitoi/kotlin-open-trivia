package com.fmi.opentrivia.services.external

import com.fmi.opentrivia.models.ApiQuestion

data class QuestionsApiResponse(
    val results: List<ApiQuestion>
)
