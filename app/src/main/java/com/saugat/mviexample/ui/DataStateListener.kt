package com.saugat.mviexample.ui

import com.saugat.mviexample.util.DataState

interface DataStateListener {

    fun onDataStateChanged(dataState: DataState<*>?)

}