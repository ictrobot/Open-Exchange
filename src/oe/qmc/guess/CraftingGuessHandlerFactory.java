package oe.qmc.guess;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import oe.api.GuessHandler;
import oe.api.GuessHandlerFactory;

public class CraftingGuessHandlerFactory extends GuessHandlerFactory {
  
  private List<GuessHandler> handlers = new ArrayList<GuessHandler>();
  
  @Override
  public void init() {
    for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
      if (recipeObject instanceof IRecipe) {
        handlers.add(new CraftingGuessHandler((IRecipe) recipeObject));
      }
    }
  }
  
  public List<GuessHandler> handlers() {
    return handlers;
  }
}
