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
namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// Model that keeps track of battle instructions at start.
  /// </summary>
  public class BattleData {
    public string id { get; set; }
    public string opponentTypeId { get; set; }
    public bool playerStarts { get; set; } // 1-Player 0-Opponent
    public int maxAttackScoreBonus{ get; set; }
    public int maxDefenseScoreBonus{ get; set; }
    public int energyLevel{ get; set; }
    public string cooldown{ get; set; }

    public override string ToString() {
      return "{Id: " + id +
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
