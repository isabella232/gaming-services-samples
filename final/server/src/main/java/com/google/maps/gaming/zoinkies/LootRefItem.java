package com.google.maps.gaming.zoinkies;

public class LootRefItem {
  public double weight;
  public String objectTypeId;
  public int minQuantity;
  public int maxQuantity;

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
}
