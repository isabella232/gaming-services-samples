package com.google.maps.gaming.zoinkies.exceptions;

/**
 * Specific Exception used when locations are still respawning.
 */
public class LocationStillRespawningException  extends RuntimeException {
  public LocationStillRespawningException(String errorMessage) {
    super(errorMessage);
  }
}
