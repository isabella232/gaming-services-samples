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
    ///     Model that provides information on the battle outcome.
    /// </summary>
    public class BattleSummaryData
    {
        /// <summary>
        /// Indicates who the winner was: 0 -> NPC  1 -> Avatar
        /// </summary>
        public bool winner { get; set; }
        /// <summary>
        /// Provides information about rewards or penalties for winning or losing.
        /// </summary>
        public RewardsData rewards { get; set; }
        /// <summary>
        /// Indicates if the player has won the game.
        /// </summary>
        public bool wonTheGame { get; set; }

        public override string ToString()
        {
            return "{winner: " + winner +
                   " rewards: " + rewards +
                   " wonTheGame: " + wonTheGame +
                   "}";
        }
    }
}
