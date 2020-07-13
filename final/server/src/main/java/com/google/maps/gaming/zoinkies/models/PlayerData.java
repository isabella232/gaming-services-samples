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
   * Getter for the generated id
   * @return
   */
  public long getId() {
    return Id;
  }

  /**
   * Setter for the generated id
   * @param id
   */
  public void setId(long id) {
    Id = id;
  }

  /**
   * Player's avatar name
   */
  private String Name;

  /**
   * Getter for avater name.
   * @return
   */
  public String getName() {
    return this.Name;
  }

  /**
   * Setter for avatar name.
   * @param Name
   */
  public void setName(String Name) {
    this.Name = Name;
  }

  /**
   * Equipped weapon. Empty or null means nothing equipped.
   */
  private String EquippedWeapon;

  /**
   * Getter for equipped weapon
   * @return
   */
  public String getEquippedWeapon() {
    return EquippedWeapon;
  }

  /**
   * Setter for equipped weapon
   * @param equippedWeapon
   */
  public void setEquippedWeapon(String equippedWeapon) {
    EquippedWeapon = equippedWeapon;
  }

  /**
   * Equipped shield. Empty or null means nothing equipped.
   */
  private String EquippedShield;

  /**
   * Getter for equipped shield
   * @return
   */
  public String getEquippedShield() {
    return EquippedShield;
  }

  /**
   * Setter for equipped shield
   * @param equippedShield
   */
  public void setEquippedShield(String equippedShield) {
    EquippedShield = equippedShield;
  }

  /**
   * Equipped helmet. Empty or null means nothing equipped.
   */
  private String EquippedHelmet;

  /**
   * Getter for equipped helmet
   * @return
   */
  public String getEquippedHelmet() {
    return EquippedHelmet;
  }

  /**
   * Setter for equipped helmet
   * @param equippedHelmet
   */
  public void setEquippedHelmet(String equippedHelmet) {
    EquippedHelmet = equippedHelmet;
  }

  /**
   * Equipped bodyarmor. Empty or null means nothing equipped.
   */
  private String EquippedBodyArmor;

  /**
   * Getter for equipped body armor
   * @return
   */
  public String getEquippedBodyArmor() {
    return EquippedBodyArmor;
  }

  /**
   * Setter for equipped body armor
   * @param equippedBodyArmor
   */
  public void setEquippedBodyArmor(String equippedBodyArmor) {
    EquippedBodyArmor = equippedBodyArmor;
  }

  /**
   * Avatar's current energy level
   */
  private int EnergyLevel;

  /**
   * Getter for energy level
   * @return
   */
  public int getEnergyLevel() {
    return EnergyLevel;
  }

  /**
   * Setter for energy level
   * @param energyLevel
   */
  public void setEnergyLevel(int energyLevel) {
    EnergyLevel = energyLevel;
  }

  /**
   * Avatar's max energy level
   */
  private int MaxEnergyLevel;

  /**
   * Getter for maximum energy level
   * @return
   */
  public int getMaxEnergyLevel() {
    return MaxEnergyLevel;
  }

  /**
   * Setter for maximum energy level
   * @param maxEnergyLevel
   */
  public void setMaxEnergyLevel(int maxEnergyLevel) {
    MaxEnergyLevel = maxEnergyLevel;
  }

  /**
   * The Player's character type
   */
  private String CharacterType;

  /**
   * Getter for character type
   * @return
   */
  public String getCharacterType() {
    return this.CharacterType;
  }

  /**
   * Setter for character type
   * @param CharacterType
   */
  public void setCharacterType(String CharacterType) {
    this.CharacterType = CharacterType;
  }

  /**
   * Items Inventory
   */
  private List<Item> Inventory;

  /**
   * Getter for inventory
   * @return
   */
  public List<Item> getInventory() {
    return this.Inventory;
  }

  /**
   * Setter for inventory
   * @param Inventory
   */
  public void setInventory(List<Item> Inventory) {
    this.Inventory = Inventory;
  }

  public PlayerData() {
  }

  /**
   * Specialized constructor.
   *
   * @param name
   * @param CharacterType
   * @param EnergyLevel
   * @param MaxEnergyLevel
   * @param Inventory
   */
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

  /**
   * ToString overload mostly for testing purposes
   * @return
   */
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
   * @param type The type of item
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

  /**
   * Checks if the item identified by the given id is in the player's inventory.
   *
   * @param itemId
   * @return A boolean
   * @throws Exception
   */
  public Boolean hasInventoryItem(String itemId) throws Exception {
    if (itemId == null || itemId.isEmpty()) {
      throw new Exception("Invalid item id received!");
    }
    // Search for the item
    List<Item> items = getInventoryItems(itemId);
    return (items.size()>0);
  }

  /**
   * Adds the sub collection of items to the inventory.
   * @param items a collection of items.
   * @throws Exception
   */
  public void addAllInventoryItems(Collection<Item> items) throws Exception {

    for (Item item:items) {
      addInventoryItem(item);
    }
  }

  /**
   * Adds the given item to the inventory.
   * If items of this type already exist in the inventory, we update the quantity.
   * @param item The item to add to the inventory.
   * @throws Exception
   */
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

  /**
   * Removes the given item from the inventory.
   *
   * @param item the item to remove from the inventory
   * @throws Exception
   */
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
