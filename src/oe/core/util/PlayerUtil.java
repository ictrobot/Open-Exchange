package oe.core.util;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil {
  
  public static void wipeInv(EntityPlayer player) {
    for (int i = 0; i < player.inventory.mainInventory.length; i++) {
      if (player.inventory.mainInventory[i] != null) {
        player.inventory.mainInventory[i] = null;
      }
    }
    
    for (int i = 0; i < player.inventory.armorInventory.length; i++) {
      if (player.inventory.armorInventory[i] != null) {
        player.inventory.armorInventory[i] = null;
      }
    }
  }
}
