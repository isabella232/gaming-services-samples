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
public class Filter {
  /**
   * The maximum number of locations returned in the query to playable locations
   */
  private int max_location_count;

  /**
   * Getter for max_location_count
   * @return
   */
  public int getMax_location_count() {
    return max_location_count;
  }

  /**
   * Setter for max_location_count
   * @param max_location_count
   */
  public void setMax_location_count(int max_location_count) {
    this.max_location_count = max_location_count;
  }

  /**
   * Minimum spacing requirement between two playable locations
   */
  private SpacingOptions spacing;

  /**
   * Getter for spacing
   * @return
   */
  public SpacingOptions getSpacing() {
    return spacing;
  }

  /**
   * Setter for spacing
   * @param spacing
   */
  public void setSpacing(SpacingOptions spacing) {
    this.spacing = spacing;
  }

  /**
   * The list of included playable locations types in the response
   */
  private String[] included_types;
  /**
   * Setter for included_types
   * @param included_types
   */
  public void setIncluded_types(String[] included_types) {
    this.included_types = included_types;
  }

  /**
   * Getter for included_types
   * @return
   */
  public String[] getIncluded_types() {
    return included_types;
  }
}
