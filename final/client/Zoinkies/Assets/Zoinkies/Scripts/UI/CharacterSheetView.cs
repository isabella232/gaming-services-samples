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
        ///     Reference to player service
        /// </summary>
        private readonly PlayerService service = PlayerService.GetInstance();

        /// <summary>
        ///     Shield selector
        /// </summary>
        public MicroInventoryController Shields;

        /// <summary>
        ///     Weapon selector
        /// </summary>
        public MicroInventoryController Weapons;

        /// <summary>
        ///     Checks if we are in FTUE. Completes FTUE.
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

        /// <summary>
        ///     Initializes player stats and inventory from player data.
        /// </summary>
        /// <returns></returns>
        public IEnumerator Init()
        {
            yield return new WaitUntil(() => service.IsInitialized);

            AvatarName.text = service.AvatarName;

            if (service.GetMaxEnergyLevel() != 0)
            {
                Lives.size = (float) service.GetEnergyLevel() / service.GetMaxEnergyLevel();
            }

            GoldKeyQuantity.text = service.GetNumberOfGoldKeys().ToString("N0");
            DiamondKeyQuantity.text = service.GetNumberOfDiamondKeys().ToString("N0");
            DefenseIconScore.text = service.GetDefenseScore().ToString("N0");
            AttackIconScore.text = service.GetAttackScore().ToString("N0");
            FreedLeadersQuantity.text = service.GetNumberOfFreedLeaders().ToString("N0");

            Portraits.InitItems(new List<Item>(service.GetAvatars()), service.AvatarType);
            Portraits.SelectionChanged += id =>
            {
                Debug.Log("Character type changed");
                service.AvatarType = id;
            };

            Weapons.InitItems(new List<Item>(service.GetWeapons()), service.EquippedWeapon);
            Weapons.SelectionChanged += id =>
            {
                Debug.Log("Weapon type changed");
                service.EquippedWeapon = id;
                AttackIconScore.text = service.GetAttackScore().ToString("N0");
            };
            BodyArmors.InitItems(new List<Item>(service.GetBodyArmors()),
                service.EquippedBodyArmor);
            BodyArmors.SelectionChanged += id =>
            {
                Debug.Log("Body Armor changed");
                service.EquippedBodyArmor = id;
                DefenseIconScore.text = service.GetDefenseScore().ToString("N0");
            };
            Helmets.InitItems(new List<Item>(service.GetHelmets()), service.EquippedHelmet);
            Helmets.SelectionChanged += id =>
            {
                Debug.Log("Helmet changed");
                service.EquippedHelmet = id;
                DefenseIconScore.text = service.GetDefenseScore().ToString("N0");
            };
            Shields.InitItems(new List<Item>(service.GetShields()), service.EquippedShield);
            Shields.SelectionChanged += id =>
            {
                Debug.Log("Shield changed");
                service.EquippedShield = id;
                DefenseIconScore.text = service.GetDefenseScore().ToString("N0");
            };
        }

        /// <summary>
        ///     Updates the player name if it has changed.
        /// </summary>
        public void OnNameChanged()
        {
            service.AvatarName = AvatarName.text;
            Debug.Log("New name " + service.AvatarName);
        }
    }
}
