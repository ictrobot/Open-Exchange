package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import oe.api.OEGuesser;
import oe.lib.Log;
import oe.qmc.QMC;

public class Fluid extends OEGuesser {
  
  public static void init() {
  }
  
  public static Guess.Data check(ItemStack itemstack) {
    if (itemstack.itemID == 327) {
      Log.info(itemstack);
    }
    if (FluidContainerRegistry.isContainer(itemstack) && !FluidContainerRegistry.isEmptyContainer(itemstack)) {
      FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
      FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
      FluidContainerData fc = null;
      for (FluidContainerData f : data) {
        if (f.filledContainer.itemID == itemstack.itemID && f.filledContainer.getItemDamage() == itemstack.getItemDamage() && fc == null) {
          fc = f;
          break;
        }
      }
      if (fc != null) {
        double container = QMC.getQMC(fc.emptyContainer);
        double liquid = QMC.getQMC(fluidStack);
        if (container != -1 && liquid != -1) {
          return new Guess.Data(itemstack, container + liquid, 1);
        }
      }
    }
    return null;
  }
  
  public static int[] meta(int ID) {
    int[] meta = new int[0];
    FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
    for (FluidContainerData f : data) {
      if (f.filledContainer.itemID == ID) {
        int[] tmp = new int[meta.length + 1];
        System.arraycopy(meta, 0, tmp, 0, meta.length);
        meta = tmp;
        meta[meta.length - 1] = f.filledContainer.getItemDamage();
      }
    }
    return meta;
  }
}
