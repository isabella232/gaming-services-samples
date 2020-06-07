using System.Text;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Models a single reference item object.
  /// </summary>
  public class ReferenceItem {
    /// <summary>
    /// Unique identifier for this object
    /// </summary>
    public string id { get; set; }
    /// <summary>
    /// Friendly name for the UI
    /// </summary>
    public string name { get; set; }
    /// <summary>
    /// Type of the object
    /// </summary>
    public string type { get; set; }
    /// <summary>
    /// Friendly description for the UI
    /// </summary>
    public string description { get; set; }
    /// <summary>
    /// Base Attack score (if any)
    /// </summary>
    public int attackScore { get; set; }
    /// <summary>
    /// Base Defense score (if any)
    /// </summary>
    public int defenseScore { get; set; }
    /// <summary>
    /// Base cooldown in between two battle turns (if any)
    /// </summary>
    public string cooldown { get; set; }
    /// <summary>
    /// Time to respawn after use/battle (if any)
    /// </summary>
    public string respawnDuration { get; set; }
    /// <summary>
    /// Name of the prefab to instantiate in the editor.
    /// </summary>
    public string prefab { get; set; }

    public override string ToString() {

      StringBuilder sb = new StringBuilder();
      sb.Append("Id: " + id);
      sb.Append("Name: " + name);
      sb.Append("Type: " + type);
      sb.Append("Description: " + description);
      sb.Append("AttackScore: " + attackScore);
      sb.Append("DefenseScore: " + defenseScore);
      sb.Append("Cooldown: " + cooldown);
      sb.Append("RespawnDuration: " + respawnDuration);
      sb.Append("Prefab: " + prefab);
      return sb.ToString();
    }
  }
}
