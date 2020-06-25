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

using System;
using System.Collections.Generic;
using System.Linq;
using System.Xml;

namespace Google.Maps.Demos.Zoinkies
{
  /// <summary>
  ///     This class provides access to player data through helper functions.
  /// </summary>
  public class PlayerService
    {
        // Singleton pattern implementation
        private static PlayerService instance;

        // Quick access to the reference service
        private readonly ReferenceService RefService = ReferenceService.GetInstance();
        private PlayerService()
        {
            IsInitialized = false;
        }

        /// <summary>
        ///     Indicates if player data has been set (at least once).
        /// </summary>
        public bool IsInitialized { get; set; }

        /// <summary>
        ///     Indicates if player data has been modified in the client and needs to sync with the server.
        /// </summary>
        public bool DataHasChanged { get; set; }

        /// <summary>
        ///     Direct access to player data.
        ///     This is only accessible to other services or managers.
        /// </summary>
        internal PlayerData data { get; set; }

        /// <summary>
        ///     Returns the avatar name.
        /// </summary>
        public string AvatarName
        {
            get => data.name;
            set
            {
                data.name = value;
                DataHasChanged = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped helmet, null if nothing is equipped.
        /// </summary>
        public string EquippedHelmet
        {
            get => data.equippedHelmet;
            set
            {
                data.equippedHelmet = value;
                DataHasChanged = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped shield, null if nothing is equipped.
        /// </summary>
        public string EquippedShield
        {
            get => data.equippedShield;
            set
            {
                data.equippedShield = value;
                DataHasChanged = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped body armor, null if nothing is equipped.
        /// </summary>
        public string EquippedBodyArmor
        {
            get => data.equippedBodyArmor;
            set
            {
                data.equippedBodyArmor = value;
                DataHasChanged = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped weapon, null if nothing is equipped.
        /// </summary>
        public string EquippedWeapon
        {
            get => data.equippedWeapon;
            set
            {
                data.equippedWeapon = value;
                DataHasChanged = true;
            }
        }

        /// <summary>
        ///     Returns the type of the current avatar selection.
        /// </summary>
        public string AvatarType
        {
            get => data.characterType;
            set
            {
                data.characterType = value;
                DataHasChanged = true;
            }
        }

        public static PlayerService GetInstance()
        {
            if (instance == null)
            {
                instance = new PlayerService();
            }

            return instance;
        }

        /// <summary>
        ///     Initializes Player Data.
        /// </summary>
        /// <param name="data">A Player Data structure</param>
        /// <exception cref="Exception">Throws an exception if the provided data
        /// is invalid
        /// </exception>
        internal void Init(PlayerData data)
        {
            if (data == null)
            {
                throw new System.Exception("Invalid player data. Can't be null!");
            }

            this.data = data;
            IsInitialized = true;
            DataHasChanged = false;
        }

        /// <summary>
        ///     Returns the avatar's cooldown parameter.
        /// </summary>
        /// <returns>A duration</returns>
        public TimeSpan GetCooldown()
        {
            ReferenceItem weaponRef = RefService.GetItem(EquippedWeapon);
            return XmlConvert.ToTimeSpan(weaponRef.cooldown);
        }

        /// <summary>
        ///     Returns the avatar's attack score.
        /// </summary>
        /// <returns>The attack score as int</returns>
        public int GetAttackScore()
        {
            // Attack score is sum of all attack scores from character type and equipped weapon
            int score = 0;
            ReferenceItem type = RefService.GetItem(data.characterType);
            score += type.attackScore;

            if (EquippedWeapon != null)
            {
                score += RefService.GetItem(EquippedWeapon).attackScore;
            }

            return score;
        }

        /// <summary>
        ///     Returns the avatar's defense score.
        /// </summary>
        /// <returns>The defense score as int</returns>
        public int GetDefenseScore()
        {
            // Attack score is sum of all attack scores from character type and equipped weapon
            int score = 0;
            ReferenceItem type = RefService.GetItem(data.characterType);
            score += type.defenseScore;

            if (EquippedBodyArmor != null)
            {
                score += RefService.GetItem(EquippedBodyArmor).defenseScore;
            }

            if (EquippedHelmet != null)
            {
                score += RefService.GetItem(EquippedHelmet).defenseScore;
            }

            if (EquippedShield != null)
            {
                score += RefService.GetItem(EquippedShield).defenseScore;
            }

            return score;
        }

        /// <summary>
        ///     Increases the avatar's energy level.
        /// </summary>
        /// <param name="value">The amount to increase the energy by.</param>
        public void IncreaseEnergyLevel(int value)
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            data.energyLevel = Math.Min(data.energyLevel + value, data.maxEnergyLevel);
            DataHasChanged = true;
        }

        /// <summary>
        ///     Decreases the avatar's energy level.
        /// </summary>
        /// <param name="value">The amount to decrease the energy by</param>
        public void DecreaseEnergyLevel(int value)
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            data.energyLevel = Math.Max(data.energyLevel - value, 0);
            DataHasChanged = true;
        }

        /// <summary>
        ///     Returns the avatar's energy level.
        /// </summary>
        /// <returns></returns>
        public int GetEnergyLevel()
        {
            return data.energyLevel;
        }

        /// <summary>
        ///     Returns the avatar's max energy level.
        /// </summary>
        /// <returns></returns>
        public int GetMaxEnergyLevel()
        {
            return data.maxEnergyLevel;
        }

        /// <summary>
        ///     Adds new items or updates quantities of existing ones.
        /// </summary>
        /// <param name="items"></param>
        /// <exception cref="Exception"></exception>
        public void AddToInventory(List<Item> items)
        {
            // Add all items in the list to the current inventory
            if (items == null)
            {
                throw new System.Exception("Invalid items list input!");
            }

            if (data == null)
            {
                throw new System.Exception("data not initialized!");
            }

            foreach (Item item in items)
            {
                Item i = data.inventory.Find(s => s.id == item.id);
                if (i != null)
                {
                    i.quantity += i.quantity;
                }
                else
                {
                    data.inventory.Add(item);
                }
            }

            DataHasChanged = true;
        }

        /// <summary>
        ///     Returns the amount of gold keys in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public int GetNumberOfGoldKeys()
        {
            int qty = 0;

            Item i = data.inventory.Find(s => s.id == GameConstants.GOLD_KEY);
            if (i != null)
            {
                qty = i.quantity;
            }

            return qty;
        }

        /// <summary>
        ///     Returns the amount of diamond keys in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public int GetNumberOfDiamondKeys()
        {
            int qty = 0;

            Item i = data.inventory.Find(s => s.id == GameConstants.DIAMOND_KEY);
            if (i != null)
            {
                qty = i.quantity;
            }

            return qty;
        }

        /// <summary>
        ///     Returns the amount of freed leaders.
        /// </summary>
        /// <returns></returns>
        public int GetNumberOfFreedLeaders()
        {
            int qty = 0;

            Item i = data.inventory.Find(s => s.id == GameConstants.FREED_LEADERS);
            if (i != null)
            {
                qty = i.quantity;
            }

            return qty;
        }

        /// <summary>
        ///     Returns all weapons in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetWeapons()
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = RefService.GetWeapons();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all avatar types available to the player.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetAvatars()
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = RefService.GetAvatars();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all body armors in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetBodyArmors()
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = RefService.GetBodyArmors();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all helmets in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetHelmets()
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = RefService.GetHelmets();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all shields in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetShields()
        {
            if (data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = RefService.GetShields();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return data.inventory.Where(s => ids.Contains(s.id));
        }
    }
}
