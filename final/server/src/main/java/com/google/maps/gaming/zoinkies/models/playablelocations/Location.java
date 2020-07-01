/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.maps.gaming.zoinkies.models.playablelocations;

/**
 * POJO classes to map the json request / response to the playable locations REST API.
 * @see https://developers.google.com/maps/documentation/gaming/reference/playable_locations/rest
 *
 */
public class Location {
  /**
   * Reference to the parent S2CellId used for housekeeping
   */
  private String S2CellId;

  /**
   * Getter for s2CellId
   * @return
   */
  public String getS2CellId() {
    return S2CellId;
  }

  /**
   * Setter for s2CellId
   * @param s2CellId
   */
  public void setS2CellId(String s2CellId) {
    S2CellId = s2CellId;
  }

  /**
   * Unique identifier of a playable location
   */
  private String name;

  /**
   * Getter for name
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for name
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * PlaceId is the unique identifier for non-generated playable locations
   */
  private String placeId;

  /**
   * Getter for placeId
   * @return
   */
  public String getPlaceId() {
    return placeId;
  }

  /**
   * Setter for placeId
   * @param placeId
   */
  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  /**
   * PlusCode is the unique identifier for generated playable locations
   */
  private String plusCode;

  /**
   * Getter for plusCode
   * @return
   */
  public String getPlusCode() {
    return plusCode;
  }

  /**
   * Setter for plusCode
   * @param plusCode
   */
  public void setPlusCode(String plusCode) {
    this.plusCode = plusCode;
  }

  /**
   * Latitude longitude of the center point of a playable location
   */
  private LatLng centerPoint;

  /**
   * Getter for centerPoint
   * @return
   */
  public LatLng getCenterPoint() {
    return centerPoint;
  }

  /**
   * Setter for centerPoint
   * @param centerPoint
   */
  public void setCenterPoint(LatLng centerPoint) {
    this.centerPoint = centerPoint;
  }

  /**
   * Snapped point if the playable location to the closest road segment edge
   */
  private LatLng snappedPoint;

  /**
   * Getter for snappedPoint
   * @return
   */
  public LatLng getSnappedPoint() {
    return snappedPoint;
  }

  /**
   * Setter for snappedPoint
   * @param snappedPoint
   */
  public void setSnappedPoint(
      LatLng snappedPoint) {
    this.snappedPoint = snappedPoint;
  }

  /**
   * Collection of types
   */
  private String[] types;

  /**
   * Getter for types
   * @return
   */
  public String[] getTypes() {
    return types;
  }

  /**
   * Setter for types
   * @param types
   */
  public void setTypes(String[] types) {
    this.types = types;
  }
}
