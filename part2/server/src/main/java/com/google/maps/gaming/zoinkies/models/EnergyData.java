package com.google.maps.gaming.zoinkies.models;

/**
 * A POJO class used by the Energy Station REST API.
 */
public class EnergyData {

  /**
   * Location id
   */
  private String Id;
  /**
   * Amount of energy restored for this player at this location.
   */
  private int AmountRestored;

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public int getAmountRestored() {
    return AmountRestored;
  }

  public void setAmountRestored(int amountRestored) {
    AmountRestored = amountRestored;
  }
}
