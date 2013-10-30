package oe.qmc.guess;

import net.minecraft.item.ItemStack;

public class GuessReturn {
  public ItemStack[] ingredients;
  public double[] ingredientsQMC;
  public double totalQMC;
  public int outputNum; // How Many this recipe makes, however the totalQMC should of already been
                        // divided by this
  
  public GuessReturn(ItemStack[] Ingredients, double[] IngredientsQMC, double TotalQMC, int OutputNum) {
    this.ingredients = Ingredients;
    this.ingredientsQMC = IngredientsQMC;
    this.totalQMC = TotalQMC;
    this.outputNum = OutputNum;
  }
}
