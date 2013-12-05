package oe.core.util;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil {
  
  public static void wipeInv(EntityPlayer player) {
    player.inventory.clearInventory(-1, -1);
  }
}
