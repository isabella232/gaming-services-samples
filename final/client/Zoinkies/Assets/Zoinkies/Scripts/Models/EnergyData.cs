namespace Google.Maps.Demos.Zoinkies {
  /// <summary>
  /// Model that provides information about restored energy.
  /// </summary>
  public class EnergyData {
    public string id{ get; set; }
    public int amountRestored{ get; set; }
    public override string ToString() {
      return "{Id: " + id +
             " AmountRestored: " + amountRestored +
             "}";
    }
  }
}
