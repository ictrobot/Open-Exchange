package oe.qmc.guess;

import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import oe.Log;
import oe.api.OEGuesser;

public class Smelting extends OEGuesser {
  private static GuessData[] smelting = new GuessData[0];
  
  @SuppressWarnings("unchecked")
  public static void init() {
    Log.debug("Loading Smelting Guesser");
    int recipes = 0;
    Map<Integer, ItemStack> list = FurnaceRecipes.smelting().getSmeltingList();
    for (Integer i : list.keySet()) {
      increaseSmelting();
      ItemStack input = new ItemStack(i, 1, 0);
      ItemStack output = list.get(i);
      if (input != null & output != null) {
        smelting[smelting.length - 1] = new GuessData(output, input);
        recipes++;
      }
    }
    Log.debug("Found " + recipes + " Smelting Recipes");
  }
  
  public static double check(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    GuessData[] data = new GuessData[0];
    for (GuessData gd : smelting) {
      if (gd.output.itemID == itemstack.itemID && gd.output.getItemDamage() == gd.output.getItemDamage()) {
        GuessData[] tmp = new GuessData[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd;
      }
    }
    if (data.length != 0) {
      for (GuessData gd : data) {
        if (gd.output.itemID == itemstack.itemID && gd.output.getItemDamage() == itemstack.getItemDamage()) {
          double value = 0;
          ItemStack stack = gd.input[0];
          if (stack != null) {
            double v = Guess.check(stack);
            value = v;
          }
          if (value > 0) {
            return value;
          }
        }
      }
    }
    return -1;
  }
  
  public static int[] meta(int ID) {
    ItemStack itemstack = new ItemStack(ID, 0, 0);
    int[] data = new int[0];
    for (GuessData gd : smelting) {
      if (gd.output.itemID == itemstack.itemID) {
        int[] tmp = new int[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd.output.getItemDamage();
      }
    }
    return data;
  }
  
  private static void increaseSmelting() {
    GuessData[] tmp = new GuessData[smelting.length + 1];
    System.arraycopy(smelting, 0, tmp, 0, smelting.length);
    smelting = tmp;
  }
}
