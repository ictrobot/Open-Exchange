package oe.item;

import net.minecraft.item.ItemStack;

public interface ItemMode {
  
  public String getMode(ItemStack itemstack);
  
  public String switchMode(ItemStack itemstack);
}
