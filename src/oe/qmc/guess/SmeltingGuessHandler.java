package oe.qmc.guess;

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
    Log.debug("Loading Smelting Guesser");
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
  public Guess.Data check(ItemStack itemstack) {
    if (itemstack == null) {
      return null;
    }
    for (ItemStack output : recipes.keySet()) {
      if (ItemStackUtil.equalsIgnoreNBT(output, itemstack)) {
        double value = Guess.check(recipes.get(output));
        if (value > 0) {
          Guess.Data toReturn = new Guess.Data(recipes.get(output), value, 1);
          return toReturn;
        }
      }
    }
    return null;
  }
  
  @Override
  public int[] meta(int ID) {
    int[] meta = new int[0];
    for (ItemStack output : recipes.keySet()) {
      if (output.itemID == ID) {
        int[] tmp = new int[meta.length + 1];
        System.arraycopy(meta, 0, tmp, 0, meta.length);
        meta = tmp;
        meta[meta.length - 1] = output.getItemDamage();
      }
    }
    return meta;
  }
}
