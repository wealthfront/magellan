package com.wealthfront.magellan;

class EventTracker {

  private EvictingQueue<Event> events;

  EventTracker(final int maxSize) {
    events = new EvictingQueue<>(maxSize);
  }

  void reportEvent(Event event) {
    events.add(event);
  }

  String getEventsDescription() {
    StringBuilder eventsDescription = new StringBuilder();
    for (Event event : events) {
      eventsDescription.append(event);
      eventsDescription.append("\n");
    }
    return eventsDescription.toString();
  }

}
