package com.example.cryptoapp.constant.coingekko

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R

object ExchangeConstant {
    const val PER_PAGE: Int = 50
    const val PAGE: String = "1"

    fun ImageView.loadPng(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.color.orange)
            .error(R.color.orange)
            .into(this)
    }
}