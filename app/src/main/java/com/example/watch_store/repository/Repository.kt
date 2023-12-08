package com.example.watch_store.repository

import com.example.watch_store.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val networkservice:ApiService) {

    fun getNetworkApi():ApiService =  networkservice

}