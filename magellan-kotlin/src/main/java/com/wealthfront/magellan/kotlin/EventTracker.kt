package com.wealthfront.magellan.kotlin

internal class EventTracker(maxSize: Int) {

  private val events: EvictingQueue<Event> = EvictingQueue(maxSize)

  val eventsDescription: String
    get() {
      val eventsDescription = StringBuilder()
      events.forEach {
        eventsDescription.append("$it \n")
      }
      return eventsDescription.toString()
    }

  fun reportEvent(event: Event) {
    events.add(event)
  }

}
