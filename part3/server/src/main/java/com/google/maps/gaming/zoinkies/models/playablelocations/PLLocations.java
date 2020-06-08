package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLLocations {
  private PLLocation[] locations;

  public PLLocation[] getLocations() {
    return locations;
  }

  public void setLocations(PLLocation[] locations) {
    this.locations = locations;
  }
}
