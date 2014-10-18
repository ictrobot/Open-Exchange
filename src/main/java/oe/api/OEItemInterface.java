package oe.api;

import net.minecraft.item.ItemStack;
import oe.api.lib.OEType;

public interface OEItemInterface {
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

  /**
   * Returns max QMC Storable
   */
  public double getMaxQMC(ItemStack itemstack);

  /**
   * Return Tier (Devices are only meant to interact with devices at the same tier or less)
   */
  public int getTier(ItemStack itemstack);

  /**
   * Returns device OEType
   */
  public OEType getType(ItemStack itemstack);
}
