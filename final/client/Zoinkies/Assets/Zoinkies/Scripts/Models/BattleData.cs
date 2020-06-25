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

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     Model that keeps track of battle instructions at start.
    ///     These attributes are serialized/deserialized into json data structures.
    /// </summary>
    public class BattleData
    {
        /// <summary>
        /// Unique identifier for the location where the battle takes place.
        /// </summary>
        public string id { get; set; }
        /// <summary>
        /// Opponent type
        /// </summary>
        public string opponentTypeId { get; set; }
        /// <summary>
        /// Indicates who starts first: 1-Player 0-Opponent
        /// </summary>
        public bool playerStarts { get; set; }
        /// <summary>
        /// Provides information about the attack score bonus for the opponent.
        /// </summary>
        public int maxAttackScoreBonus { get; set; }
        /// <summary>
        /// Provides information about the defense score bonus for the opponent.
        /// </summary>
        public int maxDefenseScoreBonus { get; set; }
        /// <summary>
        /// Indicates the energy level of the opponent.
        /// </summary>
        public int energyLevel { get; set; }
        /// <summary>
        /// Indicates the weapon cooldown of the opponent.
        /// </summary>
        public string cooldown { get; set; }

        public override string ToString()
        {
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
