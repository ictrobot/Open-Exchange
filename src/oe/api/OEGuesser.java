package oe.api;

import net.minecraft.item.ItemStack;
import oe.qmc.guess.Guess;

public class OEGuesser {
  /*
   * Not Interface because it needs to be static
   */
  
  /**
   * Load values
   */
  public static void init() {
    
  }
  
  /**
   * Check what meta data there are results for.
   * 
   * @param ID
   * @return int[] of meta data;
   */
  public static int[] meta(int ID) {
    return new int[] {};
  }
  
  /**
   * Check a ItemStack
   */
  public static Guess.Data check(ItemStack itemstack) {
    return null;
  }
}
