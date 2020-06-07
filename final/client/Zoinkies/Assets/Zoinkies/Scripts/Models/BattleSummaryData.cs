namespace Google.Maps.Demos.Zoinkies {
  /// <summary>
  /// Model that provides information on the battle outcome.
  /// </summary>
  public class BattleSummaryData {
    public bool winner{ get; set; }
    public RewardsData rewards{ get; set; }
    public bool wonTheGame{ get; set; }
    public override string ToString() {
      return "{winner: " + winner +
             " rewards: " + rewards +
             " wonTheGame: " + wonTheGame +
             "}";
    }
  }
}
