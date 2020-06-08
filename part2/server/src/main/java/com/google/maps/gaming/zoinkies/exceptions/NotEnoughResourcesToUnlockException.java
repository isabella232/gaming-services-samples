package com.google.maps.gaming.zoinkies.exceptions;

/**
 * Specific exception used when there aren't enough resources in the player's inventory
 * to unlock location.
 */
public class NotEnoughResourcesToUnlockException extends RuntimeException {
    public NotEnoughResourcesToUnlockException(String errorMessage) {
      super(errorMessage);
    }
}
