package oe.qmc.guess;

import net.minecraft.item.ItemStack;

public class GuessData {
  public ItemStack output;
  public ItemStack[] input;
  
  public GuessData(ItemStack Output, ItemStack[] Input) {
    output = Output;
    input = Input;
  }
}
