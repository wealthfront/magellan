package com.wealthfront.magellan.kotlin

import java.util.*

internal class EvictingQueue<E>(private val maxSize: Int) : Iterable<E> {

  private val queue: Deque<E>

  init {
    queue = ArrayDeque(maxSize)
  }

  fun add(e: E) {
    if (queue.size == maxSize) {
      queue.remove()
    }
    queue.add(e)
  }

  override fun iterator(): Iterator<E> = queue.iterator()

}
