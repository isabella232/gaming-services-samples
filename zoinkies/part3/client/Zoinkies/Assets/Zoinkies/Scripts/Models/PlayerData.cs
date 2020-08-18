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

using System.Collections.Generic;
using System.Text;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     Models a player stats and inventory.
    /// </summary>
    public class PlayerData
    {
        /// <summary>
        /// Generated device Id provided by user
        /// </summary>
        public long deviceId { get; set; }

        /// <summary>
        /// Player's avatar name
        /// </summary>
        public string name { get; set; }

        /// <summary>
        /// Equipped weapon. Empty or null means nothing equipped.
        /// </summary>
        public string equippedWeapon { get; set; }

        /// <summary>
        /// Equipped shield. Empty or null means nothing equipped.
        /// </summary>
        public string equippedShield { get; set; }

        /// <summary>
        /// Equipped helmet. Empty or null means nothing equipped.
        /// </summary>
        public string equippedHelmet { get; set; }

        /// <summary>
        /// Equipped bodyarmor. Empty or null means nothing equipped.
        /// </summary>
        public string equippedBodyArmor { get; set; }

        /// <summary>
        /// Avatar's current energy level
        /// </summary>
        public int energyLevel { get; set; }

        /// <summary>
        /// Avatar's max energy level
        /// </summary>
        public int maxEnergyLevel { get; set; }

        /// <summary>
        /// The Player's character type
        /// </summary>
        public string characterType { get; set; }

        /// <summary>
        /// Items Inventory
        /// </summary>
        public List<Item> inventory { get; set; }

        public PlayerData()
        {
        }

        public PlayerData(
            long deviceId,
            string name,
            string characterType,
            int energyLevel,
            int maxEnergyLevel,
            string equippedWeapon,
            string equippedHelmet,
            string equippedBodyArmor,
            string equippedShield,
            List<Item> inventory)
        {
            this.deviceId = deviceId;
            this.name = name;
            this.characterType = characterType;
            this.energyLevel = energyLevel;
            this.maxEnergyLevel = maxEnergyLevel;
            this.equippedWeapon = equippedWeapon;
            this.equippedHelmet = equippedHelmet;
            this.equippedBodyArmor = equippedBodyArmor;
            this.equippedShield = equippedShield;
            this.inventory = inventory;
        }

        public PlayerData Clone()
        {
            return new PlayerData(
                deviceId,
                name,
                characterType,
                energyLevel,
                maxEnergyLevel,
                equippedWeapon,
                equippedHelmet,
                equippedBodyArmor,
                equippedShield,
                new List<Item>(inventory)
                );
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("User{" + "name=" + name + ", "
                      + "characterType:" + characterType + ", "
                      + "EnergyLevel:" + energyLevel + ", "
                      + "MaxEnergyLevel:" + maxEnergyLevel + ", "
                      + "EquippedWeapon:" + equippedWeapon + ", "
                      + "EquippedHelmet:" + equippedHelmet + ", "
                      + "EquippedBodyArmor:" + equippedBodyArmor + ", "
                      + "EquippedShield:" + equippedShield + ", "
                      + "Inventory=");
            if (inventory != null)
            {
                foreach (Item i in inventory)
                {
                    sb.Append(i + " \n");
                }
            }
            else
            {
                sb.Append("Empty \n");
            }

            sb.Append("}");
            return sb.ToString();
        }
    }
}
