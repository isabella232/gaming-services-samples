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
  private String name;
  private String placeId;
  private String plusCode;
  private LatLng centerPoint;
  private LatLng snappedPoint;
  private String[] types;

  // Reference to the parent S2CellId used for housekeeping
  private String S2CellId;

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

  public LatLng getCenterPoint() {
    return centerPoint;
  }

  public void setCenterPoint(LatLng centerPoint) {
    this.centerPoint = centerPoint;
  }

  public LatLng getSnappedPoint() {
    return snappedPoint;
  }

  public void setSnappedPoint(
      LatLng snappedPoint) {
    this.snappedPoint = snappedPoint;
  }

  public String[] getTypes() {
    return types;
  }

  public void setTypes(String[] types) {
    this.types = types;
  }

  public String getS2CellId() {
    return S2CellId;
  }

  public void setS2CellId(String s2CellId) {
    S2CellId = s2CellId;
  }
}
