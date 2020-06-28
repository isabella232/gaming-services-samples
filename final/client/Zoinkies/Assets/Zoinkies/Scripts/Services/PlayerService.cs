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
        private static PlayerService _instance;
        public static PlayerService GetInstance()
        {
            if (_instance == null)
            {
                _instance = new PlayerService();
            }

            return _instance;
        }
        private PlayerService()
        {
            IsInitialized = false;
        }

        /// <summary>
        ///     Indicates if player data has been set (at least once).
        /// </summary>
        public bool IsInitialized { get; set; }

        /// <summary>
        /// Returns a copy of player data
        /// </summary>
        public PlayerData Data => _data.Clone();

        /// <summary>
        ///     Reference to player data
        /// </summary>
        private PlayerData _data;

        /// <summary>
        ///     Returns the avatar name.
        /// </summary>
        public string AvatarName
        {
            get => _data.name;
            set
            {
                _data.name = value;
                _dataIsUntrusted = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped helmet, null if nothing is equipped.
        /// </summary>
        public string EquippedHelmet
        {
            get => _data.equippedHelmet;
            set
            {
                _data.equippedHelmet = value;
                _dataIsUntrusted = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped shield, null if nothing is equipped.
        /// </summary>
        public string EquippedShield
        {
            get => _data.equippedShield;
            set
            {
                _data.equippedShield = value;
                _dataIsUntrusted = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped body armor, null if nothing is equipped.
        /// </summary>
        public string EquippedBodyArmor
        {
            get => _data.equippedBodyArmor;
            set
            {
                _data.equippedBodyArmor = value;
                _dataIsUntrusted = true;
            }
        }

        /// <summary>
        ///     Returns the id of the equipped weapon, null if nothing is equipped.
        /// </summary>
        public string EquippedWeapon
        {
            get => _data.equippedWeapon;
            set
            {
                _data.equippedWeapon = value;
                _dataIsUntrusted = true;
            }
        }

        /// <summary>
        ///     Returns the type of the current avatar selection.
        /// </summary>
        public string AvatarType
        {
            get => _data.characterType;
            set
            {
                _data.characterType = value;
                _dataIsUntrusted = true;
            }
        }

        /// <summary>
        /// Returns DataIsUntrusted
        /// </summary>
        public bool DataIsUntrusted => _dataIsUntrusted;

        /// <summary>
        ///     Indicates if player data has been modified in the client and needs to sync with the server.
        /// </summary>
        private bool _dataIsUntrusted;

        /// <summary>
        /// Quick access to the reference service.
        /// </summary>
        private readonly ReferenceService _referenceService = ReferenceService.GetInstance();

        /// <summary>
        ///     Initializes Player Data.
        /// </summary>
        /// <param name="data">A Player Data structure</param>
        /// <exception cref="Exception">
        ///     Throws an exception if the provided data
        ///     is invalid
        /// </exception>
        internal void Init(PlayerData data)
        {
            if (data == null)
            {
                throw new System.Exception("Invalid player data. Can't be null!");
            }

            _data = data;
            IsInitialized = true;
            _dataIsUntrusted = false;
        }

        /// <summary>
        ///     Returns the avatar's cooldown parameter.
        /// </summary>
        /// <returns>A duration</returns>
        public TimeSpan GetCooldown()
        {
            ReferenceItem weaponRef = _referenceService.GetItem(EquippedWeapon);
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
            ReferenceItem type = _referenceService.GetItem(_data.characterType);
            score += type.attackScore;

            if (EquippedWeapon != null)
            {
                score += _referenceService.GetItem(EquippedWeapon).attackScore;
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
            ReferenceItem type = _referenceService.GetItem(_data.characterType);
            score += type.defenseScore;

            if (EquippedBodyArmor != null)
            {
                score += _referenceService.GetItem(EquippedBodyArmor).defenseScore;
            }

            if (EquippedHelmet != null)
            {
                score += _referenceService.GetItem(EquippedHelmet).defenseScore;
            }

            if (EquippedShield != null)
            {
                score += _referenceService.GetItem(EquippedShield).defenseScore;
            }

            return score;
        }

        /// <summary>
        ///     Increases the avatar's energy level.
        /// </summary>
        /// <param name="value">The amount to increase the energy by.</param>
        public void IncreaseEnergyLevel(int value)
        {
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            _data.energyLevel = Math.Min(_data.energyLevel + value, _data.maxEnergyLevel);
            _dataIsUntrusted = true;
        }

        /// <summary>
        ///     Decreases the avatar's energy level.
        /// </summary>
        /// <param name="value">The amount to decrease the energy by</param>
        public void DecreaseEnergyLevel(int value)
        {
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            _data.energyLevel = Math.Max(_data.energyLevel - value, 0);
            _dataIsUntrusted = true;
        }

        /// <summary>
        ///     Returns the avatar's energy level.
        /// </summary>
        /// <returns></returns>
        public int GetEnergyLevel()
        {
            return _data.energyLevel;
        }

        /// <summary>
        ///     Returns the avatar's max energy level.
        /// </summary>
        /// <returns></returns>
        public int GetMaxEnergyLevel()
        {
            return _data.maxEnergyLevel;
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

            if (_data == null)
            {
                throw new System.Exception("data not initialized!");
            }

            foreach (Item item in items)
            {
                Item i = _data.inventory.Find(s => s.id == item.id);
                if (i != null)
                {
                    i.quantity += i.quantity;
                }
                else
                {
                    _data.inventory.Add(item);
                }
            }

            _dataIsUntrusted = true;
        }

        /// <summary>
        ///     Returns the amount of gold keys in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public int GetNumberOfGoldKeys()
        {
            int qty = 0;

            Item i = _data.inventory.Find(s => s.id == GameConstants.GOLD_KEY);
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

            Item i = _data.inventory.Find(s => s.id == GameConstants.DIAMOND_KEY);
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

            Item i = _data.inventory.Find(s => s.id == GameConstants.FREED_LEADERS);
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
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = _referenceService.GetWeapons();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return _data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all avatar types available to the player.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetAvatars()
        {
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = _referenceService.GetAvatars();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return _data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all body armors in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetBodyArmors()
        {
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = _referenceService.GetBodyArmors();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return _data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all helmets in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetHelmets()
        {
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = _referenceService.GetHelmets();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return _data.inventory.Where(s => ids.Contains(s.id));
        }

        /// <summary>
        ///     Returns all shields in the player's inventory.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Item> GetShields()
        {
            if (_data == null)
            {
                throw new System.Exception(" data not initialized!");
            }

            IEnumerable<ReferenceItem> refItems = _referenceService.GetShields();
            List<string> ids = refItems.Select(item => item.id).ToList();

            return _data.inventory.Where(s => ids.Contains(s.id));
        }
    }
}
