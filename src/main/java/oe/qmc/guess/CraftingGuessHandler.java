package oe.qmc.guess;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import oe.OpenExchange;
import oe.api.qmc.guess.GuessHandler;
import oe.api.qmc.id.ItemStackID;
import oe.core.Debug;
import oe.core.Log;
import oe.core.data.FakeContainer;
import oe.core.util.ItemStackUtil;
import oe.core.util.PlayerUtil;
import oe.core.util.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CraftingGuessHandler extends GuessHandler {

  public static class Input {

    public boolean finalized = false;
    public boolean isValid = true;

    @SuppressWarnings("unchecked")
    public List<Integer>[] data = new ArrayList[9];

    public ArrayList<ItemStackID> itemstacks = new ArrayList<ItemStackID>();
    public boolean[] value;

    public Input() {
      for (int i = 0; i < 9; i++) {
        data[i] = new ArrayList<Integer>();
      }
    }

    public void addInput(int slot, ItemStack itemstack) {
      if (finalized) {
        throw new RuntimeException("Input was finalized");
      }
      if (itemstack.getItemDamage() == Short.MAX_VALUE) {
        itemstack.setItemDamage(0);
      }
      ItemStackID i = new ItemStackID(itemstack);
      if (!itemstacks.contains(i)) {
        itemstacks.add(i);
      }
      List<Integer> list = data[slot];
      list.add(itemstacks.indexOf(i));
    }

    public void addInput(int slot, List<ItemStack> itemstacks) {
      if (!Util.notEmpty(itemstacks)) {
        return;
      }
      for (ItemStack itemstack : itemstacks) {
        addInput(slot, itemstack);
      }
    }

    public boolean isBlank() {
      return itemstacks.isEmpty();
    }

    public void checkValues() {
      finalized = true;
      for (int i = 0; i < itemstacks.size(); i++) {
        value[i] = Guess.check(itemstacks.get(i)) > 0;
      }
    }

    public void removeFailed() {
      for (int slot = 0; slot < 9; slot++) {
        boolean isBlank = data[slot].isEmpty();
        Iterator<Integer> i = data[slot].iterator();
        while (i.hasNext()) {
          if (!value[i.next()]) {
            i.remove();
          }
        }
        if (data[slot].isEmpty() && !isBlank) {
          isValid = false;
          return;
        }
      }
    }

    public void setCheapest() {
      for (int slot = 0; slot < 9; slot++) {
        if (isSlotBlank(slot)) {
          continue;
        }
        int cheapestNum = -1;
        double cheapest = 0;
        for (int i = 0; i < data[slot].size(); i++) {
          double qmc = Guess.check(itemstacks.get(i));
          if (qmc > 0 && qmc > cheapest) {
            cheapest = qmc;
            cheapestNum = i;
          }
        }
        int c = data[slot].get(cheapestNum);
        data[slot].clear();
        data[slot].add(c);
      }
    }

    public ArrayList<ItemStackID> list() {
      ArrayList<ItemStackID> r = new ArrayList<ItemStackID>();
      for (int slot = 0; slot < 9; slot++) {
        if (isSlotBlank(slot)) {
          continue;
        }
        r.add(itemstacks.get(data[slot].get(0)));
      }
      return r;
    }

    public boolean isSlotBlank(int slot) {
      return data[slot].isEmpty();
    }

    public ItemStackID getSlot(int slot) {
      return itemstacks.get(data[slot].get(0));
    }
  }

  public CraftingGuessHandler(IRecipe recipe) {
    super(CraftingGuessHandlerFactory.class);
    this.recipe = recipe;
    this.itemstacks.add(recipe.getRecipeOutput());
  }

  private IRecipe recipe;

  public String toString() {
    return "CraftingGuessHandler[ " + recipe.toString() + " ]";
  }

  // ItemID, Pair of Output ItemStack and Input (See above)
  private Input input;

  private ItemStack getItemstack() {
    return this.itemstacks.get(0);
  }

  @Override
  public double check(ItemStack itemstack) {
    ItemStack output = recipe.getRecipeOutput();
    if (output != null) {
      Input input = getInput(recipe);
      if (input != null) {
        this.input = input;
      } else {
        return -1;
      }
    }
    input.checkValues();
    input.removeFailed();
    if (!input.isValid) {
      return -1;
    }
    input.setCheapest();
    double qmc = 0;
    for (ItemStackID id : input.list()) {
      double q = Guess.check(id);
      if (q > 0) {
        qmc += q;
      } else {
        return -1;
      }
    }
    for (ItemStack returned : getReturned(input)) {
      double q = Guess.check(new ItemStackID(returned));
      if (q > 0) {
        qmc -= q;
      } else {
        return -1;
      }
    }
    return qmc / output.stackSize;
  }

  private Input getInput(IRecipe recipe) {
    Input input = new Input();
    boolean failed = false;
    if (recipe instanceof ShapedRecipes) {
      ShapedRecipes shaped = (ShapedRecipes) recipe;
      for (int i = 0; i < shaped.recipeItems.length; i++) {
        if (shaped.recipeItems[i] instanceof ItemStack) {
          input.addInput(i, shaped.recipeItems[i]);
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
      Log.debug("Error while reading crafting recipes inputs for " + recipe.getRecipeOutput().getDisplayName() + " (ID:" + recipe.getRecipeOutput().getUnlocalizedName() + ", Meta:" + recipe.getRecipeOutput().getItemDamage() + ")");
      Log.debug("IRecipe Type: " + recipe.getClass());
      Debug.printObject(recipe);
      return null;
    }
    return input;
  }

  private List<ItemStack> getReturned(Input input) {
    ItemStack output = getItemstack();
    List<ItemStack> data = new ArrayList<ItemStack>();
    try {
      FakeContainer fake = new FakeContainer();
      InventoryCrafting ic = new InventoryCrafting(fake, 3, 3);
      for (int i = 0; i < 9; i++) {
        if (!input.isSlotBlank(i)) {
          ic.setInventorySlotContents(i, input.getSlot(i).getItemStack());
        }
      }
      PlayerUtil.wipeInv(OpenExchange.fakePlayer);
      output.onCrafting(OpenExchange.fakePlayer.worldObj, OpenExchange.fakePlayer, 1);
      for (int i = 0; i < 9; i++) {
        if (!input.isSlotBlank(i)) {
          if (input.getSlot(i).getItemStack() != ic.getStackInSlot(i)) {
            data.add(ic.getStackInSlot(i));
          } else if (ic.getStackInSlot(i).getItem().hasContainerItem(ic.getStackInSlot(i)) && ic.getStackInSlot(i).getItem().getContainerItem() != null) {
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
