package oe.qmc.guess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import oe.api.OEGuesser;
import oe.lib.Debug;
import oe.lib.FakeContainer;
import oe.lib.Log;
import oe.lib.handler.ore.OreDictionaryHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class Crafting extends OEGuesser {
  
  private static GuessData[][] crafting = new GuessData[32000][0];
  
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
            CheckData cd = afterCrafting(recipe, input, output);
            crafting[id][crafting[id].length - 1] = new GuessData(output, input, cd);
            recipes++;
          }
        }
      }
    }
    Log.debug("Found " + recipes + " Crafting Recipes");
  }
  
  public static GuessReturn check(ItemStack itemstack) {
    if (itemstack == null) {
      return null;
    }
    int id = itemstack.itemID;
    GuessData[] data = new GuessData[0];
    for (GuessData gd : crafting[id]) {
      if (itemstack.getItemDamage() == gd.output.getItemDamage()) {
        GuessData[] tmp = new GuessData[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
        data[data.length - 1] = gd;
      }
    }
    if (data.length != 0) {
      for (GuessData gd : data) {
        double value = 0;
        double[] values = new double[gd.input.length];
        CheckData cd = gd.after;
        for (int i = 0; i < gd.input.length; i++) {
          ItemStack stack = gd.input[i];
          ItemStack check = cd.after[i];
          if (stack != null) {
            // Log.info("B: " + stack.itemID + ":" + stack.getItemDamage() + "x" + stack.stackSize);
            // Log.info("A: " + check.itemID + ":" + check.getItemDamage() + "x" + check.stackSize);
            if (stack.equals(check)) {
              double v = checkQMC(stack);
              if (v == -1) {
                value = v;
                break;
              } else {
                values[i] = v;
                value = value + v;
              }
            } else {
              double v = checkQMC(stack);
              if (v == -1) {
                value = v;
                break;
              } else {
                double c = checkQMC(check);
                if (c == -1) {
                  value = v;
                  break;
                }
                values[i] = v;
                value = value + v - c;
              }
            }
          }
        }
        if (value > 0) {
          value = value / gd.output.stackSize;
          GuessReturn toReturn = new GuessReturn(gd.input, values, value, gd.output.stackSize);
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
    for (GuessData gd : crafting[ID]) {
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
            for (int i = 0; i < os.length; i++) {
              Object r = os[i];
              if (r instanceof ItemStack) {
                ItemStack stack = (ItemStack) r;
                inputs[i] = stack;
              } else if (r instanceof String) {
                String ore = (String) r;
                ItemStack[] stacks = OreDictionaryHelper.getItemStacks(ore);
                if (stacks != null) {
                  inputs[i] = stacks[0];
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
    if (nullNum == inputs.length) {
      Log.debug("Error while reading crafting recipes inputs for " + recipe.getRecipeOutput().toString() + " (ID: " + recipe.getRecipeOutput().itemID + ")");
      Log.debug("IRecipe Type: " + recipe.getClass());
      return null; // Failed to read Recipe
    }
    return inputs;
  }
  
  private static void increaseCrafting(int id) {
    GuessData[] tmp = new GuessData[crafting[id].length + 1];
    System.arraycopy(crafting[id], 0, tmp, 0, crafting[id].length);
    crafting[id] = tmp;
  }
  
  private static CheckData afterCrafting(IRecipe recipe, ItemStack[] inputs, ItemStack output) {
    ItemStack[] toReturn = new ItemStack[inputs.length];
    boolean changed = false;
    try {
      FakeContainer fake = new FakeContainer();
      InventoryCrafting ic = new InventoryCrafting(fake, 3, 3);
      for (int i = 0; i < inputs.length; i++) {
        if (inputs[i] != null) {
          ic.setInventorySlotContents(i, inputs[i]);
        }
      }
      World[] worlds = MinecraftServer.getServer().worldServers;
      World world = worlds[0];
      EntityPlayer player = new FakePlayer(world, "[OE]");
      GameRegistry.onItemCrafted(player, output, ic);
      for (int i = 0; i < inputs.length; i++) {
        if (inputs[i] != null) {
          if (ic.getStackInSlot(i).getItem().hasContainerItem()) {
            ic.setInventorySlotContents(i, new ItemStack(ic.getStackInSlot(i).getItem().getContainerItem()));
          }
          if (inputs[i] != ic.getStackInSlot(i)) {
            changed = true;
          }
          toReturn[i] = ic.getStackInSlot(i);
        }
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
    CheckData cd = new CheckData();
    cd.changed = changed;
    cd.after = toReturn;
    return cd;
  }
}
