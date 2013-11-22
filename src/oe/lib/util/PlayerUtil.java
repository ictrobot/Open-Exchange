package oe.lib.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerUtil {
  
  @SuppressWarnings("unused")
  public static void wipeInv(EntityPlayer player) {
    for (ItemStack stack : player.inventory.mainInventory) {
      stack = null;
    }
    for (ItemStack stack : player.inventory.armorInventory) {
      stack = null;
    }
  }
}
