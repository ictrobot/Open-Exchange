package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.qmc.guess.GuessHandler;
import oe.api.qmc.id.ItemStackID;
import oe.core.util.OreDictionaryUtil;
import oe.qmc.QMC;

public class OreGuessHandler extends GuessHandler {

  public OreGuessHandler(Class<?> parent) {
    super(parent);
    for (String ore : OreDictionary.getOreNames()) {
      for (ItemStack itemstack : OreDictionary.getOres(ore)) {
        this.itemstacks.add(itemstack);
      }
    }
  }

  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null || !OreDictionaryUtil.isOre(itemstack)) {
      return -1;
    }
    for (ItemStack stack : OreDictionaryUtil.getItemStacks(OreDictionaryUtil.ore(itemstack))) {
      Guess.check(new ItemStackID(stack));
      if (QMC.hasQMC(stack)) {
        return QMC.getQMC(stack);
      }
    }
    return -1;
  }
}
