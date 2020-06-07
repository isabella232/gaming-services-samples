using System.Collections.Generic;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Models a rewards list of items.
  /// </summary>
  public class RewardsData {
    /// <summary>
    /// List of items
    /// </summary>
    public List<Item> items{ get; set; }

    public RewardsData() {
      items = new List<Item>();

    }
  }
}
