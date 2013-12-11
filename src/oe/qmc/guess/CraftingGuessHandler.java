package oe.qmc.guess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import oe.OpenExchange;
import oe.api.GuessHandler;
import oe.core.Debug;
import oe.core.Log;
import oe.core.data.FakeContainer;
import oe.core.util.ItemStackUtil;
import oe.core.util.PlayerUtil;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingGuessHandler extends GuessHandler {
  public static class Data {
    ItemStack output;
    ItemStack[] input;
    List<ItemStack> returned;
    
    public Data(ItemStack Output, ItemStack[] Inputs, List<ItemStack> Returned) {
      this.output = Output;
      this.input = Inputs;
      this.returned = Returned;
    }
  }
  
  private Data[][] crafting = new Data[32000][0];
  
  @Override
  public void init() {
    int recipes = 0;
    for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
      try {
        if (recipeObject instanceof IRecipe) {
          IRecipe recipe = (IRecipe) recipeObject;
          ItemStack output = recipe.getRecipeOutput();
          ItemStack[] input = getInputs(recipe);
          if (output != null && input != null) {
            List<ItemStack> returned = getReturned(recipe, input, output);
            if (returned != null) {
              increaseData(output.itemID);
              crafting[output.itemID][crafting[output.itemID].length - 1] = new Data(output, input, returned);
              recipes++;
            }
          }
        }
      } catch (Exception e) {
        
      }
    }
    Log.debug("Found " + recipes + " Crafting Recipes");
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null) {
      return -1;
    }
    int id = itemstack.itemID;
    for (Data d : crafting[id]) {
      if (itemstack.getItemDamage() == d.output.getItemDamage()) {
        double value = 0;
        for (int i = 0; i < d.input.length; i++) {
          ItemStack stack = d.input[i];
          if (stack != null) {
            double v = checkQMC(stack);
            if (v == -1) {
              return -1;
            } else {
              value = value + v;
            }
          }
        }
        for (ItemStack stack : d.returned) {
          if (stack != null) {
            double v = checkQMC(stack);
            if (v == -1) {
              return -1;
            } else {
              value = value - v;
            }
          }
        }
        if (value > 0) {
          value = value / d.output.stackSize;
          return value;
        }
      }
    }
    return -1;
  }
  
  private double checkQMC(ItemStack stack) {
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
  
  @Override
  public List<Integer> meta(int ID) {
    List<Integer> meta = new ArrayList<Integer>();
    for (Data d : crafting[ID]) {
      meta.add(d.output.getItemDamage());
    }
    return meta;
  }
  
  private static ItemStack[] getInputs(IRecipe recipe) {
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
      Field[] f = o.getClass().getDeclaredFields();
      Class<?> c = o.getClass();
      int times = 0;
      while (f.length == 0 && c.getSuperclass() != null && !c.getSuperclass().equals(IRecipe.class)) {
        f = c.getSuperclass().getDeclaredFields();
        c = c.getSuperclass();
        times++;
        if (times == 50) {
          break;
        }
      }
      for (Field field : f) {
        field.setAccessible(true);
        Object value = null;
        try {
          value = field.get(o);
        } catch (Exception e) {
          Debug.handleException(e);
        }
        
        if (value != null) {
          if (value.getClass().isArray() || value instanceof ArrayList) {
            Object[] os = null;
            if (value instanceof ArrayList) {
              os = ((ArrayList<?>) value).toArray();
            } else if (value instanceof Object[]) {
              os = (Object[]) value;
            } else if (value instanceof ItemStack[]) {
              os = (ItemStack[]) value;
            }
            for (int i = 0; i < Math.min(os.length, 9); i++) {
              Object r = os[i];
              if (r != null) {
                ItemStack fromObject = ItemStackUtil.getItemStackFromObject(r);
                if (fromObject != null) {
                  inputs[i] = fromObject;
                } else {
                  Log.debug("Error while reading crafting recipes inputs for " + recipe.getRecipeOutput().getDisplayName() + " (ID:" + recipe.getRecipeOutput().itemID + ", Meta:" + recipe.getRecipeOutput().getItemDamage() + ")");
                  Log.debug("IRecipe Type: " + recipe.getClass());
                  Debug.printObject(recipe);
                  return null;
                }
              }
            }
            break;
          }
        }
      }
    }
    int nullNum = 0;
    for (ItemStack stack : inputs) {
      if (stack == null) {
        nullNum++;
      } else if (stack.getItemDamage() == 32767 || stack.getItemDamage() == 32768) {
        stack.setItemDamage(0);
      }
    }
    if (nullNum >= inputs.length) {
      Log.debug("Error while reading crafting recipes inputs for " + recipe.getRecipeOutput().getDisplayName() + " (ID:" + recipe.getRecipeOutput().itemID + ", Meta:" + recipe.getRecipeOutput().getItemDamage() + ")");
      Log.debug("IRecipe Type: " + recipe.getClass());
      Debug.printObject(recipe);
      return null;
    }
    return inputs;
  }
  
  private void increaseData(int id) {
    Data[] tmp = new Data[crafting[id].length + 1];
    System.arraycopy(crafting[id], 0, tmp, 0, crafting[id].length);
    crafting[id] = tmp;
  }
  
  private List<ItemStack> getReturned(IRecipe recipe, ItemStack[] inputs, ItemStack output) {
    List<ItemStack> data = new ArrayList<ItemStack>();
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
          if (inputs[i] != ic.getStackInSlot(i)) {
            data.add(ic.getStackInSlot(i));
          } else if (ic.getStackInSlot(i).getItem().hasContainerItem() && ic.getStackInSlot(i).getItem().getContainerItem() != null) {
            data.add(new ItemStack(ic.getStackInSlot(i).getItem().getContainerItem()));
          }
        }
      }
      for (ItemStack itemstack : OpenExchange.fakePlayer.inventory.mainInventory) {
        if (itemstack != null) {
          data.add(itemstack);
        }
      }
    } catch (Exception e) {
      Log.debug("Get Returned Failed for " + output.toString() + " IRecipe Type: " + recipe.getClass());
      Debug.handleException(e);
    }
    return data;
  }
}
