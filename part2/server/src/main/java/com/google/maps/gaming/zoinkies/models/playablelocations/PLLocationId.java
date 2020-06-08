package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLLocationId {

  private String PlaceId;
  private String plus_code;
  private PLLocationId() {
  }

  public String getPlaceId() {
    return PlaceId;
  }

  public void setPlaceId(String placeId) {
    PlaceId = placeId;
  }

  public String getPlus_code() {
    return plus_code;
  }

  public void setPlus_code(String plus_code) {
    this.plus_code = plus_code;
  }
}
