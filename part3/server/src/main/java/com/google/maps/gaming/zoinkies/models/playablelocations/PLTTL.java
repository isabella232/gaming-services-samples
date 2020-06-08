package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLTTL {
  private long seconds;

  public long getSeconds() {
    return seconds;
  }

  public void setSeconds(long seconds) {
    this.seconds = seconds;
  }
}
