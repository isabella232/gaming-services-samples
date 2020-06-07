package com.google.maps.gaming.zoinkies.models.playablelocations;

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
