package oe.qmc.guess;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;
import oe.core.util.OreDictionaryUtil;
import oe.qmc.QMC;

public class OreGuessHandler extends GuessHandler {
  
  @Override
  public void init() {
    
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null || !OreDictionaryUtil.isOre(itemstack)) {
      return -1;
    }
    for (ItemStack stack : OreDictionaryUtil.getItemStacks(OreDictionaryUtil.ore(itemstack))) {
      Guess.check(stack);
      if (QMC.hasQMC(stack)) {
        return QMC.getQMC(stack);
      }
    }
    return -1;
  }
  
  @Override
  public List<Integer> meta(int ID) {
    return new ArrayList<Integer>();
  }
}
