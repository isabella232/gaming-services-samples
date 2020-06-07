﻿using System;
using System.Collections.Generic;
using System.Text;

namespace Google.Maps.Demos.Zoinkies {

 /// <summary>
 /// Models a player stats and inventory.
 /// </summary>
 public class PlayerData {
  /**
   * Generated device Id provided by user
   */
  public long id { get; set; }

  /**
   * Player's avatar name
   */
  public string name { get; set; }

  /**
   * Equipped weapon. Empty or null means nothing equipped.
   */
  public string equippedWeapon { get; set; }

  /**
   * Equipped shield. Empty or null means nothing equipped.
   */
  public string equippedShield { get; set; }

  /**
   * Equipped helmet. Empty or null means nothing equipped.
   */
  public string equippedHelmet { get; set; }

  /**
   * Equipped bodyarmor. Empty or null means nothing equipped.
   */
  public string equippedBodyArmor { get; set; }

  /**
   * Avatar's current energy level
   */
  public int energyLevel { get; set; }

  /**
   * Avatar's max energy level
   */
  public int maxEnergyLevel { get; set; }

  /**
   * The Player's character type
   */
  public string characterType { get; set; }

  /**
   * items Inventory
   */
  public List<Item> inventory { get; set; }

  public PlayerData() {

  }

  public PlayerData(String name,
   string CharacterType,
   int EnergyLevel,
   int MaxEnergyLevel,
   string EquippedWeapon,
   string EquippedHelmet,
   string EquippedBodyArmor,
   string EquippedShield,
   List<Item> Inventory) {
   this.name = name;
   this.characterType = CharacterType;
   this.energyLevel = EnergyLevel;
   this.maxEnergyLevel = MaxEnergyLevel;
   this.equippedWeapon = EquippedWeapon;
   this.equippedHelmet = EquippedHelmet;
   this.equippedBodyArmor = EquippedBodyArmor;
   this.equippedShield = EquippedShield;
   this.inventory = Inventory;
  }

  public override string ToString() {
   StringBuilder sb = new StringBuilder();
   sb.Append("User{" + "name=" + this.name + ", "
             + "characterType:" + this.characterType + ", "
             + "EnergyLevel:" + this.energyLevel + ", "
             + "MaxEnergyLevel:" + this.maxEnergyLevel + ", "
             + "EquippedWeapon:" + this.equippedWeapon + ", "
             + "EquippedHelmet:" + this.equippedHelmet + ", "
             + "EquippedBodyArmor:" + this.equippedBodyArmor + ", "
             + "EquippedShield:" + this.equippedShield + ", "
             + "Inventory=");
   if (inventory != null) {
    foreach (Item i in inventory) {
     sb.Append(i + " \n");
    }
   }
   else {
    sb.Append("Empty \n");
   }

   sb.Append("}");
   return sb.ToString();
  }
 }
}

