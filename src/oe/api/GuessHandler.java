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
   * 
   * @return False to be marked as not valid or True to be marked as valid
   */
  public boolean init() {
    return true;
  }
  
  /**
   * Check an Itemstack
   */
  public double check(ItemStack itemstack) {
    return -1;
  }
}
