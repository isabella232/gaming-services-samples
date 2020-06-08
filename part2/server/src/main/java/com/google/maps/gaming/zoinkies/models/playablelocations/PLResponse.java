package com.google.maps.gaming.zoinkies.models.playablelocations;

import java.util.Map;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLResponse {
  private Map<String, PLLocations> locationsPerGameObjectType;
  private String ttl;

  public String getTtl() {
    return ttl;
  }

  public void setTtl(String ttl) {
    this.ttl = ttl;
  }

  public Map<String, PLLocations> getLocationsPerGameObjectType() {
    return locationsPerGameObjectType;
  }

  public void setLocationsPerGameObjectType(
      Map<String, PLLocations> locationsPerGameObjectType) {
    this.locationsPerGameObjectType = locationsPerGameObjectType;
  }
}
