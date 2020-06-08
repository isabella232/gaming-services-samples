using System;
using System.Collections.Generic;
using System.Linq;

namespace Google.Maps.Demos.Zoinkies {

  /// <summary>
  /// This class provides access reference data through helper functions.
  ///
  /// </summary>
  public class ReferenceService {

    // Singleton
    private static ReferenceService instance;

    public static ReferenceService GetInstance() {
      if (instance == null) {
        instance = new ReferenceService();
      }

      return instance;
    }
    private ReferenceService() {
    }

    /// <summary>
    /// Reference to the actual reference data
    /// </summary>
    internal ReferenceData data;

    /// <summary>
    /// Initializes Player Data.
    /// </summary>
    /// <param name="data"></param>
    /// <exception cref="Exception"></exception>
    public void Init(ReferenceData data) {
      if (data == null) {
        throw new System.Exception("Invalid reference data! (Data is null)");
      }

      this.data = data;
    }

    /// <summary>
    /// Returns a reference item identified by the given id.
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public ReferenceItem GetItem(String id) {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Find(s => s.id == id);
    }

    /// <summary>
    /// Returns all weapons references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<ReferenceItem> GetWeapons() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.WEAPONS);
    }

    /// <summary>
    /// Returns all body armors references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<ReferenceItem> GetBodyArmors() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.BODYARMORS);
    }

    /// <summary>
    /// Returns all helmets references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<ReferenceItem> GetHelmets() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.HELMETS);
    }

    /// <summary>
    /// Returns all shields references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<ReferenceItem> GetShields() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.SHIELDS);
    }

    /// <summary>
    /// Returns all NPCs references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    /*
    public IEnumerable<ReferenceItem> GetNPCs() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.NPCS);
    }
    */

    /// <summary>
    /// Returns all Avatar types references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    public IEnumerable<ReferenceItem> GetAvatars() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.AVATARS);
    }

    /// <summary>
    /// Returns all towers references.
    /// </summary>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    /*
    public IEnumerable<ReferenceItem> GetTowers() {
      if (data == null) {
        throw new System.Exception("Reference data not initialized!");
      }

      return data.references.Where(s => s.type == GameConstants.TOWER);
    }
    */
  }
}
