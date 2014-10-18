package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import oe.api.qmc.guess.GuessHandler;
import oe.api.qmc.guess.GuessHandlerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmeltingGuessHandlerFactory extends GuessHandlerFactory {

  private List<GuessHandler> handlers = new ArrayList<GuessHandler>();

  @Override
  @SuppressWarnings("unchecked")
  public void init() {
    Map<ItemStack, ItemStack> normal = FurnaceRecipes.smelting().getSmeltingList();
    for (ItemStack input : normal.keySet()) {
      ItemStack output = normal.get(input);
      if (input != null & output != null) {
        handlers.add(new InputOutputGuessHandler(input, output, this.getClass()));
      }
    }
  }

  public List<GuessHandler> handlers() {
    return handlers;
  }
}
