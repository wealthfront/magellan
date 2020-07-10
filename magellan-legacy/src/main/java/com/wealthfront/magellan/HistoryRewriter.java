package com.wealthfront.magellan;

import android.app.Activity;

import java.util.Deque;

public interface HistoryRewriter {

  /**
   * Used to rewrite history on the fly, using either {@link Navigator#rewriteHistory(Activity, HistoryRewriter)} or
   * {@link Navigator#navigate(HistoryRewriter)} (and its variants).
   */
  void rewriteHistory(Deque<Screen> history);

}
