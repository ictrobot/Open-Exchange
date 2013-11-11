package oe.api;

public interface OEPipeInterface {
  /**
   * Returns max QMC that can pass through
   */
  public double getMaxQMC();
  
  /**
   * Increase amount that pass through pipe that tick by amount
   * 
   * @return amount stored after increasing
   */
  public double increasePassThrough(double amount);
  
  /**
   * GetMaxQMC - Pass Through
   */
  public double passThroughLeft();
}
