package com.google.maps.gaming.zoinkies;

/**
 * Specific exception when there aren't enough resources in the player's inventory
 * to unlock some spawned location.
 */
public class NotEnoughResourcesToUnlockException extends RuntimeException {
    public NotEnoughResourcesToUnlockException(String errorMessage) {
      super(errorMessage);
    }
}
