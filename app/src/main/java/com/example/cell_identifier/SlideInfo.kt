package com.example.cell_identifier

data class SlideInfo(
    var slideName: String? = "",
    var category: String? = "",
    var subCategory: String? = "",
    var slideComment: String? = "No Description",
    var userSubmitter: String? = "",
    var imageUri: String? = null
)

