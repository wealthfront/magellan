package com.wealthfront.magellan.kotlin

import java.util.*

interface HistoryRewriter {

  /**
   * Used to rewrite history on the fly, using either [Navigator.rewriteHistory] or
   * [Navigator.navigate] (and its variants).
   */
  fun rewriteHistory(history: Deque<Screen<*>>)

}