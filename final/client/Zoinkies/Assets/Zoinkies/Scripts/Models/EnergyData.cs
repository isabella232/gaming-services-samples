namespace Google.Maps.Demos.Zoinkies {
  /// <summary>
  /// Model that provides information about restored energy.
  /// </summary>
  public class EnergyData {
    public string placeId{ get; set; }
    public int amountRestored{ get; set; }
    public override string ToString() {
      return "{placeId: " + placeId +
             " amountRestored: " + amountRestored +
             "}";
    }
  }
}
