package oe.lib.handler.ore;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class OreDictionaryHelper {
  
  private static OreData[] data = new OreData[0];
  
  @ForgeSubscribe
  public void handleOreRegisterEvent(OreRegisterEvent event) {
    int r = getReference(event.Name);
    if (r == -1) {
      OreData[] tmp = new OreData[data.length + 1];
      System.arraycopy(data, 0, tmp, 0, data.length);
      data = tmp;
      data[data.length - 1] = new OreData();
      data[data.length - 1].ore = event.Name;
      r = getReference(event.Name);
    }
    ItemStack[] tmp = new ItemStack[data[r].itemstacks.length + 1];
    System.arraycopy(data[r].itemstacks, 0, tmp, 0, data[r].itemstacks.length);
    data[r].itemstacks = tmp;
    data[r].itemstacks[data[r].itemstacks.length - 1] = event.Ore;
  }
  
  public static int getReference(String name) {
    for (int i = 0; i < data.length; i++) {
      if (data[i].ore == name) {
        return i;
      }
    }
    return -1;
  }
  
  public static ItemStack[] getItemStacks(String ore) {
    for (OreData od : data) {
      if (od.ore == ore) {
        return od.itemstacks;
      }
    }
    return null;
  }
  
  public static boolean exists(String name) {
    return getReference(name) != -1;
  }
}
