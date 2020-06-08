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
   * List of items found or lost at this location (negative quantity indicate losses)
   */
  private List<Item> Items;

  public RewardsData() {
    Items = new ArrayList<>();
  }

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public List<Item> getItems() {
    return Items;
  }

  public void setItems(List<Item> items) {
    Items = items;
  }

}
