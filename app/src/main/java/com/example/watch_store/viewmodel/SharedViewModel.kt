package com.example.watch_store.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.watch_store.data.User

class SharedViewModel:ViewModel(){
    private var string  = MutableLiveData<String>()
    private var tag = MutableLiveData<String>()
    private var userId  = MutableLiveData<User>()

    fun setUserId(user:User){
        userId.postValue(user)
    }

    fun getUserId(): MutableLiveData<User> {
        return userId
    }

    fun setProductId(id:String){
        string.postValue(id)
    }

    fun getProductId(): MutableLiveData<String> {
        return string
    }

    fun setTag(tags:String){
        tag.postValue(tags)
    }
    fun getTag():MutableLiveData<String>{
        return tag
    }

}