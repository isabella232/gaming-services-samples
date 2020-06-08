package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLFieldMask {
  private String[] paths;

  public String[] getPaths() {
    return paths;
  }

  public void setPaths(String[] paths) {
    this.paths = paths;
  }
}
