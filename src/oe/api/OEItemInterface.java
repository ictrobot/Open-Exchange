package oe.api;

import net.minecraft.item.ItemStack;

public interface OEItemInterface extends OEInterface {
  /**
   * Returns QMC stored
   */
  public double getQMC(ItemStack itemstack);
  
  /**
   * Sets QMC stored
   */
  public void setQMC(double qmc, ItemStack itemstack);
  
  /**
   * Increases stored QMC by vlue
   */
  public void increaseQMC(double value, ItemStack itemstack);
  
  /**
   * Decreases stored QMC by vlue
   */
  public void decreaseQMC(double value, ItemStack itemstack);
}
