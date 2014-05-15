package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import oe.api.qmc.guess.GuessHandler;
import oe.core.util.FluidUtil;

/*
 * This tries to make sure all fluid containers get guessed
 */
public class FluidGuessHandler extends GuessHandler {
  
  public FluidGuessHandler(Class<?> parent) {
    super(parent);
    for (FluidContainerData data : FluidUtil.getFluidData()) {
      this.itemstacks.add(data.filledContainer);
    }
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (FluidUtil.storesFluid(itemstack)) {
      Guess.check(FluidUtil.getEmpty(itemstack));
    }
    return -1;
  }
}
