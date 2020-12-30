package com.saugat.mviexample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.saugat.mviexample.R
import com.saugat.mviexample.model.BlogPost
import com.saugat.mviexample.model.User
import com.saugat.mviexample.ui.DataStateListener
import com.saugat.mviexample.ui.main.state.MainStateEvent.GetBlogPostsEvent
import com.saugat.mviexample.ui.main.state.MainStateEvent.GetUserEvent
import com.saugat.mviexample.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_blog_list_item.*

class MainFragment : Fragment(), BlogListAdapter.Interaction {

    lateinit var viewModel: MainViewModel

    lateinit var dataStateHandler: DataStateListener

    lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            dataStateHandler.onDataStateChanged(dataState)

            //Handle Data<T>
            dataState.data?.let { event ->

                event.getContentIfNotHandled()?.let { mainViewState ->
                    /*handle loading and message*/
                    println("DEBUG: DataState: ${dataState}")

                    mainViewState.blogPosts?.let { blogPosts ->
                        /*set BlogPosts data*/
                        // set BlogPosts Data
                        viewModel.setBlogListData(blogPosts)
                    }

                    mainViewState.user?.let { user ->
                        /*set User Data*/
                        viewModel.setUser(user)
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            println("DEBUG: DataState: ${viewState}")
            viewState.blogPosts?.let {
                /*set BlogPosts data*/
                println("DEBUG: Setting blog posts to RecyclerView: ${viewState.blogPosts}")
                blogListAdapter.submitList(it)
            }

            viewState.user?.let {
                /*set User Data*/
                println("DEBUG: Setting user data: ${it}")
                setUserProperties(it)
            }
        })
    }

    private fun setUserProperties(user: User) {
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()

            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostsEvent())
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: $context must implement the data state listener")
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: CLICKED $position")
        println("DEBUG: CLICKED $item")
    }
}