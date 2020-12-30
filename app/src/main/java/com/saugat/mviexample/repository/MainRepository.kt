package com.saugat.mviexample.repository

import androidx.lifecycle.LiveData
import com.saugat.mviexample.api.RetrofitBuilder
import com.saugat.mviexample.model.BlogPost
import com.saugat.mviexample.model.User
import com.saugat.mviexample.ui.main.state.MainViewState
import com.saugat.mviexample.util.ApiSuccessResponse
import com.saugat.mviexample.util.DataState
import com.saugat.mviexample.util.GenericApiResponse

object MainRepository {

    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(
                    null,
                    data = MainViewState(
                        blogPosts = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitBuilder.apiService.getBlogPosts()
            }

        }.asLiveData()
    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<User, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    null,
                    data = MainViewState(
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()
    }


}