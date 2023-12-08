package com.example.watch_store.data

data class UsersResponse(
    val `data`: List<User>,
    val limit: Int,
    val page: Int,
    val total: Int
)