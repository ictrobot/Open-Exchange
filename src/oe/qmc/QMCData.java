package oe.qmc;

import oe.qmc.guess.GuessReturn;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class QMCData {
  public double QMC; // QMC Value
  public ItemStack itemstack; // Itemstack
  public String oreDictionary; // Ore Dictionary
  public QMCType type;
  public GuessReturn guess;
  
  public QMCData(ItemStack stack, double Value) {
    this.itemstack = stack;
    this.QMC = Value;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.oreDictionary = OreDictionary.getOreName(oreID);
      this.type = QMCType.OreDictionary_Itemstack;
    } else {
      this.oreDictionary = "NONE";
      this.type = QMCType.Itemstack;
    }
    this.guess = null;
  }
  
  public QMCData(ItemStack stack, GuessReturn data) {
    this.itemstack = stack;
    this.QMC = data.totalQMC;
    this.guess = data;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.oreDictionary = OreDictionary.getOreName(oreID);
      this.type = QMCType.OreDictionary_Itemstack;
    } else {
      this.oreDictionary = "NONE";
      this.type = QMCType.Itemstack;
    }
  }
  
  public QMCData(String ore, double Value) {
    this.type = QMCType.OreDictionary;
    this.QMC = Value;
    this.itemstack = null;
    this.oreDictionary = ore;
    this.guess = null;
  }
}
