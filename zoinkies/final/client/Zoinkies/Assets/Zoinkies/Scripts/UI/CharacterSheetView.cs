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

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     This class handles the character sheet logic.
    /// </summary>
    public class CharacterSheetView : BaseView
    {
        /// <summary>
        ///     Attack score
        /// </summary>
        public Text AttackIconScore;

        /// <summary>
        ///     Avatar name
        /// </summary>
        public InputField AvatarName;

        /// <summary>
        ///     Armor selector
        /// </summary>
        public MicroInventoryController BodyArmors;

        /// <summary>
        ///     Defense score
        /// </summary>
        public Text DefenseIconScore;

        /// <summary>
        ///     Diamond Keys
        /// </summary>
        public Text DiamondKeyQuantity;

        /// <summary>
        ///     Freed leaders
        /// </summary>
        public Text FreedLeadersQuantity;

        /// <summary>
        ///     Gold Keys
        /// </summary>
        public Text GoldKeyQuantity;

        /// <summary>
        ///     Helmet selector
        /// </summary>
        public MicroInventoryController Helmets;

        /// <summary>
        ///     Health
        /// </summary>
        public Scrollbar Lives;

        /// <summary>
        ///     Character types selector
        /// </summary>
        public MicroInventoryController Portraits;

        /// <summary>
        ///     Shield selector
        /// </summary>
        public MicroInventoryController Shields;

        /// <summary>
        ///     Weapon selector
        /// </summary>
        public MicroInventoryController Weapons;

        /// <summary>
        ///     Reference to player service
        /// </summary>
        private PlayerService _playerService = PlayerService.GetInstance();

        /// <summary>
        ///     Initializes player stats and inventory from player data.
        /// </summary>
        /// <returns></returns>
        public IEnumerator Init()
        {
            yield return new WaitUntil(() => _playerService.IsInitialized);

            AvatarName.text = _playerService.AvatarName;

            if (_playerService.GetMaxEnergyLevel() != 0)
            {
                Lives.size = (float) _playerService.GetEnergyLevel() / _playerService.GetMaxEnergyLevel();
            }

            GoldKeyQuantity.text = _playerService.GetNumberOfGoldKeys().ToString("N0");
            DiamondKeyQuantity.text = _playerService.GetNumberOfDiamondKeys().ToString("N0");
            DefenseIconScore.text = _playerService.GetDefenseScore().ToString("N0");
            AttackIconScore.text = _playerService.GetAttackScore().ToString("N0");
            FreedLeadersQuantity.text = _playerService.GetNumberOfFreedLeaders().ToString("N0");

            Portraits.InitItems(new List<Item>(_playerService.GetAvatars()), _playerService.AvatarType);
            Portraits.SelectionChanged += id =>
            {
                _playerService.AvatarType = id;
            };
            Weapons.InitItems(new List<Item>(_playerService.GetWeapons()), _playerService.EquippedWeapon);
            Weapons.SelectionChanged += id =>
            {
                _playerService.EquippedWeapon = id;
                AttackIconScore.text = _playerService.GetAttackScore().ToString("N0");
            };
            BodyArmors.InitItems(new List<Item>(_playerService.GetBodyArmors()),
                _playerService.EquippedBodyArmor);
            BodyArmors.SelectionChanged += id =>
            {
                _playerService.EquippedBodyArmor = id;
                DefenseIconScore.text = _playerService.GetDefenseScore().ToString("N0");
            };
            Helmets.InitItems(new List<Item>(_playerService.GetHelmets()), _playerService.EquippedHelmet);
            Helmets.SelectionChanged += id =>
            {
                _playerService.EquippedHelmet = id;
                DefenseIconScore.text = _playerService.GetDefenseScore().ToString("N0");
            };
            Shields.InitItems(new List<Item>(_playerService.GetShields()), _playerService.EquippedShield);
            Shields.SelectionChanged += id =>
            {
                _playerService.EquippedShield = id;
                DefenseIconScore.text = _playerService.GetDefenseScore().ToString("N0");
            };
        }

        /// <summary>
        ///     Updates the player name if it has changed.
        /// </summary>
        public void OnNameChanged()
        {
            _playerService.AvatarName = AvatarName.text;
        }

        /// <summary>
        ///     Checks if we are in First Time User Experience. Completes the FTUE.
        /// </summary>
        private void OnEnable()
        {
            // FTUE complete as soon as players reaches this view - basic example
            if (!PlayerPrefs.HasKey(GameConstants.FTUE_COMPLETE))
            {
                PlayerPrefs.SetInt(GameConstants.FTUE_COMPLETE, 1);
            }

            StartCoroutine(Init());
        }
    }
}
