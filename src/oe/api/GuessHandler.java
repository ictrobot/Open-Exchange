package oe.api;

import net.minecraft.item.ItemStack;
import oe.qmc.guess.Guess;

public class GuessHandler {
  
  /**
   * Load values
   */
  public void init() {
    
  }
  
  /**
   * Check what meta data there are results for.
   * 
   * @param ID
   * @return int[] of meta data;
   */
  public int[] meta(int ID) {
    return new int[] {};
  }
  
  /**
   * Check a ItemStack
   */
  public Guess.Data check(ItemStack itemstack) {
    return null;
  }
}
