package com.example.watch_store.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watch_store.data.User
import com.example.watch_store.data.postcomment.Comments
import com.example.watch_store.data.postdata.Post
import com.example.watch_store.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    private val mutableUser  = MutableLiveData<Result<List<User>>>()
    private val mutablePosts  = MutableLiveData<Result<List<Post>>>()
    private val mutablePostComment = MutableLiveData<Result<List<Comments>>>()
    private val mutableSinglePost  = MutableLiveData<Result<Post>>()
    private val mutableByTagPost   = MutableLiveData<Result<List<Post>>>()
    private val mutablePostsByUserId = MutableLiveData<Result<List<Post>>>()


    fun fetchUser(page: Int): MutableLiveData<Result<List<User>>> {
        viewModelScope.launch(Dispatchers.IO) {
            flow {emit(repository.getNetworkApi().getUser(20,page))  }.catch {
                mutableUser.postValue(Result.failure(it))
            }.collect{
                if(it.isSuccessful){
                    mutableUser.postValue(Result.success(it.body()?.data?: emptyList<User>()))
                }
                else{
                    mutableUser.postValue(Result.failure(Throwable(it.message())))
                }
            }
        }
        return mutableUser

    }

    fun fetchPosts(page:Int): MutableLiveData<Result<List<Post>>> {

        viewModelScope.launch(Dispatchers.IO) {
            flow { emit(repository.getNetworkApi().getPost(30,page)) }.catch {
                mutablePosts.postValue(Result.failure(it))
            }.collect{
                if(it.isSuccessful){
                    mutablePosts.postValue(Result.success(it.body()?.data?: emptyList()))
                }
                else{
                    mutablePosts.postValue(Result.failure(Throwable(it.message())))
                }
            }

        }
        return mutablePosts
    }

    fun getCommentById(id:String): MutableLiveData<Result<List<Comments>>> {
        viewModelScope.launch(Dispatchers.IO) {
            flow{emit(repository.getNetworkApi().getPostByIdComment(id))}.catch {
                mutablePostComment.postValue(Result.failure(it))

            }.collect{
                if(it.isSuccessful){
                    mutablePostComment.postValue(Result.success(it.body()!!.data?: emptyList()))

                }else{
                    mutablePostComment.postValue(Result.failure(Throwable(it.message())))

                }
            }
        }
        return mutablePostComment
    }


    fun getPostById(id: String): MutableLiveData<Result<Post>> {
        viewModelScope.launch(Dispatchers.IO) {
            flow{emit(repository.getNetworkApi().getPostById(id))}.catch {
                mutableSinglePost.postValue(Result.failure(it))
            }.collect{
                if(it.isSuccessful){
                    mutableSinglePost.postValue(Result.success(it.body()!!))
                }
                else{
                    mutableSinglePost.postValue(Result.failure(Throwable("ERROR")))
                }
            }

        }
        return mutableSinglePost
    }


    fun getPostByTag(tag:String): MutableLiveData<Result<List<Post>>> {
        viewModelScope.launch(Dispatchers.IO) {
            flow { emit(repository.getNetworkApi().getPostByTag(tag,50)) }.catch {
                mutableByTagPost.postValue(Result.failure(it))
            }.collect{
                mutableByTagPost.postValue(Result.success(it.body()?.data?: emptyList()))
            }

        }
        return mutableByTagPost
    }


    fun getPostsByUserId(id: String): MutableLiveData<Result<List<Post>>> {
        viewModelScope.launch(Dispatchers.IO) {
            flow {emit(repository.getNetworkApi().getPostsByUserId(id))  }.catch {
                mutablePostsByUserId.postValue(Result.failure(it))
            }.collect{
                if(it.isSuccessful){
                    mutablePostsByUserId.postValue(Result.success(it.body()?.data?: emptyList()))
                }else{
                    mutablePostsByUserId.postValue(Result.failure(Throwable(it.code().toString())))
                }
            }

        }

        return mutablePostsByUserId
    }


}

