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

using UnityEngine;
using UnityEngine.UI;

namespace Google.Maps.Demos.Zoinkies {

    public class ItemGO : MonoBehaviour {

        public Image ItemIcon;
        public Text ItemName;
        public Text ItemDescription;
        public Text ItemQuantity;

        public string Id {
            get { return item.id; }
        }

        private Item item;
        private ReferenceItem refItem;

        public void Init(Item item, bool showQuantity = false) {
            if (item != null) {
                this.item = item;

                // Get the associated reference data
                refItem = ReferenceService.GetInstance().GetItem(this.item.id);
                if (refItem != null) {
                    ItemName.text = refItem.name;
                    ItemDescription.text = refItem.description;
                    ItemIcon.sprite = Resources.Load<Sprite>("Icon" + refItem.prefab);
                }

                if (this.item.quantity >= 0)
                    this.ItemQuantity.text = "+" + this.item.quantity;
                else
                    this.ItemQuantity.text = this.item.quantity.ToString();

                this.ItemQuantity.gameObject.SetActive(showQuantity);
            }
            else {
                Debug.LogError("Invalid item data assigned to item prefab.");
            }

        }
    }
}
