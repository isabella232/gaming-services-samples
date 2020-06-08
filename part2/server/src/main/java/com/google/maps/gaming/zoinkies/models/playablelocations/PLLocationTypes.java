package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * Enumeration of supported location types to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public enum PLLocationTypes {
  food_and_drink,
  education,
  entertainment,
  finance,
  outdoor_recreation,
  retail,
  tourism,
  transit,
  transportation_infrastructure,
  wellness,
  undefined
}
