package oe.api;

import net.minecraft.item.ItemStack;

public interface OEItemMode {
  
  public String getMode(ItemStack itemstack);
  
  public String switchMode(ItemStack itemstack);
}
