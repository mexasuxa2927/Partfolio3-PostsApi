package com.example.watch_store.data.postcomment

data class Comments(
    val id: String,
    val message: String,
    val owner: Owner,
    val post: String,
    val publishDate: String
)