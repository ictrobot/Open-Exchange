package oe.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

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
  public List<Integer> meta(int ID) {
    return new ArrayList<Integer>();
  }
  
  /**
   * Check a ItemStack
   */
  public double check(ItemStack itemstack) {
    return -1;
  }
}
