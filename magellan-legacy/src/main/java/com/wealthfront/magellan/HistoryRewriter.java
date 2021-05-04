package com.wealthfront.magellan;

import android.app.Activity;
import android.view.View;

import com.wealthfront.magellan.navigation.NavigableCompat;

import java.util.Deque;

public interface HistoryRewriter {

  /**
   * Used to rewrite history on the fly, using either {@link Navigator#rewriteHistory(Activity, HistoryRewriter)} or
   * {@link Navigator#navigate(HistoryRewriter)} (and its variants).
   */
  void rewriteHistory(Deque<NavigableCompat<View>> history);

}