package oe.qmc.guess;

import net.minecraft.item.ItemStack;

public class GuessData {
  public ItemStack output;
  public ItemStack[] input;
  public CheckData after;
  
  public GuessData(ItemStack Output, ItemStack[] Input) {
    output = Output;
    input = Input;
    after = null;
  }
  
  public GuessData(ItemStack Output, ItemStack[] Input, CheckData After) {
    output = Output;
    input = Input;
    after = After;
  }
  
  public GuessData(ItemStack Output, ItemStack Input) {
    input = new ItemStack[1];
    input[0] = Input;
    output = Output;
    after = null;
  }
}
