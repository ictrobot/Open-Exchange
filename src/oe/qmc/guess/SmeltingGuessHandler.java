package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import oe.api.GuessHandler.ActiveGuessHandler;
import oe.core.Log;
import oe.core.util.data.Pair;

public class SmeltingGuessHandler extends ActiveGuessHandler {
  
  // Output, Input
  private HashMap<Integer, List<Pair<ItemStack, ItemStack>>> recipes = new HashMap<Integer, List<Pair<ItemStack, ItemStack>>>();
  private List<ItemStack> toGuess = new ArrayList<ItemStack>();
  
  @Override
  @SuppressWarnings("unchecked")
  public void init() {
    Map<Integer, ItemStack> normal = FurnaceRecipes.smelting().getSmeltingList();
    for (Integer i : normal.keySet()) {
      ItemStack input = new ItemStack(i, 1, 0);
      ItemStack output = normal.get(i);
      if (input != null & output != null) {
        add(output, input);
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
        add(output, input);
      }
    }
    Log.debug("Found " + recipes.size() + " Smelting Recipes");
  }
  
  @Override
  public List<ItemStack> getItemStacks() {
    return toGuess;
  }
  
  public void add(ItemStack output, ItemStack input) {
    List<Pair<ItemStack, ItemStack>> list;
    if (recipes.get(output.itemID) != null) {
      list = recipes.get(output.itemID);
      recipes.remove(output.itemID);
    } else {
      list = new ArrayList<Pair<ItemStack, ItemStack>>();
    }
    list.add(new Pair<ItemStack, ItemStack>(output, input));
    toGuess.add(output);
    recipes.put(output.itemID, list);
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null || recipes.get(itemstack.itemID) == null) {
      return -1;
    }
    for (Pair<ItemStack, ItemStack> pair : recipes.get(itemstack.itemID)) {
      if (pair.left.getItemDamage() == itemstack.getItemDamage()) {
        double value = Guess.check(pair.right);
        if (value > 0) {
          return value;
        }
      }
    }
    return -1;
  }
}
