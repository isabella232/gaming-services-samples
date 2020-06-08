package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class PLLocation {
  private String name;
  private String placeId;
  private String plusCode;
  private PLLatLng centerPoint;
  private PLLatLng snappedPoint;
  private String[] types;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public String getPlusCode() {
    return plusCode;
  }

  public void setPlusCode(String plusCode) {
    this.plusCode = plusCode;
  }

  public PLLatLng getCenterPoint() {
    return centerPoint;
  }

  public void setCenterPoint(PLLatLng centerPoint) {
    this.centerPoint = centerPoint;
  }

  public PLLatLng getSnappedPoint() {
    return snappedPoint;
  }

  public void setSnappedPoint(
      PLLatLng snappedPoint) {
    this.snappedPoint = snappedPoint;
  }

  public String[] getTypes() {
    return types;
  }

  public void setTypes(String[] types) {
    this.types = types;
  }
}
