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
    public class MicroInventoryController : MonoBehaviour
    {
        private ItemGO CurrentSelection;

        public ItemGO ItemGOPrefab;
        public List<ItemGO> ItemGOs;
        public GameObject ItemsContainer;
        public Button LeftArrow;
        public Button RightArrow;

        public Action<string> SelectionChanged;

        // Start is called before the first frame update
        private void Start()
        {
            ItemGOs = new List<ItemGO>();
        }

        public void InitItems(List<Item> selection, string id)
        {
            // Delete current items
            foreach (Transform child in ItemsContainer.transform)
            {
                Destroy(child.gameObject);
            }

            ItemGOs.Clear();

            if (selection != null)
            {
                foreach (Item i in selection)
                {
                    ItemGO itemGO = Instantiate(ItemGOPrefab, ItemsContainer.transform);
                    itemGO.Init(i);
                    ItemGOs.Add(itemGO);

                    itemGO.gameObject.SetActive(i.id == id);
                    if (i.id == id)
                    {
                        CurrentSelection = itemGO;
                    }
                }
            }

            LeftArrow.interactable = ItemGOs.Count > 0;
            RightArrow.interactable = ItemGOs.Count > 0;
        }

        public void SetItem(string id)
        {
            if (CurrentSelection != null)
            {
                CurrentSelection.gameObject.SetActive(false);
            }

            ItemGO selection = ItemGOs.Find(s => s.Id == id);
            if (selection != null)
            {
                CurrentSelection = selection;
                CurrentSelection.gameObject.SetActive(true);

                // Send new selected id
                SelectionChanged?.Invoke(id);
            }
        }

        public void OnShowPrevious()
        {
            Debug.Log("Prev");
            if (ItemGOs.Count == 0)
            {
                return;
            }

            if (CurrentSelection == null)
            {
                SetItem(ItemGOs[0].Id);
                return;
            }

            // Get index of current selection (if any)
            int idx = ItemGOs.IndexOf(CurrentSelection);
            if (idx >= 0)
            {
                idx--;
                if (idx < 0)
                {
                    idx = ItemGOs.Count - 1;
                }

                SetItem(ItemGOs[idx].Id);
            }
        }

        public void OnShowNext()
        {
            Debug.Log("Next");
            if (ItemGOs.Count == 0)
            {
                return;
            }

            if (CurrentSelection == null)
            {
                SetItem(ItemGOs[0].Id);
                return;
            }

            // Get index of current selection (if any)
            int idx = ItemGOs.IndexOf(CurrentSelection);
            if (idx >= 0)
            {
                idx++;
                if (idx >= ItemGOs.Count)
                {
                    idx = 0;
                }

                SetItem(ItemGOs[idx].Id);
            }
        }
    }
}
