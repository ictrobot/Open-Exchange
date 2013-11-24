package oe.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oe.lib.util.Util;

public class ItemOE extends Item {
  
  public ItemOE(int id) {
    super(id);
    setCreativeTab(CreativeTabs.tabTools);
    setMaxStackSize(1);
  }
  
  @Override
  public String getItemDisplayName(ItemStack itemstack) {
    return Util.localize(getUnlocalizedName(itemstack));
  }
}
