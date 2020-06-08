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
