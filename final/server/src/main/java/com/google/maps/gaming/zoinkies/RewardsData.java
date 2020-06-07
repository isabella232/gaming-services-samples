package com.google.maps.gaming.zoinkies;

import java.util.ArrayList;
import java.util.List;

public class RewardsData {
  private String PlaceId;
  private List<Item> Items;

  public RewardsData() {
    Items = new ArrayList<>();
  }

  public String getPlaceId() {
    return PlaceId;
  }

  public void setPlaceId(String placeId) {
    PlaceId = placeId;
  }

  public List<Item> getItems() {
    return Items;
  }

  public void setItems(List<Item> items) {
    Items = items;
  }

}
