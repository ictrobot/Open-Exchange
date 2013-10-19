package oe.api;

import oe.api.lib.OEType;

public interface OEInterface {
  /**
   * Returns QMC stored
   */
  public double getQMC();
  
  /**
   * Sets QMC stored
   */
  public void setQMC(double qmc);
  
  /**
   * Increases stored QMC by vlue
   */
  public void increaseQMC(double value);
  
  /**
   * Decreases stored QMC by vlue
   */
  public void decreaseQMC(double value);
  
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
