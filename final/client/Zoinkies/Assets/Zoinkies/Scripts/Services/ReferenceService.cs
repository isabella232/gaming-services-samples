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

using System;
using System.Collections.Generic;
using System.Linq;

namespace Google.Maps.Demos.Zoinkies
{
  /// <summary>
  ///     This class provides access reference data through helper functions.
  /// </summary>
  public class ReferenceService
    {
        // Singleton Pattern Implementation
        private static ReferenceService instance;

        /// <summary>
        ///     Reference to the actual reference data
        /// </summary>
        internal ReferenceData data;

        private ReferenceService()
        {
        }
        public static ReferenceService GetInstance()
        {
            if (instance == null)
            {
                instance = new ReferenceService();
            }

            return instance;
        }

        /// <summary>
        ///     Initializes Player Data.
        /// </summary>
        /// <param name="data"></param>
        /// <exception cref="Exception"></exception>
        public void Init(ReferenceData data)
        {
            if (data == null)
            {
                throw new System.Exception("Invalid reference data! (Data is null)");
            }

            this.data = data;
        }

        /// <summary>
        ///     Returns a reference item identified by the given id.
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public ReferenceItem GetItem(string id)
        {
            if (data == null)
            {
                throw new System.Exception("Reference data not initialized!");
            }

            return data.references.Find(s => s.id == id);
        }

        /// <summary>
        ///     Returns all weapons references.
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public IEnumerable<ReferenceItem> GetWeapons()
        {
            if (data == null)
            {
                throw new System.Exception("Reference data not initialized!");
            }

            return data.references.Where(s => s.type == GameConstants.WEAPONS);
        }

        /// <summary>
        ///     Returns all body armors references.
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public IEnumerable<ReferenceItem> GetBodyArmors()
        {
            if (data == null)
            {
                throw new System.Exception("Reference data not initialized!");
            }

            return data.references.Where(s => s.type == GameConstants.BODYARMORS);
        }

        /// <summary>
        ///     Returns all helmets references.
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public IEnumerable<ReferenceItem> GetHelmets()
        {
            if (data == null)
            {
                throw new System.Exception("Reference data not initialized!");
            }

            return data.references.Where(s => s.type == GameConstants.HELMETS);
        }

        /// <summary>
        ///     Returns all shields references.
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public IEnumerable<ReferenceItem> GetShields()
        {
            if (data == null)
            {
                throw new System.Exception("Reference data not initialized!");
            }

            return data.references.Where(s => s.type == GameConstants.SHIELDS);
        }

        /// <summary>
        ///     Returns all Avatar types references.
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        public IEnumerable<ReferenceItem> GetAvatars()
        {
            if (data == null)
            {
                throw new System.Exception("Reference data not initialized!");
            }

            return data.references.Where(s => s.type == GameConstants.AVATARS);
        }
    }
}
