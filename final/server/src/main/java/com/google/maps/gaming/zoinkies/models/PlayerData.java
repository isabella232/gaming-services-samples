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

import com.google.maps.gaming.zoinkies.ITEMS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A POJO class used by the users REST API, and throughout the game logic, as it provides stats
 * and inventory details for the player.
 */
public class PlayerData {

  public ITEMS test;

  /**
   * Generated device Id provided by user
   */
  private long deviceId;

  /**
   * Getter for the generated id
   * @return
   */
  public long getDeviceId() {
    return deviceId;
  }

  /**
   * Setter for the generated id
   * @param deviceId
   */
  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * Player's avatar name
   */
  private String name;

  /**
   * Getter for avater name.
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for avatar name.
   * @param Name
   */
  public void setName(String Name) {
    this.name = Name;
  }

  /**
   * Equipped weapon. Empty or null means nothing equipped.
   */
  private ITEMS equippedWeapon;

  /**
   * Getter for equipped weapon
   * @return
   */
  public ITEMS getEquippedWeapon() {
    return equippedWeapon;
  }

  /**
   * Setter for equipped weapon
   * @param equippedWeapon
   */
  public void setEquippedWeapon(ITEMS equippedWeapon) {
    this.equippedWeapon = equippedWeapon;
  }

  /**
   * Equipped shield. Empty or null means nothing equipped.
   */
  private ITEMS equippedShield;

  /**
   * Getter for equipped shield
   * @return
   */
  public ITEMS getEquippedShield() {
    return equippedShield;
  }

  /**
   * Setter for equipped shield
   * @param equippedShield
   */
  public void setEquippedShield(ITEMS equippedShield) {
    this.equippedShield = equippedShield;
  }

  /**
   * Equipped helmet. Empty or null means nothing equipped.
   */
  private ITEMS equippedHelmet;

  /**
   * Getter for equipped helmet
   * @return
   */
  public ITEMS getEquippedHelmet() {
    return equippedHelmet;
  }

  /**
   * Setter for equipped helmet
   * @param equippedHelmet
   */
  public void setEquippedHelmet(ITEMS equippedHelmet) {
    this.equippedHelmet = equippedHelmet;
  }

  /**
   * Equipped bodyarmor. Empty or null means nothing equipped.
   */
  private ITEMS equippedBodyArmor;

  /**
   * Getter for equipped body armor
   * @return
   */
  public ITEMS getEquippedBodyArmor() {
    return equippedBodyArmor;
  }

  /**
   * Setter for equipped body armor
   * @param equippedBodyArmor
   */
  public void setEquippedBodyArmor(ITEMS equippedBodyArmor) {
    this.equippedBodyArmor = equippedBodyArmor;
  }

  /**
   * Avatar's current energy level
   */
  private int energyLevel;

  /**
   * Getter for energy level
   * @return
   */
  public int getEnergyLevel() {
    return energyLevel;
  }

  /**
   * Setter for energy level
   * @param energyLevel
   */
  public void setEnergyLevel(int energyLevel) {
    this.energyLevel = energyLevel;
  }

  /**
   * Avatar's max energy level
   */
  private int maxEnergyLevel;

  /**
   * Getter for maximum energy level
   * @return
   */
  public int getMaxEnergyLevel() {
    return maxEnergyLevel;
  }

  /**
   * Setter for maximum energy level
   * @param maxEnergyLevel
   */
  public void setMaxEnergyLevel(int maxEnergyLevel) {
    this.maxEnergyLevel = maxEnergyLevel;
  }

  /**
   * The Player's character type
   */
  private ITEMS characterType;

  /**
   * Getter for character type
   * @return
   */
  public ITEMS getCharacterType() {
    return this.characterType;
  }

  /**
   * Setter for character type
   * @param CharacterType
   */
  public void setCharacterType(ITEMS CharacterType) {
    this.characterType = CharacterType;
  }

  /**
   * Items Inventory
   */
  private List<Item> inventory;

  /**
   * Getter for inventory
   * @return
   */
  public List<Item> getInventory() {
    return this.inventory;
  }

  /**
   * Setter for inventory
   * @param Inventory
   */
  public void setInventory(List<Item> Inventory) {
    this.inventory = Inventory;
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
      ITEMS CharacterType,
      int EnergyLevel,
      int MaxEnergyLevel,
      List<Item> Inventory) {
    this.name = name;
    this.characterType = CharacterType;
    this.energyLevel = EnergyLevel;
    this.maxEnergyLevel = MaxEnergyLevel;
    this.inventory = Inventory;
  }

  /**
   * ToString overload mostly for testing purposes
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("User{" + "name='" + this.name + '\'' + ", "
        + "characterType:" + this.characterType + ", "
        + "equippedBodyArmor:" + this.equippedBodyArmor + ", "
        + "equippedHelmet:" + this.equippedHelmet + ", "
        + "equippedShield:" + this.equippedShield + ", "
        + "equippedWeapon:" + this.equippedWeapon + ", "
        + "EnergyLevel:" + this.energyLevel + ", "
        + "MaxEnergyLevel:" + this.maxEnergyLevel + ", "
        + "Inventory=");
    if (this.inventory != null) {
      for (Item i : this.inventory) {
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
  public List<Item> getInventoryItems(ITEMS type) {
    List<Item> items = new ArrayList<>();
    if (type == null) {
      return items;
    }

    for (Item i:this.inventory) {
      if (i.getItemId().equals(type)) {
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
  public Boolean hasInventoryItem(ITEMS itemId) throws Exception {
    if (itemId == null) {
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
    List<Item> items = getInventoryItems(item.getItemId());
    if (items.size()>0) {
      items.get(0).setQuantity(items.get(0).getQuantity()+item.getQuantity());
    } else {
      this.inventory.add(item);
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
    List<Item> items = getInventoryItems(item.getItemId());
    if (items.size()>0) {
      if (items.get(0).getQuantity() < item.getQuantity()) {
        throw new Exception("Not enough amount of " + items.get(0).getItemId() + " in inventory!");
      }
      items.get(0).setQuantity(items.get(0).getQuantity()-item.getQuantity());
    }
  }
}
