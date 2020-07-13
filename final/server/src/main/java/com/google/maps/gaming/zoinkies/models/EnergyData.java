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
  private String Id;

  /**
   * Getter for id
   * @return
   */
  public String getId() {
    return Id;
  }

  /**
   * Setter for id
   * @param id
   */
  public void setId(String id) {
    Id = id;
  }

  /**
   * Amount of energy restored for this player at this location.
   */
  private int AmountRestored;

  /**
   * Getter for AmountRestored
   * @return
   */
  public int getAmountRestored() {
    return AmountRestored;
  }

  /**
   * Setter for AmountRestored
   * @param amountRestored
   */
  public void setAmountRestored(int amountRestored) {
    AmountRestored = amountRestored;
  }
}
