package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.core.util.FluidUtil;

/*
 * This tries to make sure all fluid containers get guessed
 */
public class FluidGuessHandler extends GuessHandler {
  
  @Override
  public double check(ItemStack itemstack) {
    if (FluidUtil.storesFluid(itemstack)) {
      Guess.check(FluidUtil.getEmpty(itemstack));
    }
    return -1;
  }
}
