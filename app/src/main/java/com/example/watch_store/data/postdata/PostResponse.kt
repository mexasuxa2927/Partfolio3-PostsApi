package com.example.watch_store.data.postdata

data class PostResponse(
    val `data`: List<Post>,
    val limit: Int,
    val page: Int,
    val total: Int
)