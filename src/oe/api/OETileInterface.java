package oe.api;

import oe.api.lib.OEType;

public interface OETileInterface {
  /**
   * Returns QMC stored
   */
  public double getQMC();
  
  /**
   * Sets QMC stored
   */
  public void setQMC(double qmc);
  
  /**
   * Increases stored QMC by value
   */
  public void increaseQMC(double value);
  
  /**
   * Decreases stored QMC by value
   */
  public void decreaseQMC(double value);
  
  /**
   * Returns max QMC Storable
   */
  public double getMaxQMC();
  
  /**
   * Return Tier (Devices are only meant to interact with devices at the same tier or less)
   */
  public int getTier();
  
  /**
   * Returns device OEType
   */
  public OEType getType();
}
