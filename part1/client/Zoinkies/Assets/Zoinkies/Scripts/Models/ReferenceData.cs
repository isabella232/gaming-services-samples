using System.Collections.Generic;
using System.Text;

namespace Google.Maps.Demos.Zoinkies {

/// <summary>
/// Keeps track of reference data for the game.
///
/// Types of reference data:
/// - Location Objects
/// - Collectibles
/// - Weapons
/// - Armors
/// - Characters
/// </summary>
  public class ReferenceData {

    /// <summary>
    /// List of all references used in the game.
    /// </summary>
    public List<ReferenceItem> references { get; set; }

    public ReferenceData() {
      references = new List<ReferenceItem>();
    }

    public override string ToString() {
      var sb = new StringBuilder();
      foreach (ReferenceItem ri in references) {
        sb.Append(ri);
        sb.Append("\n");
      }

      return sb.ToString();
    }
  }
}
