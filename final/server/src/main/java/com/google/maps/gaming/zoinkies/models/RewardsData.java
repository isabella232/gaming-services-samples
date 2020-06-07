package com.google.maps.gaming.zoinkies.models;

import java.util.ArrayList;
import java.util.List;

public class RewardsData {
  private String Id;
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
