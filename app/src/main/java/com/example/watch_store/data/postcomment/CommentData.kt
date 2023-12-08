package com.example.watch_store.data.postcomment

data class CommentData(
    val `data`: List<Comments>,
    val limit: Int,
    val page: Int,
    val total: Int
)