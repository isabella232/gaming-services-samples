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
import java.util.Collection;
import java.util.List;

/**
 * A POJO class used by the users REST API, and throughout the game logic, as it provides stats
 * and inventory details for the player.
 */
public class PlayerData {

  /**
   * Generated device Id provided by user
   */
  private long Id;
  /**
   * Player's avatar name
   */
  private String Name;

  /**
   * Equipped weapon. Empty or null means nothing equipped.
   */
  private String EquippedWeapon;

  /**
   * Equipped shield. Empty or null means nothing equipped.
   */
  private String EquippedShield;

  /**
   * Equipped helmet. Empty or null means nothing equipped.
   */
  private String EquippedHelmet;

  /**
   * Equipped bodyarmor. Empty or null means nothing equipped.
   */
  private String EquippedBodyArmor;

  /**
   * Avatar's current energy level
   */
  private int EnergyLevel;

  /**
   * Avatar's max energy level
   */
  private int MaxEnergyLevel;

  /**
   * The Player's character type
   */
  private String CharacterType;

  /**
   * Items Inventory
   */
  private List<Item> Inventory;

  public PlayerData() {

  }

  public PlayerData(String name,
      String CharacterType,
      int EnergyLevel,
      int MaxEnergyLevel,
      List<Item> Inventory) {
    this.Name = name;
    this.CharacterType = CharacterType;
    this.EnergyLevel = EnergyLevel;
    this.MaxEnergyLevel = MaxEnergyLevel;
    this.Inventory = Inventory;
  }

  // region Getters/Setters

  public long getId() {
    return Id;
  }

  public void setId(long id) {
    Id = id;
  }

  public String getName() {
    return this.Name;
  }

  public void setName(String Name) {
    this.Name = Name;
  }

  public String getEquippedBodyArmor() {
    return EquippedBodyArmor;
  }

  public void setEquippedBodyArmor(String equippedBodyArmor) {
    EquippedBodyArmor = equippedBodyArmor;
  }

  public String getEquippedHelmet() {
    return EquippedHelmet;
  }

  public void setEquippedHelmet(String equippedHelmet) {
    EquippedHelmet = equippedHelmet;
  }

  public String getEquippedShield() {
    return EquippedShield;
  }

  public void setEquippedShield(String equippedShield) {
    EquippedShield = equippedShield;
  }

  public String getEquippedWeapon() {
    return EquippedWeapon;
  }

  public void setEquippedWeapon(String equippedWeapon) {
    EquippedWeapon = equippedWeapon;
  }

  public String getCharacterType() {
    return this.CharacterType;
  }

  public void setCharacterType(String CharacterType) {
    this.CharacterType = CharacterType;
  }

  public int getEnergyLevel() {
    return EnergyLevel;
  }

  public void setEnergyLevel(int energyLevel) {
    EnergyLevel = energyLevel;
  }

  public int getMaxEnergyLevel() {
    return MaxEnergyLevel;
  }

  public void setMaxEnergyLevel(int maxEnergyLevel) {
    MaxEnergyLevel = maxEnergyLevel;
  }

  public List<Item> getInventory() {
    return this.Inventory;
  }

  public void setInventory(List<Item> Inventory) {
    this.Inventory = Inventory;
  }
  // endregion

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("User{" + "name='" + this.Name + '\'' + ", "
        + "characterType:" + this.CharacterType + ", "
        + "equippedBodyArmor:" + this.EquippedBodyArmor + ", "
        + "equippedHelmet:" + this.EquippedHelmet + ", "
        + "equippedShield:" + this.EquippedShield + ", "
        + "equippedWeapon:" + this.EquippedWeapon + ", "
        + "EnergyLevel:" + this.EnergyLevel + ", "
        + "MaxEnergyLevel:" + this.MaxEnergyLevel + ", "
        + "Inventory=");
    if (this.Inventory != null) {
      for (Item i : this.Inventory) {
        sb.append(i.toString() + " \n");
      }
    } else {
      sb.append("Empty \n");
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * Returns all items of a given type
   * @param type
   * @return
   */
  public List<Item> getInventoryItems(String type) {
    List<Item> items = new ArrayList<>();
    if (type == null || type.isEmpty()) {
      return items;
    }

    for (Item i:this.Inventory) {
      if (i.getId().equals(type)) {
        items.add(i);
      }
    }
    return items;
  }

  public Boolean hasInventoryItem(String itemId) throws Exception {
    if (itemId == null || itemId.isEmpty()) {
      throw new Exception("Invalid item id received!");
    }
    // Search for the item
    List<Item> items = getInventoryItems(itemId);
    return (items.size()>0);
  }

  public void addAllInventoryItems(Collection<Item> items) throws Exception {

    for (Item item:items) {
      addInventoryItem(item);
    }
  }

  public void addInventoryItem(Item item) throws Exception {
    if (item == null) {
      throw new Exception("Invalid item received!");
    }
    // Search for the item
    Item curr = null;
    List<Item> items = getInventoryItems(item.getId());
    if (items.size()>0) {
      items.get(0).setQuantity(items.get(0).getQuantity()+item.getQuantity());
    } else {
      this.Inventory.add(item);
    }
  }

  public void removeInventoryItem(Item item) throws Exception {
    if (item == null) {
      throw new Exception("Invalid item received!");
    }
    // Search for the item
    Item curr = null;
    List<Item> items = getInventoryItems(item.getId());
    if (items.size()>0) {
      if (items.get(0).getQuantity() < item.getQuantity()) {
        throw new Exception("Not enough amount of " + items.get(0).getId() + " in inventory!");
      }
      items.get(0).setQuantity(items.get(0).getQuantity()-item.getQuantity());
    }
  }
}
