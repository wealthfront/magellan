package com.wealthfront.magellan.sample.migration.api

import okhttp3.Interceptor
import okhttp3.Response

class RequestCountingInterceptor : Interceptor {

  companion object {

    var listener: CallEventListener? = null
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    listener?.onRequestStart()
    val response = chain.proceed(chain.request())
    listener?.onRequestEnd()
    return response
  }
}

interface CallEventListener {

  fun onRequestStart()
  fun onRequestEnd()
}
