package oe.qmc.guess;

import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import oe.api.GuessHandler;
import oe.core.Log;

public class SmeltingGuessHandler extends GuessHandler {
  public static class Data {
    ItemStack output;
    ItemStack input;
    
    public Data(ItemStack Output, ItemStack Input) {
      this.output = Output;
      this.input = Input;
    }
  }
  
  private Data[] smelting = new Data[0];
  
  @Override
  @SuppressWarnings("unchecked")
  public void init() {
    Log.debug("Loading Smelting Guesser");
    int recipes = 0;
    Map<Integer, ItemStack> normal = FurnaceRecipes.smelting().getSmeltingList();
    for (Integer i : normal.keySet()) {
      increaseSmelting();
      ItemStack input = new ItemStack(i, 1, 0);
      ItemStack output = normal.get(i);
      if (input != null & output != null) {
        smelting[smelting.length - 1] = new Data(output, input);
        recipes++;
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
      increaseSmelting();
      if (input != null & output != null) {
        smelting[smelting.length - 1] = new Data(output, input);
        recipes++;
      }
    }
    Log.debug("Found " + recipes + " Smelting Recipes");
  }
  
  @Override
  public Guess.Data check(ItemStack itemstack) {
    if (itemstack == null) {
      return null;
    }
    for (Data gd : smelting) {
      if (gd.output.itemID == itemstack.itemID && gd.output.getItemDamage() == itemstack.getItemDamage()) {
        double value = 0;
        ItemStack stack = gd.input;
        if (stack != null) {
          double v = Guess.check(stack);
          value = v;
        }
        if (value > 0) {
          Guess.Data toReturn = new Guess.Data(gd.input, value, 1);
          return toReturn;
        }
      }
    }
    return null;
  }
  
  @Override
  public int[] meta(int ID) {
    ItemStack itemstack = new ItemStack(ID, 0, 0);
    int[] data = new int[0];
    for (Data gd : smelting) {
      if (gd.output.itemID == itemstack.itemID) {
        int[] tmp = new int[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd.output.getItemDamage();
      }
    }
    return data;
  }
  
  private void increaseSmelting() {
    Data[] tmp = new Data[smelting.length + 1];
    System.arraycopy(smelting, 0, tmp, 0, smelting.length);
    smelting = tmp;
  }
}
