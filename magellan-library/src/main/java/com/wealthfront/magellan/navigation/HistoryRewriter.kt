package com.wealthfront.magellan.navigation

import java.util.Deque

interface HistoryRewriter {

  fun rewriteHistory(history: Deque<NavigationEvent>)
}