package com.example.cell_identifier

data class SlideInfo(
    var slideName: String? = "",
    var category: String? = "",
    var subCategory: String? = "",
    var slideComment: String? = "No Description",
    var userSubmitter: String? = "",
    var imageUri: String? = null,
    val annotations: List<AnnotationData> = emptyList()
){
    data class AnnotationData(
        val name: String,
        val facts: String,
        val xCoordinate: Float,
        val yCoordinate: Float
    )
}

