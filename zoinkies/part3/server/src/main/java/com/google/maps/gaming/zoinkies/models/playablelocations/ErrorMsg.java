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
public class ErrorMsg {
  /**
   * Error code eventually returned by the query to playable locations
   */
  private long error_code;

  /**
   * Getter for error_code
   * @return
   */
  public long getError_code() {
    return error_code;
  }

  /**
   * Setter for error_code
   * @param error_code
   */
  public void setError_code(long error_code) {
    this.error_code = error_code;
  }

  /**
   * Error message eventually returned by the query to playable locations
   */
  private String error_msg;

  /**
   * Getter for error_msg
   * @return
   */
  public String getError_msg() {
    return error_msg;
  }

  /**
   * Setter for error_msg
   * @param error_msg
   */
  public void setError_msg(String error_msg) {
    this.error_msg = error_msg;
  }
}
