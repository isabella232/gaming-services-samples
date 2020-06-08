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
 * This class holds reference data for loot items as generated in loot tables.
 */
public class LootRefItem {

  /**
   * Chances to find this item in the table
   */
  private double weight;
  /**
   * Type of item (the id is reconcile in the game reference data.
   */
  private String objectTypeId;
  /**
   * Min quantity awarded
   */
  private int minQuantity;
  /**
   * Max quantity awarded
   */
  private int maxQuantity;

  public LootRefItem(
      String objectTypeId,
      double weight,
      int minQuantity,
      int maxQuantity
  ) {
    this.objectTypeId = objectTypeId;
    this.weight = weight;
    this.minQuantity = minQuantity;
    this.maxQuantity = maxQuantity;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public String getObjectTypeId() {
    return objectTypeId;
  }

  public void setObjectTypeId(String objectTypeId) {
    this.objectTypeId = objectTypeId;
  }

  public int getMaxQuantity() {
    return maxQuantity;
  }

  public void setMaxQuantity(int maxQuantity) {
    this.maxQuantity = maxQuantity;
  }

  public int getMinQuantity() {
    return minQuantity;
  }

  public void setMinQuantity(int minQuantity) {
    this.minQuantity = minQuantity;
  }
}
