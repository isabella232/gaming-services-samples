package com.google.maps.gaming.zoinkies.models;

import com.google.maps.gaming.zoinkies.models.playablelocations.PLLatLng;

/**
 * A POJO class used to keep track of the client's world request.
 * The client provides a Lat Lng Rectangle, which is used to find all playable locations
 * within the overlapping S2 cells.
 */
public class WorldDataRequest {
  private PLLatLng northeast;
  private PLLatLng southwest;

  public PLLatLng getNortheast() {
    return northeast;
  }

  public void setNortheast(PLLatLng northeast) {
    this.northeast = northeast;
  }

  public PLLatLng getSouthwest() {
    return southwest;
  }

  public void setSouthwest(PLLatLng southwest) {
    this.southwest = southwest;
  }
}
