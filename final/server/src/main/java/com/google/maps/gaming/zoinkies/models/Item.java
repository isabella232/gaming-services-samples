package com.google.maps.gaming.zoinkies.models;

/**
 * This class models an item in the game.
 */
public class Item {

  private String ObjectType;
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
