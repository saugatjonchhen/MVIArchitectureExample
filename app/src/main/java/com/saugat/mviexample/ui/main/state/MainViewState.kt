package com.saugat.mviexample.ui.main.state

import com.saugat.mviexample.model.BlogPost
import com.saugat.mviexample.model.User

data class MainViewState(
    var blogPosts: List<BlogPost>? = null,
    var user: User? = null
)