package oe.core.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class FluidUtil {
  
  public static boolean storesFluid(ItemStack itemstack) {
    if (itemstack.getItem() instanceof IFluidContainerItem) {
      return true;
    }
    for (FluidContainerData f : getFluidData()) {
      if (Item.getIdFromItem(f.filledContainer.getItem()) == Item.getIdFromItem(itemstack.getItem()) && f.filledContainer.getItemDamage() == itemstack.getItemDamage()) {
        return true;
      }
    }
    return false;
  }
  
  public static FluidStack getFluidStack(ItemStack itemstack) {
    if (itemstack == null) {
      return null;
    }
    if (itemstack.getItem() instanceof IFluidContainerItem) {
      IFluidContainerItem f = (IFluidContainerItem) itemstack.getItem();
      FluidStack r = f.getFluid(itemstack);
      return r;
    }
    for (FluidContainerData f : getFluidData()) {
      if (Item.getIdFromItem(f.filledContainer.getItem()) == Item.getIdFromItem(itemstack.getItem()) && f.filledContainer.getItemDamage() == itemstack.getItemDamage()) {
        return f.fluid;
      }
    }
    return null;
  }
  
  public static int getAmountStored(ItemStack itemstack) {
    FluidStack f = getFluidStack(itemstack);
    if (f != null) {
      return f.amount;
    }
    return -1;
  }
  
  public static ItemStack getEmpty(ItemStack itemstack) {
    if (itemstack.getItem() instanceof IFluidContainerItem) {
      ItemStack c = itemstack.copy();
      IFluidContainerItem f = (IFluidContainerItem) c.getItem();
      FluidStack fStack = f.getFluid(c);
      if (fStack != null) {
        f.drain(c, fStack.amount, true);
        return c;
      }
    }
    for (FluidContainerData f : getFluidData()) {
      if (Item.getIdFromItem(f.filledContainer.getItem()) == Item.getIdFromItem(itemstack.getItem()) && f.filledContainer.getItemDamage() == itemstack.getItemDamage()) {
        return f.emptyContainer;
      }
    }
    return null;
  }
  
  public static List<ItemStack> getStoringItemStacks(int fluidID) {
    List<ItemStack> data = new ArrayList<ItemStack>();
    for (FluidContainerData f : getFluidData()) {
      if (f.fluid.fluidID == fluidID) {
        data.add(f.filledContainer);
      }
    }
    return data;
  }
  
  public static net.minecraftforge.fluids.Fluid getFluid(String name) {
    net.minecraftforge.fluids.Fluid f = null;
    try {
      f = FluidRegistry.getFluid(FluidRegistry.getFluidID(name));
    } catch (Exception e) {
      return null;
    }
    return f;
  }
  
  public static int getFluidID(String name) {
    return FluidRegistry.getFluidID(name);
  }
  
  public static net.minecraftforge.fluids.Fluid getFluid(int id) {
    return FluidRegistry.getFluid(id);
  }
  
  public static String getName(int id) {
    return FluidRegistry.getFluidName(id);
  }
  
  public static FluidContainerData[] getFluidData() {
    return FluidContainerRegistry.getRegisteredFluidContainerData();
  }
}
