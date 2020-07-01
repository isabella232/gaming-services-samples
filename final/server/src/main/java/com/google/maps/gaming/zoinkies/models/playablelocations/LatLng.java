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
public class LatLng {

  /**
   * The latitude of a playable location
   */
  private double latitude;

  /**
   * The getter for latitude
   * @return
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * The setter for latitude
   * @param latitude
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * The longitude of a playable location
   */
  private double longitude;

  /**
   * The getter for longitude
   * @return
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * The setter for longitude
   * @param longitude
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  /**
   * Default constructor
   */
  public LatLng() {
  }

  /**
   * A constructor that takes a latitude and longitude doubles as input parameters
   * @param latitude
   * @param longitude
   */
  public LatLng(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
