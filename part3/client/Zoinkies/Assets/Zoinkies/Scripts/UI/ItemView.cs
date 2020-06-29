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

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    /// This class represents the view for game items.
    /// </summary>
    public class ItemView : MonoBehaviour
    {
        /// <summary>
        /// A reference to the item description
        /// </summary>
        public Text ItemDescription;

        /// <summary>
        /// A reference to the item icon
        /// </summary>
        public Image ItemIcon;

        /// <summary>
        /// A reference to the item name
        /// </summary>
        public Text ItemName;

        /// <summary>
        /// A reference to the item quantity
        /// </summary>
        public Text ItemQuantity;

        /// <summary>
        /// A reference to the item id
        /// </summary>
        public string Id => _data.id;

        /// <summary>
        /// A reference to the properties of the item
        /// </summary>
        private ReferenceItem _referenceData;

        /// <summary>
        /// The actual quantities owned
        /// </summary>
        private Item _data;

        /// <summary>
        /// Initializes this view with an item data.
        /// </summary>
        /// <param name="item">The item data</param>
        /// <param name="showQuantity">A flag that indicates if we should show quantities</param>
        public void Init(Item item, bool showQuantity = false)
        {
            if (item != null)
            {
                _data = item;

                _referenceData = ReferenceService.GetInstance().GetItem(this._data.id);
                if (_referenceData != null)
                {
                    ItemName.text = _referenceData.name;
                    ItemDescription.text = _referenceData.description;
                    ItemIcon.sprite = Resources.Load<Sprite>("Icon" + _referenceData.prefab);
                }

                if (_data.quantity >= 0)
                {
                    ItemQuantity.text = "+" + _data.quantity;
                }
                else
                {
                    ItemQuantity.text = _data.quantity.ToString();
                }

                ItemQuantity.gameObject.SetActive(showQuantity);
            }
            else
            {
                Debug.LogError("Invalid item data assigned to item prefab.");
            }
        }
    }
}
