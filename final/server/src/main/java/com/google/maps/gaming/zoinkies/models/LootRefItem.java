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
   * Getter for weight
   * @return
   */
  public double getWeight() {
    return weight;
  }

  /**
   * Setter for weight
   * @param weight
   */
  public void setWeight(double weight) {
    this.weight = weight;
  }
  /**
   * Type of item (the id is reconcile in the game reference data.
   */
  private String itemId;

  /**
   * Getter for id
   * @return
   */
  public String getItemId() {
    return itemId;
  }

  /**
   * Setter for id
   * @param itemId
   */
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }
  /**
   * Minimum quantity awarded
   */
  private int minQuantity;

  /**
   * Getter for minQuantity
   * @return
   */
  public int getMinQuantity() {
    return minQuantity;
  }

  /**
   * Setter for minQuantity
   * @param minQuantity
   */
  public void setMinQuantity(int minQuantity) {
    this.minQuantity = minQuantity;
  }
  /**
   * Maximum quantity awarded
   */
  private int maxQuantity;

  /**
   * Getter for maxQuantity
   * @return
   */
  public int getMaxQuantity() {
    return maxQuantity;
  }

  /**
   * Setter for maxQuantity
   * @param maxQuantity
   */
  public void setMaxQuantity(int maxQuantity) {
    this.maxQuantity = maxQuantity;
  }

  public LootRefItem(
      String itemId,
      double weight,
      int minQuantity,
      int maxQuantity
  ) {
    this.itemId = itemId;
    this.weight = weight;
    this.minQuantity = minQuantity;
    this.maxQuantity = maxQuantity;
  }
}
