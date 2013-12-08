package oe.qmc.guess;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import oe.api.GuessHandler;
import oe.core.util.FluidUtil;

/*
 * This tries to make sure all fluid containers get guessed
 */
public class FluidGuessHandler extends GuessHandler {
  
  @Override
  public void init() {
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (FluidUtil.storesFluid(itemstack)) {
      Guess.check(FluidUtil.getEmpty(itemstack));
    }
    return -1;
  }
  
  @Override
  public List<Integer> meta(int ID) {
    List<Integer> meta = new ArrayList<Integer>();
    FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
    for (FluidContainerData f : data) {
      if (f.filledContainer.itemID == ID) {
        meta.add(f.filledContainer.getItemDamage());
      }
      if (f.emptyContainer.itemID == ID) {
        meta.add(f.filledContainer.getItemDamage());
      }
    }
    return meta;
  }
}
