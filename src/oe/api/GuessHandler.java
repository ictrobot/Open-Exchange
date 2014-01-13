package oe.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class GuessHandler {
  
  public List<ItemStack> itemstacks;
  public final Class<?> parent;
  
  public GuessHandler(Class<?> parent) {
    this.parent = parent;
    this.itemstacks = new ArrayList<ItemStack>();
  }
  
  /**
   * Load values
   */
  public void init() {
    
  }
  
  /**
   * Check an Itemstack
   */
  public double check(ItemStack itemstack) {
    return -1;
  }
}
