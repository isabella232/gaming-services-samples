package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLAreaFilter {
  private String s2CellId;

  public String getS2CellId() {
    return s2CellId;
  }

  public void setS2CellId(String s2CellId) {
    this.s2CellId = s2CellId;
  }
}
