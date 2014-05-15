package oe.qmc.guess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import oe.api.qmc.guess.GuessHandler;
import oe.api.qmc.guess.GuessHandlerFactory;

public class SmeltingGuessHandlerFactory extends GuessHandlerFactory {
  
  private List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  @Override
  @SuppressWarnings("unchecked")
  public void init() {
    Map<Integer, ItemStack> normal = FurnaceRecipes.smelting().getSmeltingList();
    for (Integer i : normal.keySet()) {
      ItemStack input = new ItemStack(Item.getItemById(i), 1, 0);
      ItemStack output = normal.get(i);
      if (input != null & output != null) {
        handlers.add(new InputOutputGuessHandler(input, output, this.getClass()));
      }
    }
    /*
     * Map<List<Integer>, ItemStack> meta = FurnaceRecipes.smelting().getMetaSmeltingList();
     * for (List<Integer> idPair : meta.keySet()) {
     * int ID = idPair.get(0);
     * int Meta = 0;
     * if (idPair.size() == 2) {
     * Meta = idPair.get(1);
     * }
     * ItemStack input = new ItemStack(Item.getItemById(ID), 1, Meta);
     * ItemStack output = meta.get(idPair);
     * if (input != null & output != null) {
     * handlers.add(new InputOutputGuessHandler(input, output, this.getClass()));
     * }
     * }
     */
  }
  
  public List<GuessHandler> handlers() {
    return handlers;
  }
}
