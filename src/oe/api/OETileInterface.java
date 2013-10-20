package oe.api;

public interface OETileInterface extends OEInterface {
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
}
