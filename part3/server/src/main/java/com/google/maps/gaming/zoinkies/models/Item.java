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
 * This class models a (generic) game item. It is mostly used for the player's inventory.
 */
public class Item {

  /**
   * Type of object - the key must matched a value in the reference table.
   */
  private String ObjectType;
  /**
   * Quantity owned.
   */
  private int Quantity;

  public Item() {
  }

  public Item(String ObjectType, int Quantity) {
    this.ObjectType = ObjectType;
    this.Quantity = Quantity;
  }

  // region Getters/Setters
  public String getId() {
    return ObjectType;
  }
  public void setId(String ObjectType) {
    this.ObjectType = ObjectType;
  }
  public int getQuantity() {
    return Quantity;
  }
  public void setQuantity(int Quantity) {
    this.Quantity = Quantity;
  }
  // endregion
  @Override
  public String toString() {
    return "{Type: " + ObjectType + " Quantity: " + Quantity + "}";
  }
}
