package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import oe.api.GuessHandler;
import oe.core.Log;
import oe.core.util.ItemStackUtil;

public class SmeltingGuessHandler extends GuessHandler {
  
  // Output, Input
  private HashMap<ItemStack, ItemStack> recipes = new HashMap<ItemStack, ItemStack>();
  
  @Override
  @SuppressWarnings("unchecked")
  public void init() {
    Map<Integer, ItemStack> normal = FurnaceRecipes.smelting().getSmeltingList();
    for (Integer i : normal.keySet()) {
      ItemStack input = new ItemStack(i, 1, 0);
      ItemStack output = normal.get(i);
      if (input != null & output != null) {
        recipes.put(output, input);
      }
    }
    Map<List<Integer>, ItemStack> meta = FurnaceRecipes.smelting().getMetaSmeltingList();
    for (List<Integer> idPair : meta.keySet()) {
      int ID = idPair.get(0);
      int Meta = 0;
      if (idPair.size() == 2) {
        Meta = idPair.get(1);
      }
      ItemStack input = new ItemStack(ID, 1, Meta);
      ItemStack output = meta.get(idPair);
      if (input != null & output != null) {
        recipes.put(output, input);
      }
    }
    Log.debug("Found " + recipes.size() + " Smelting Recipes");
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    for (ItemStack output : recipes.keySet()) {
      if (ItemStackUtil.equalsIgnoreNBT(output, itemstack)) {
        double value = Guess.check(recipes.get(output));
        if (value > 0) {
          return value;
        }
      }
    }
    return -1;
  }
  
  @Override
  public List<Integer> meta(int ID) {
    List<Integer> meta = new ArrayList<Integer>();
    for (ItemStack output : recipes.keySet()) {
      if (output.itemID == ID) {
        meta.add(output.getItemDamage());
      }
    }
    return meta;
  }
}
