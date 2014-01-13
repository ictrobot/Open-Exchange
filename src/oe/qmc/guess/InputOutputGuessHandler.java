package oe.qmc.guess;

import net.minecraft.item.ItemStack;
import oe.api.GuessHandler;

/**
 * Simple Guess Handler with one Input and one Output
 */
public class InputOutputGuessHandler extends GuessHandler {
  
  public final ItemStack input;
  public final ItemStack output;
  
  public InputOutputGuessHandler(ItemStack input, ItemStack output, Class<?> parent) {
    super(parent);
    this.input = input;
    this.output = output;
    this.itemstacks.add(input);
  }
  
  @Override
  public double check(ItemStack itemstack) {
    if (itemstack == null || itemstack != input) {
      return -1;
    }
    double value = Guess.check(output);
    if (value > 0) {
      return value;
    }
    return -1;
  }
}
