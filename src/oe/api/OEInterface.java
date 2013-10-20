package oe.api;

import oe.api.lib.OEType;

public interface OEInterface {
  /**
   * This just needs to exist
   */
  public void isOE();
  
  /**
   * Returns max QMC Storable
   */
  public int getMaxQMC();
  
  /**
   * Return Tier (Devices are only meant to interact with devices at the same tier or less)
   */
  public int getTier();
  
  /**
   * Returns device OEType
   */
  public OEType getType();
}
