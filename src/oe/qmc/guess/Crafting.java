package oe.qmc.guess;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import oe.Log;
import oe.api.OEGuesser;

public class Crafting extends OEGuesser {
  
  private static GuessData[] crafting = new GuessData[0];
  
  public static void init() {
    Log.debug("Loading Crafting Guesser");
    for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
      if (recipeObject instanceof IRecipe) {
        IRecipe recipe = (IRecipe) recipeObject;
        ItemStack output = recipe.getRecipeOutput();
        if (output != null) {
          ItemStack[] input = getCraftingInputs(recipe);
          if (input != null) {
            increaseCrafting();
            crafting[crafting.length - 1] = new GuessData(output, input);
          }
        }
      }
    }
  }
  
  public static double check(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    GuessData[] data = new GuessData[0];
    for (GuessData gd : crafting) {
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
          for (ItemStack stack : gd.input) {
            if (stack != null) {
              double v = Guess.check(stack);
              if (v == -1) {
                value = v;
                break;
              } else {
                value = value + v;
              }
            }
          }
          if (value > 0) {
            value = value / gd.output.stackSize;
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
    for (GuessData gd : crafting) {
      if (gd.output.itemID == itemstack.itemID) {
        int[] tmp = new int[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd.output.getItemDamage();
      }
    }
    return data;
  }
  
  @SuppressWarnings("rawtypes")
  private static ItemStack[] getCraftingInputs(IRecipe recipe) {
    ItemStack[] inputs = new ItemStack[9];
    if (recipe instanceof ShapedRecipes) {
      ShapedRecipes shaped = (ShapedRecipes) recipe;
      for (int i = 0; i < shaped.recipeItems.length; i++) {
        if (shaped.recipeItems[i] instanceof ItemStack) {
          ItemStack stack = shaped.recipeItems[i].copy();
          stack.stackSize = 1;
          inputs[i] = stack;
        }
      }
    } else if (recipe instanceof ShapelessRecipes) {
      ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
      int i = 0;
      for (Object object : shapeless.recipeItems) {
        if (object instanceof ItemStack) {
          ItemStack stack = ((ItemStack) object).copy();
          if (stack.stackSize > 1) {
            stack.stackSize = 1;
          }
          inputs[i] = stack;
          i++;
        }
      }
    } else if (recipe instanceof ShapedOreRecipe || recipe instanceof ShapelessOreRecipe) {
      Object[] input;
      if (recipe instanceof ShapedOreRecipe) {
        ShapedOreRecipe ore = (ShapedOreRecipe) recipe;
        input = ore.getInput();
      } else {
        ShapelessOreRecipe ore = (ShapelessOreRecipe) recipe;
        ArrayList al = ore.getInput();
        input = al.toArray();
      }
      for (int i = 0; i < input.length; i++) {
        if (input[i] != null) {
          if (input[i] instanceof ArrayList) {
            ArrayList al = (ArrayList) input[i];
            Object[] obj = al.toArray();
            if (obj.length != 0) {
              if (obj[0] != null) {
                if (obj[0] instanceof ItemStack) {
                  ItemStack stack = (ItemStack) obj[0];
                  inputs[i] = stack;
                }
              }
            }
          } else if (input[i] instanceof ItemStack) {
            ItemStack stack = (ItemStack) input[i];
            inputs[i] = stack;
          }
        }
      }
    }
    return inputs;
  }
  
  private static void increaseCrafting() {
    GuessData[] tmp = new GuessData[crafting.length + 1];
    System.arraycopy(crafting, 0, tmp, 0, crafting.length);
    crafting = tmp;
  }
  
}
