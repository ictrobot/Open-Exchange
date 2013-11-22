package oe.qmc.guess;

import java.lang.reflect.Field;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import oe.OpenExchange;
import oe.api.OEGuesser;
import oe.lib.Debug;
import oe.lib.Log;
import oe.lib.misc.FakeContainer;
import oe.lib.util.ItemStackUtil;
import oe.lib.util.PlayerUtil;
import cpw.mods.fml.common.registry.GameRegistry;

public class Crafting extends OEGuesser {
  public static class Data {
    ItemStack output;
    ItemStack[] input;
    ItemStack[] returned;
    
    public Data(ItemStack Output, ItemStack[] Inputs, ItemStack[] Returned) {
      this.output = Output;
      this.input = Inputs;
      this.returned = Returned;
    }
  }
  
  private static Data[][] crafting = new Data[32000][0];
  
  public static void init() {
    Log.debug("Loading Crafting Guesser");
    int recipes = 0;
    for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
      if (recipeObject instanceof IRecipe) {
        IRecipe recipe = (IRecipe) recipeObject;
        ItemStack output = recipe.getRecipeOutput();
        if (output != null) {
          ItemStack[] input = getCraftingInputs(recipe);
          if (input != null) {
            int id = output.itemID;
            increaseCrafting(id);
            ItemStack[] returned = afterCrafting(recipe, input, output);
            crafting[id][crafting[id].length - 1] = new Data(output, input, returned);
            recipes++;
          }
        }
      }
    }
    Log.debug("Found " + recipes + " Crafting Recipes");
  }
  
  public static Guess.Data check(ItemStack itemstack) {
    if (itemstack == null) {
      return null;
    }
    int id = itemstack.itemID;
    Data[] data = new Data[0];
    for (Data gd : crafting[id]) {
      if (itemstack.getItemDamage() == gd.output.getItemDamage()) {
        Data[] tmp = new Data[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd;
      }
    }
    if (data.length > 0) {
      for (Data gd : data) {
        double value = 0;
        for (int i = 0; i < gd.input.length; i++) {
          ItemStack stack = gd.input[i];
          if (stack != null) {
            double v = checkQMC(stack);
            if (v == -1) {
              return null;
            } else {
              value = value + v;
            }
          }
        }
        for (int i = 0; i < gd.returned.length; i++) {
          ItemStack stack = gd.returned[i];
          if (stack != null) {
            double v = checkQMC(stack);
            if (v == -1) {
              return null;
            } else {
              value = value - v;
            }
          }
        }
        if (value > 0) {
          value = value / gd.output.stackSize;
          Guess.Data toReturn = new Guess.Data(gd.input, value, gd.output.stackSize);
          return toReturn;
        }
      }
    }
    return null;
  }
  
  private static double checkQMC(ItemStack stack) {
    if (stack == null) {
      return -1;
    }
    double v = Guess.check(stack);
    if (v == -1) {
      if (stack.getItemDamage() == 32768 || stack.getItemDamage() == 32767) {
        ItemStack tmp = stack.copy();
        tmp.setItemDamage(0);
        return checkQMC(tmp);
      }
    }
    return v;
  }
  
  public static int[] meta(int ID) {
    ItemStack itemstack = new ItemStack(ID, 0, 0);
    int[] data = new int[0];
    for (Data gd : crafting[ID]) {
      if (gd.output.itemID == itemstack.itemID) {
        int[] tmp = new int[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd.output.getItemDamage();
      }
    }
    return data;
  }
  
  private static ItemStack[] getCraftingInputs(IRecipe recipe) {
    int nullNum = 0;
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
    } else {
      Object o = recipe;
      for (Field field : o.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        Object value = null;
        try {
          value = field.get(o);
        } catch (Exception e) {
          Debug.handleException(e);
        }
        
        if (value != null) {
          if (value.getClass().isArray()) {
            Object[] os = (Object[]) value;
            for (int i = 0; i < Math.min(os.length, 9); i++) {
              Object r = os[i];
              if (r != null) {
                ItemStack fromObject = ItemStackUtil.getItemStackFromObject(r);
                if (fromObject != null) {
                  inputs[i] = fromObject;
                } else {
                  nullNum = 1000;
                }
              }
            }
            break;
          }
        }
      }
    }
    for (ItemStack stack : inputs) {
      if (stack == null) {
        nullNum++;
      } else if (stack.getItemDamage() == 32767 || stack.getItemDamage() == 32768) {
        stack.setItemDamage(0);
      }
    }
    if (nullNum >= inputs.length) {
      Log.debug("Error while reading crafting recipes inputs for " + recipe.getRecipeOutput().toString() + " (ID: " + recipe.getRecipeOutput().itemID + ")");
      Log.debug("IRecipe Type: " + recipe.getClass());
      Debug.printObject(recipe);
      return null; // Failed to read Recipe
    }
    return inputs;
  }
  
  private static void increaseCrafting(int id) {
    Data[] tmp = new Data[crafting[id].length + 1];
    System.arraycopy(crafting[id], 0, tmp, 0, crafting[id].length);
    crafting[id] = tmp;
  }
  
  private static ItemStack[] afterCrafting(IRecipe recipe, ItemStack[] inputs, ItemStack output) {
    ItemStack[] toReturn = new ItemStack[0];
    try {
      FakeContainer fake = new FakeContainer();
      InventoryCrafting ic = new InventoryCrafting(fake, 3, 3);
      for (int i = 0; i < inputs.length; i++) {
        if (inputs[i] != null) {
          ic.setInventorySlotContents(i, inputs[i]);
        }
      }
      PlayerUtil.wipeInv(OpenExchange.fakePlayer);
      GameRegistry.onItemCrafted(OpenExchange.fakePlayer, output, ic);
      for (int i = 0; i < inputs.length; i++) {
        if (inputs[i] != null) {
          if (ic.getStackInSlot(i).getItem().hasContainerItem() && ic.getStackInSlot(i).getItem().getContainerItem() != null) {
            toReturn = ItemStackUtil.increaseAdd(toReturn, new ItemStack(ic.getStackInSlot(i).getItem().getContainerItem()));
          }
          if (inputs[i] != ic.getStackInSlot(i)) {
            toReturn = ItemStackUtil.increaseAdd(toReturn, ic.getStackInSlot(i));
          }
        }
      }
      for (ItemStack itemstack : OpenExchange.fakePlayer.inventory.mainInventory) {
        if (itemstack != null) {
          toReturn = ItemStackUtil.increaseAdd(toReturn, itemstack);
        }
      }
    } catch (Exception e) {
      Log.debug("After Crafting Failed for " + output.toString());
      Debug.printObject(recipe);
      Debug.handleException(e);
    }
    return toReturn;
  }
}
