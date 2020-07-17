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

import com.google.maps.gaming.zoinkies.ITEMS;

/**
 * This class models a (generic) game item. It is mostly used for the player's inventory.
 */
public class Item {

  /**
   * The id of the object as defined in the reference data file.
   */
  private ITEMS itemId;

  /**
   * Getter for id
   * @return
   */
  public ITEMS getItemId() {
    return itemId;
  }

  /**
   * Setter for id
   * @param itemId
   */
  public void setItemId(ITEMS itemId) {
    this.itemId = itemId;
  }
  /**
   * Quantity owned.
   */
  private int quantity;

  /**
   * Getter for quantity
   * @return
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Setter for quantity
   * @param Quantity
   */
  public void setQuantity(int Quantity) {
    this.quantity = Quantity;
  }

  public Item() {
  }

  /**
   * A constructor that takes an id and a quantity as parameters
   * @param itemId
   * @param Quantity
   */
  public Item(ITEMS itemId, int Quantity) {
    this.itemId = itemId;
    this.quantity = Quantity;
  }

  @Override
  public String toString() {
    return "{Type: " + itemId + " Quantity: " + quantity + "}";
  }
}
