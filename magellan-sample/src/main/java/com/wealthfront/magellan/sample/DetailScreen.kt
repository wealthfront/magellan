package com.wealthfront.magellan.sample

import android.content.Context

import com.wealthfront.magellan.Screen

internal class DetailScreen : Screen<DetailView>() {

    override fun createView(context: Context): DetailView {
        return DetailView(context)
    }

    override fun getTitle(context: Context): String {
        return "Detail Screen"
    }

}
