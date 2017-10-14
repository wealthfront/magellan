package com.wealthfront.magellan.kotlin

import android.app.Activity
import android.app.Dialog

interface DialogCreator {

  fun createDialog(activity: Activity): Dialog

}