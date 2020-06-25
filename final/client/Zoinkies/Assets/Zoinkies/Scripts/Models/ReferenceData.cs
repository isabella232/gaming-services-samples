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

using System.Collections.Generic;
using System.Text;

namespace Google.Maps.Demos.Zoinkies
{
    /// <summary>
    ///     Keeps track of reference data for the game.
    ///     Types of reference data:
    ///     - Location Objects
    ///     - Collectibles
    ///     - Weapons
    ///     - Armors
    ///     - Characters
    /// </summary>
    public class ReferenceData
    {
        public ReferenceData()
        {
            references = new List<ReferenceItem>();
        }

        /// <summary>
        ///     List of all references used in the game.
        /// </summary>
        public List<ReferenceItem> references { get; set; }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            foreach (ReferenceItem ri in references)
            {
                sb.Append(ri);
                sb.Append("\n");
            }

            return sb.ToString();
        }
    }
}
