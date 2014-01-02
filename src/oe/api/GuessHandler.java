package oe.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class GuessHandler {
  
  /**
   * The Type
   */
  public static enum Type {
    Passive, Active;
    
    public String toString() {
      return this.name() + " Guess Handler";
    }
  }
  
  public final Type type;
  
  public GuessHandler() {
    this(Type.Passive);
  }
  
  protected GuessHandler(Type type) {
    this.type = type;
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
  
  public static class ActiveGuessHandler extends GuessHandler {
    
    public ActiveGuessHandler() {
      super(Type.Active);
    }
    
    public List<ItemStack> getItemStacks() {
      return new ArrayList<ItemStack>();
    }
  }
}
