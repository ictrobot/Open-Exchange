package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import oe.api.GuessHandler;
import oe.lib.util.FluidUtil;

/*
 * This tries to make sure all fluid containers get guessed
 */
public class FluidGuessHandler extends GuessHandler {
  
  public void init() {
  }
  
  public Guess.Data check(ItemStack itemstack) {
    if (FluidUtil.storesFluid(itemstack)) {
      Guess.check(FluidUtil.getEmpty(itemstack));
    }
    return null;
  }
  
  public int[] meta(int ID) {
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
