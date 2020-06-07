
using Unity.Collections;
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
