package oe.api;

import net.minecraft.item.ItemStack;

public class GuessHandler {
  
  /**
   * Load values
   */
  public void init() {
    
  }
  
  /**
   * Anything before the main guess loop
   */
  public void beforeGuess() {
    
  }
  
  /**
   * Check an Itemstack
   */
  public double check(ItemStack itemstack) {
    return -1;
  }
  
  public static class ActiveGuessHandler extends GuessHandler {
    
    /**
     * Guess
     */
    public void guess() {
      
    }
  }
}
