package oe.api;

import oe.qmc.guess.GuessReturn;
import net.minecraft.item.ItemStack;

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
  public static GuessReturn check(ItemStack itemstack) {
    return null;
  }
}
