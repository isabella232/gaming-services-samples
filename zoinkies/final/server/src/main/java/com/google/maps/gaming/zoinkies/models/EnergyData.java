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
package com.google.maps.gaming.zoinkies.models;

/**
 * A POJO class used by the Energy Station REST API.
 */
public class EnergyData {

  /**
   * Location id
   */
  private String locationId;

  /**
   * Getter for id
   * @return
   */
  public String getLocationId() {
    return locationId;
  }

  /**
   * Setter for id
   * @param locationId
   */
  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  /**
   * Amount of energy restored for this player at this location.
   */
  private int amountRestored;

  /**
   * Getter for AmountRestored
   * @return
   */
  public int getAmountRestored() {
    return amountRestored;
  }

  /**
   * Setter for AmountRestored
   * @param amountRestored
   */
  public void setAmountRestored(int amountRestored) {
    this.amountRestored = amountRestored;
  }

  /**
   * Implementation of ToString for testing purposes.
   * @return a string that describes this energy data instance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("EnergyData{" + "Id='").append(this.locationId).append('\'').append(", ")
        .append("amountRestored:").append(this.amountRestored);
    return sb.toString();
  }
}
