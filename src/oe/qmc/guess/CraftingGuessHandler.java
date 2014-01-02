package oe.qmc.guess;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import oe.OpenExchange;
import oe.api.GuessHandler.ActiveGuessHandler;
import oe.core.Debug;
import oe.core.Log;
import oe.core.data.FakeContainer;
import oe.core.util.ItemStackUtil;
import oe.core.util.PlayerUtil;
import oe.core.util.Util;
import oe.core.util.data.Pair;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingGuessHandler extends ActiveGuessHandler {
  
  public static class Input {
    
    private HashMap<Integer, List<ItemStack>> data = new HashMap<Integer, List<ItemStack>>();
    public boolean isValid = true;
    
    public Input() {
      for (int i = 0; i < 9; i++) {
        data.put(i, new ArrayList<ItemStack>());
      }
    }
    
    public void addInput(int slot, ItemStack itemstack) {
      if (itemstack.getItemDamage() == Short.MAX_VALUE) {
        itemstack.setItemDamage(0);
      }
      List<ItemStack> list = data.get(slot);
      data.remove(slot);
      list.add(itemstack);
      data.put(slot, list);
    }
    
    public void addInput(int slot, List<ItemStack> itemstacks) {
      if (!Util.notEmpty(itemstacks)) {
        return;
      }
      for (ItemStack itemstack : itemstacks) {
        addInput(slot, itemstack);
      }
    }
    
    public List<ItemStack> getInputs(int slot) {
      return data.get(slot);
    }
    
    public boolean isBlank() {
      int blank = 0;
      for (int i = 0; i < 9; i++) {
        if (data.get(i).size() == 0) {
          blank++;
        }
      }
      if (blank == 9) {
        return true;
      }
      return false;
    }
    
    public String toString() {
      StringBuilder s = new StringBuilder().append("[");
      for (int i = 0; i < 9; i++) {
        s.append(i + " - ");
        if (Util.notEmpty(getInputs(i))) {
          s.append(getInputs(i).toString());
        } else {
          s.append("BLANK");
        }
        if (i != 8) {
          s.append(", ");
        }
      }
      s.append("]");
      return s.toString();
    }
  }
  
  // ItemID, Pair of Output ItemStack and Input (See above)
  private HashMap<Integer, List<Pair<ItemStack, Input>>> crafting = new HashMap<Integer, List<Pair<ItemStack, Input>>>();
  private List<ItemStack> toGuess = new ArrayList<ItemStack>();
  
  @Override
  public void init() {
    int recipes = 0;
    for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
      try {
        if (recipeObject instanceof IRecipe) {
          IRecipe recipe = (IRecipe) recipeObject;
          ItemStack output = recipe.getRecipeOutput();
          if (output != null) {
            Input input = getInputs(recipe);
            if (input != null) {
              List<Pair<ItemStack, Input>> data;
              if (crafting.get(output.itemID) != null) {
                data = crafting.get(output.itemID);
                crafting.remove(output.itemID);
              } else {
                data = new ArrayList<Pair<ItemStack, Input>>();
              }
              data.add(new Pair<ItemStack, Input>(output, input));
              toGuess.add(output);
              crafting.put(output.itemID, data);
              recipes++;
            }
          }
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
    Log.debug("Found " + recipes + " Crafting Recipes");
  }
  
  @Override
  public List<ItemStack> getItemStacks() {
    return toGuess;
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null || !Util.notEmpty(crafting.get(itemstack.itemID))) {
      return -1;
    }
    int id = itemstack.itemID;
    for (Pair<ItemStack, Input> pair : crafting.get(id)) {
      if (pair.left.getItemDamage() == itemstack.getItemDamage()) {
        double value = check(pair);
        if (value > 0) {
          return value;
        }
      }
    }
    return -1;
  }
  
  private double check(Pair<ItemStack, Input> pair) {
    Guess.currentlyChecking.add(pair.left);
    double total = -1;
    List<Pair<ItemStack[], Double>> data = getValuedInputs(pair);
    for (Pair<ItemStack[], Double> p : data) {
      double t = p.right;
      for (ItemStack itemstack : getReturned(p.left, pair.left)) {
        double value = Guess.check(itemstack);
        if (value > 0) {
          t = -value;
        } else {
          t = -1;
          break;
        }
      }
      if (t > 0) {
        total = Util.minPos(total, t);
      }
    }
    Guess.currentlyChecking.remove(pair.left);
    return total;
  }
  
  private List<Pair<ItemStack[], Double>> getValuedInputs(Pair<ItemStack, Input> pair) {
    ItemStack output = pair.left;
    Input input = pair.right;
    List<Pair<ItemStack[], Double>> data = new ArrayList<Pair<ItemStack[], Double>>();
    ItemStack[] stacks;
    int maxComb = 1;
    int[] toSkip = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    List<Integer> multiple = new ArrayList<Integer>();
    for (int i = 0; i < 9; i++) {
      if (input.getInputs(i).size() > 1) {
        multiple.add(i);
      }
      if (input.getInputs(i).size() > 0) {
        maxComb = maxComb * input.getInputs(i).size();
      }
    }
    maxComb--;
    for (int i = 0; i <= maxComb; i++) {
      int comb = i;
      int listPos = -1;
      while (comb > 0) {
        listPos++;
        if (listPos >= multiple.size()) {
          listPos = 0;
        }
        toSkip[multiple.get(listPos)]++;
      }
      double total = 0;
      stacks = new ItemStack[9];
      int notEmpty = 0;;
      int foundValue = 0;
      for (int slot = 0; slot < 9; slot++) {
        if (Util.notEmpty(input.getInputs(slot))) {
          notEmpty++;
          for (ItemStack itemstack : input.getInputs(slot)) {
            double value = Guess.check(itemstack);
            if (toSkip[slot] == 0) {
              if (value > 0) {
                stacks[slot] = itemstack;
                total = total += value;
                foundValue++;
                break;
              }
            } else {
              toSkip[slot]--;
            }
          }
          if (foundValue < notEmpty) {
            continue;
          }
        }
      }
      if (total > 0) {
        total = total / output.stackSize;
        data.add(new Pair<ItemStack[], Double>(stacks, total));
      }
    }
    return data;
  }
  
  private static Input getInputs(IRecipe recipe) {
    Input input = new Input();
    boolean failed = false;
    if (recipe instanceof ShapedRecipes) {
      ShapedRecipes shaped = (ShapedRecipes) recipe;
      for (int i = 0; i < shaped.recipeItems.length; i++) {
        if (shaped.recipeItems[i] instanceof ItemStack) {
          input.addInput(i, shaped.recipeItems[i]);;
        }
      }
    } else if (recipe instanceof ShapelessRecipes) {
      ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
      int i = 0;
      for (Object object : shapeless.recipeItems) {
        ItemStack itemstack = ItemStackUtil.getItemStack(object);
        if (itemstack != null) {
          input.addInput(i, itemstack);
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
                List<ItemStack> stacks = ItemStackUtil.getItemStacksOneOre(r);
                if (Util.notEmpty(stacks)) {
                  input.addInput(i, stacks);
                } else {
                  failed = true;
                  break;
                }
              }
            }
            break;
          }
        }
      }
    }
    if (failed || input.isBlank()) {
      Log.debug("Error while reading crafting recipes inputs for " + recipe.getRecipeOutput().getDisplayName() + " (ID:" + recipe.getRecipeOutput().itemID + ", Meta:" + recipe.getRecipeOutput().getItemDamage() + ")");
      Log.debug("IRecipe Type: " + recipe.getClass());
      Debug.printObject(recipe);
      return null;
    }
    return input;
  }
  
  private List<ItemStack> getReturned(ItemStack[] inputs, ItemStack output) {
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
      Log.debug("Get Returned Failed for " + output.toString());
      Debug.handleException(e);
    }
    return data;
  }
}
