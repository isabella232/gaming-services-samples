/**
 * Models a game item. Items can be anything, from a character type to a spawn location
 * to a reward item.
 */
public class Item {
    public string id { get; set; }
    public int quantity { get; set; }

    public Item() {}

    public Item(string ObjectType, int Quantity) {
        this.id = ObjectType;
        this.quantity = Quantity;
    }
    public override string ToString() {
        return "{Type: " + id + " Quantity: " + quantity + "}";
    }
}
