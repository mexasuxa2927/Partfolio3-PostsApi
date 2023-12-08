package com.example.watch_store.network

import com.example.watch_store.data.UsersResponse
import com.example.watch_store.data.postcomment.CommentData
import com.example.watch_store.data.postdata.Post
import com.example.watch_store.data.postdata.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

   @GET("user/")
   @Headers("app-id:655c8cbe6cc4203cca56cc97")
   suspend fun getUser(@Query("limit")limit:Int,@Query("page")page: Int):Response<UsersResponse>


   @GET("post/")
   @Headers("app-id:655c8cbe6cc4203cca56cc97")
   suspend fun getPost(@Query("limit")limit:Int,@Query("page")page:Int):Response<PostResponse>

   @GET("post/{id}/comment/")
   @Headers("app-id:655c8cbe6cc4203cca56cc97")
   suspend fun getPostByIdComment(@Path("id")id:String):Response<CommentData>

   @GET("post/{id}/")
   @Headers("app-id:655c8cbe6cc4203cca56cc97")
   suspend fun getPostById(@Path("id")id:String):Response<Post>

   @GET("tag/{id}/post/")
   @Headers("app-id:655c8cbe6cc4203cca56cc97")
   suspend fun getPostByTag(@Path("id")id:String,@Query("limit")limit: Int):Response<PostResponse>



   @GET("user/{id}/post/")
   @Headers("app-id:655c8cbe6cc4203cca56cc97")
   suspend fun getPostsByUserId(@Path("id")id: String):Response<PostResponse>


}