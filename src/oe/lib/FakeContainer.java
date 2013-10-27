package oe.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

public class FakeContainer extends Container {
  
  public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
  
  @Override
  public boolean canInteractWith(EntityPlayer entityplayer) {
    return false;
  }
  
}
