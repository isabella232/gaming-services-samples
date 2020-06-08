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
