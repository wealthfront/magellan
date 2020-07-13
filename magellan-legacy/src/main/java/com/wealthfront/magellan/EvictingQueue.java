package com.wealthfront.magellan;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

class EvictingQueue<E> implements Iterable<E> {

  private final Deque<E> queue;
  private final int maxSize;

  EvictingQueue(final int maxSize) {
    this.maxSize = maxSize;
    queue = new ArrayDeque<>(maxSize);
  }

  public void add(E e) {
    if (queue.size() == maxSize) {
      queue.remove();
    }
    queue.add(e);
  }

  @Override
  public Iterator<E> iterator() {
    return queue.iterator();
  }

}
