package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLSpacingOptions {
  private int min_spacing_meters;
  private PLPointType PlPointType;

  public int getMin_spacing_meters() {
    return min_spacing_meters;
  }

  public void setMin_spacing_meters(int min_spacing_meters) {
    this.min_spacing_meters = min_spacing_meters;
  }

  public PLPointType getPlPointType() {
    return PlPointType;
  }

  public void setPlPointType(
      PLPointType plPointType) {
    PlPointType = plPointType;
  }
}
