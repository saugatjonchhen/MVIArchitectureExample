package com.saugat.mviexample.api

import androidx.lifecycle.LiveData
import com.saugat.mviexample.model.BlogPost
import com.saugat.mviexample.model.User
import com.saugat.mviexample.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPosts(): LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId: String
    ): LiveData<GenericApiResponse<User>>

}