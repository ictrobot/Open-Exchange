package oe.qmc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class QMCData {
  public double value;
  public ItemStack itemstack;
  public Block block;
  public Item item;
  public int type; // 1 Item. 0 Block, 3 ItemStack, -1 Ore
  public int ID;
  public int Meta;
  public boolean itemstackprovided;
  public boolean metaprovided;
  public String ore;
  public boolean hasOre;
  
  public QMCData(Block block2, double Value) {
    this.block = block2;
    this.itemstackprovided = false;
    this.itemstack = new ItemStack(block2);
    this.ID = block.blockID;
    this.Meta = 0;
    this.type = 0;
    this.value = Value;
    this.metaprovided = false;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(ItemStack stack, double Value) {
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = stack.itemID;
    this.type = 3;
    this.Meta = 0;
    this.value = Value;
    this.metaprovided = false;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(ItemStack stack, double Value, boolean b) {
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = stack.itemID;
    this.type = 3;
    this.Meta = stack.getItemDamage();
    this.value = Value;
    this.metaprovided = true;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Block block2, int meta, double Value) {
    this.block = block2;
    this.itemstackprovided = false;
    this.itemstack = new ItemStack(block2, 1, meta);
    this.ID = block.blockID;
    this.Meta = meta;
    this.type = 0;
    this.value = Value;
    this.metaprovided = true;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Block block2, int meta, ItemStack stack, double Value) {
    this.block = block2;
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = block.blockID;
    this.Meta = meta;
    this.type = 0;
    this.value = Value;
    this.metaprovided = true;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Block block2, ItemStack stack, double Value) {
    this.block = block2;
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = block.blockID;
    this.Meta = 0;
    this.type = 0;
    this.value = Value;
    this.metaprovided = false;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Item item2, int meta, ItemStack stack, double Value) {
    this.item = item2;
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = item2.itemID;
    this.Meta = meta;
    this.type = 1;
    this.value = Value;
    this.metaprovided = true;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Item item2, int meta, double Value) {
    this.item = item2;
    this.itemstackprovided = false;
    this.itemstack = new ItemStack(item2, 1, meta);
    this.ID = item2.itemID;
    this.Meta = meta;
    this.type = 1;
    this.value = Value;
    this.metaprovided = true;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Item item2, double Value) {
    this.item = item2;
    this.itemstackprovided = false;
    this.itemstack = new ItemStack(item2);
    this.ID = item2.itemID;
    this.Meta = 0;
    this.type = 1;
    this.value = Value;
    this.metaprovided = false;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(Item item2, ItemStack stack, double Value) {
    this.item = item2;
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = item2.itemID;
    this.Meta = 0;
    this.type = 1;
    this.value = Value;
    this.metaprovided = false;
    int oreID = OreDictionary.getOreID(this.itemstack);
    if (oreID != -1) {
      this.ore = OreDictionary.getOreName(oreID);
      this.hasOre = true;
    } else {
      this.hasOre = false;
    }
  }
  
  public QMCData(String ore2, double Value) {
    this.itemstackprovided = false;
    this.type = -1;
    this.value = Value;
    this.ore = ore2;
    this.hasOre = true;
  }
}
