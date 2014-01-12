package oe.qmc.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import oe.api.GuessHandler;

public class CraftingGuessHandlerFactory extends GuessHandler {
  
  private HashMap<Class<?>, List<IRecipe>> recipes = new HashMap<Class<?>, List<IRecipe>>();
  
  @Override
  public void init() {
    for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
      if (recipeObject instanceof IRecipe) {
        IRecipe irecipe = (IRecipe) recipeObject;
        List<IRecipe> list;
        if (recipes.get(irecipe.getClass()) == null) {
          list = new ArrayList<IRecipe>();
        } else {
          list = recipes.get(irecipe.getClass());
        }
        list.add(irecipe);
        recipes.put(irecipe.getClass(), list);
      }
    }
    for (Class<?> c : recipes.keySet()) {
      CraftingGuessHandler cgh = new CraftingGuessHandler(c, recipes.get(c));
      cgh.init();
    }
    Iterator<GuessHandler> iterator = Guess.handlers.iterator();
    while (iterator.hasNext()) {
      GuessHandler g = iterator.next();
      if (g.getClass() == this.getClass()) {
        iterator.remove();
      }
    }
  }
}
