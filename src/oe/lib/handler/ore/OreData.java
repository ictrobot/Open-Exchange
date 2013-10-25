package oe.lib.handler.ore;

import net.minecraft.item.ItemStack;

public class OreData {
  public String ore;
  public ItemStack[] itemstacks;
  
  public OreData() {
    this.itemstacks = new ItemStack[0];
    this.ore = "";
  }
}
