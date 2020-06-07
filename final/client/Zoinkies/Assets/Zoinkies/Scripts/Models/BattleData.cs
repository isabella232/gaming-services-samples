namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Model that keeps track of battle instructions at start.
  /// </summary>
  public class BattleData {
    public string placeId { get; set; }
    public string opponentTypeId { get; set; }
    public bool playerStarts { get; set; } // 1-Player 0-Opponent
    public int maxAttackScoreBonus{ get; set; }
    public int maxDefenseScoreBonus{ get; set; }
    public int energyLevel{ get; set; }
    public string cooldown{ get; set; }

    public override string ToString() {
      return "{Id: " + placeId +
             " OpponentTypeId: " + opponentTypeId +
             " PlayerStarts: " + playerStarts +
             " MaxAttackScoreBonus: " + maxAttackScoreBonus +
             " MaxDefenseScoreBonus: " + maxDefenseScoreBonus +
             " EnergyLevel: " + energyLevel +
             " Cooldown: " + cooldown +
             "}";
    }
  }
}
