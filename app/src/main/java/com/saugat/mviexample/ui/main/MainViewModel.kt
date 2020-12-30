package com.saugat.mviexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.saugat.mviexample.model.BlogPost
import com.saugat.mviexample.model.User
import com.saugat.mviexample.repository.MainRepository
import com.saugat.mviexample.ui.main.state.MainStateEvent
import com.saugat.mviexample.ui.main.state.MainStateEvent.*
import com.saugat.mviexample.ui.main.state.MainViewState
import com.saugat.mviexample.util.AbsentLiveData
import com.saugat.mviexample.util.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->

            stateEvent?.let {
                handleStateEvent(it)
            }

        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        when (stateEvent) {
            is GetBlogPostsEvent -> {
                return MainRepository.getBlogPosts()
            }

            is GetUserEvent -> {
                return MainRepository.getUser(stateEvent.userId)
            }

            is None -> {
                return AbsentLiveData.create()
            }
        }
    }

    public fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    public fun setUser(user: User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    private fun getCurrentViewStateOrNew(): MainViewState {
        val value = viewState.value?.let {
            it
        } ?: MainViewState()
        return value
    }

    public fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }

}