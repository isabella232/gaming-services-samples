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
using UnityEngine;
using UnityEngine.Assertions;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     View class for the battle defeat dialog.
    /// </summary>
    public class LootResultsDialog : BaseView
    {
        public GameObject ItemsContainer;
        public Text Title;

        private void Start()
        {
            Assert.IsNotNull(ItemsContainer);
            Assert.IsNotNull(Title);
        }

        /// <summary>
        ///     Initializes the listing of items gained or lost during battle.
        /// </summary>
        /// <param name="data"></param>
        /// <exception cref="Exception"></exception>
        public void Init(string title, List<Item> items)
        {
            if (items == null || string.IsNullOrEmpty(title))
            {
                throw new System.Exception("Invalid data received.");
            }

            Title.text = title; //data.winner? "Victory!":"Defeat!";

            ItemGO ItemGOPrefab = Resources.Load<ItemGO>("ItemPrefab");
            if (ItemGOPrefab == null)
            {
                throw new System.Exception("Can't instantiate a game object of type ItemPrefab!");
            }

            // Wipe out previous
            foreach (Transform child in ItemsContainer.transform)
            {
                Destroy(child.gameObject);
            }

            // Display all items - these items are LOST!
            foreach (Item i in items)
            {
                // Create an ItemGO, parent it to the container
                ItemGO go = Instantiate(ItemGOPrefab, ItemsContainer.transform, true);
                if (go != null)
                {
                    // Set the item name, image
                    go.Init(i, true);
                }
            }
        }
    }
}
