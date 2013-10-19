package oe.qmc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class QMCData {
  public double value;
  public ItemStack itemstack;
  public Block block;
  public Item item;
  public int type;
  public int ID;
  public int Meta;
  public boolean itemstackprovided;
  public boolean metaprovided;
  
  public QMCData(Block block2, double Value) {
    this.block = block2;
    this.itemstackprovided = false;
    this.itemstack = new ItemStack(block2);
    this.ID = block.blockID;
    this.Meta = 0;
    this.type = 0;
    this.value = Value;
    this.metaprovided = false;
  }
  
  public QMCData(ItemStack stack, double Value) {
    this.itemstackprovided = true;
    this.itemstack = stack;
    this.ID = stack.itemID;
    this.type = 3;
    this.Meta = 0;
    this.value = Value;
    this.metaprovided = false;
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
  }
}
