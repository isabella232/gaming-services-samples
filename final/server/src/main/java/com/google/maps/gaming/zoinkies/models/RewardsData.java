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

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO class used to keep track of the player's rewards or losses.
 */
public class RewardsData {
  /**
   * Unique location identifier
   */
  private String Id;

  /**
   * Getter for Id
   * @return
   */
  public String getId() {
    return Id;
  }

  /**
   * Setter for Id
   * @param id
   */
  public void setId(String id) {
    Id = id;
  }
  /**
   * List of items found or lost at this location (negative quantity indicate losses)
   */
  private List<Item> Items;

  /**
   * Getter for Items
   * @return
   */
  public List<Item> getItems() {
    return Items;
  }

  /**
   * Setter for Items
   * @param items
   */
  public void setItems(List<Item> items) {
    Items = items;
  }

  /**
   * Default constructor - instantiates an empty items list.
   */
  public RewardsData() {
    Items = new ArrayList<>();
  }

  /**
   * ToString override, mostly for testing purposes.
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Id: " + Id);
    sb.append("Items: " + Items);
    return sb.toString();
  }
}
