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
public class Request {
  /**
   * Area filter for the request
   */
  private AreaFilter areaFilter;

  /**
   * Getter for areaFilter
   * @return
   */
  public AreaFilter getAreaFilter() {
    return areaFilter;
  }

  /**
   * Setter for areaFilter
   * @param areaFilter
   */
  public void setAreaFilter(
      AreaFilter areaFilter) {
    this.areaFilter = areaFilter;
  }

  /**
   * Collection of criteria for the request
   */
  private Criteria[] criteria;

  /**
   * Getter for criteria
   * @return
   */
  public Criteria[] getCriteria() {
    return criteria;
  }

  /**
   * Setter for criteria
   * @param criteria
   */
  public void setCriteria(Criteria[] criteria) {
    this.criteria = criteria;
  }
}
