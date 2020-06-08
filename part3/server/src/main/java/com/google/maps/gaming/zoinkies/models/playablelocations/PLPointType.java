package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * Enumeration of point types to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public enum PLPointType {
  /// <summary>Unspecified point type. Do not use this value.</summary>
  Unspecified,

  /// <summary>
  /// The geographic coordinates correspond to the center of the location.
  /// </summary>
  CenterPoint,

  /// <summary>
  /// The geographic coordinates correspond to the location snapped to the
  /// sidewalk of the nearest road (when a nearby road exists).
  /// </summary>
  SnappedPoint,
}
