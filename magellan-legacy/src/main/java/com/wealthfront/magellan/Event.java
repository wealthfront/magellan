package com.wealthfront.magellan;

class Event {

  private NavigationType navigationType;
  private Direction direction;
  private String backStackDescription;

  Event(NavigationType navigationType, Direction direction, String backStackDescription) {
    this.navigationType = navigationType;
    this.direction = direction;
    this.backStackDescription = backStackDescription;
  }

  @Override
  public String toString() {
    return navigationType + " " + direction + " - Backstack: " + backStackDescription;
  }

}

