package oe.lib;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import oe.item.ItemIDs;
import oe.lib.helper.ConfigHelper;
import oe.qmc.QMC;
import oe.qmc.QMCData;

public class TransmutationRecipes {
  
  public static void init() {
    
    ConfigHelper.load();
    if (!ConfigHelper.other("item", "transmutationEnabled", true)) {
      return;
    }
    ConfigHelper.save();
    
    int recipes = 0;
    ItemStack stone = new ItemStack(ItemIDs.transmutation + 256, 1, 0);
    double[] values = new double[0];
    
    for (QMCData data : QMC.getDataBase()) {
      boolean contains = false;
      for (double i : values) {
        if (i == data.QMC) {
          contains = true;
        }
      }
      if (!contains) {
        double[] tmp = new double[values.length + 1];
        System.arraycopy(values, 0, tmp, 0, values.length);
        values = tmp;
        values[values.length - 1] = data.QMC;
      }
    }
    
    for (double i : values) {
      ItemStack[] stacks = QMC.getItemStacksFromQMC(i);
      if (stacks != null) {
        if (stacks.length >= 2) {
          for (int s = 0; s < stacks.length; s++) {
            ItemStack output = stacks[s];
            int in = s + 1;
            if (in == stacks.length) {
              in = 0;
            }
            ItemStack input = stacks[in];
            if (output != null && input != null) {
              GameRegistry.addShapelessRecipe(output, stone, input);
              recipes++;
            }
          }
        }
      }
    }
    
    Log.info("Registered " + recipes + " transmutation recipes");
  }
}
