package com.wealthfront.magellan.sample.tools

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class Toaster @Inject constructor(private val context: Context) {

  fun showToast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
  }
}
