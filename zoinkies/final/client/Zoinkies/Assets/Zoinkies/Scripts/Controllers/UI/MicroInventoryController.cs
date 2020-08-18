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
using UnityEngine;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     This class represents a mini inventory for a specific collection of game items.
    ///     Game items could be weapons, body armors or event character types.
    ///     The controller keeps track of the actual inventory of the item type and
    ///     provides support for navigating forward and backward through the inventory.
    /// </summary>
    /// <remarks>
    ///     Some collections might be empty. When this happens, both navigation buttons
    ///     are disabled, and no item is shown.  If later an item is added to this inventory
    ///     but isn't selected, we still display an empty visual but the navigation buttons
    ///     are now enabled.
    /// </remarks>
    public class MicroInventoryController : MonoBehaviour
    {

        /// <summary>
        ///     Reference to the inventory of items
        /// </summary>
        public List<ItemView> ItemsCollection;

        /// <summary>
        ///     Reference to the top items container
        /// </summary>
        public GameObject ItemsContainer;

        /// <summary>
        ///     Reference to the item view prefab
        /// </summary>
        public ItemView ItemViewPrefab;

        /// <summary>
        ///     Reference to the left arrow (cycles through the collection backward)
        /// </summary>
        public Button LeftArrow;

        /// <summary>
        ///     Reference to the right arrow (cycles through the collection forward)
        /// </summary>
        public Button RightArrow;

        /// <summary>
        ///     Event triggered if the selection has changed
        /// </summary>
        public Action<string> SelectionChanged;

        /// <summary>
        ///     Reference to the current selection
        /// </summary>
        private ItemView _currentSelection;

        /// <summary>
        ///     Initializes an empty collection of items
        /// </summary>
        private void Start()
        {
            ItemsCollection = new List<ItemView>();
        }

        /// <summary>
        ///     Initializes the micro inventory with a new selection and a selected element.
        /// </summary>
        /// <param name="choices">The new selection</param>
        /// <param name="selectedItemId">The selected item</param>
        public void InitItems(List<Item> choices, string selectedItemId)
        {
            // Delete current items
            foreach (Transform child in ItemsContainer.transform)
            {
                Destroy(child.gameObject);
            }

            ItemsCollection.Clear();

            if (choices != null)
            {
                foreach (Item i in choices)
                {
                    ItemView itemView = Instantiate(ItemViewPrefab, ItemsContainer.transform);
                    itemView.Init(i);
                    ItemsCollection.Add(itemView);

                    itemView.gameObject.SetActive(i.itemId == selectedItemId);
                    if (i.itemId == selectedItemId)
                    {
                        _currentSelection = itemView;
                    }
                }
            }

            LeftArrow.interactable = ItemsCollection.Count > 0;
            RightArrow.interactable = ItemsCollection.Count > 0;
        }

        /// <summary>
        ///     Triggered when the left button is touched
        /// </summary>
        public void OnShowPrevious()
        {
            if (ItemsCollection.Count == 0)
            {
                return;
            }

            if (_currentSelection == null)
            {
                SetItem(ItemsCollection[0].Id);
                return;
            }

            // Get index of current selection (if any)
            int idx = ItemsCollection.IndexOf(_currentSelection);
            if (idx >= 0)
            {
                idx--;
                if (idx < 0)
                {
                    idx = ItemsCollection.Count - 1;
                }

                SetItem(ItemsCollection[idx].Id);
            }
        }

        /// <summary>
        ///     Triggered when the next button is touched
        /// </summary>
        public void OnShowNext()
        {
            if (ItemsCollection.Count == 0)
            {
                return;
            }

            if (_currentSelection == null)
            {
                SetItem(ItemsCollection[0].Id);
                return;
            }

            // Get index of current selection (if any)
            int idx = ItemsCollection.IndexOf(_currentSelection);
            if (idx >= 0)
            {
                idx++;
                if (idx >= ItemsCollection.Count)
                {
                    idx = 0;
                }

                SetItem(ItemsCollection[idx].Id);
            }
        }

        /// <summary>
        ///     Sets the active item to the item identified by the provided id.
        /// </summary>
        /// <param name="selectedItemId"></param>
        private void SetItem(string selectedItemId)
        {
            if (_currentSelection != null)
            {
                _currentSelection.gameObject.SetActive(false);
            }

            ItemView selection = ItemsCollection.Find(s => s.Id == selectedItemId);
            if (selection != null)
            {
                _currentSelection = selection;
                _currentSelection.gameObject.SetActive(true);

                // Send new selected id
                SelectionChanged?.Invoke(selectedItemId);
            }
        }
    }
}
