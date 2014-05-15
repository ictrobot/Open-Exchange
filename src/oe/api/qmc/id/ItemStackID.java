package oe.api.qmc.id;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import oe.api.qmc.ID;
import oe.api.qmc.QMCHandler;
import oe.qmc.QMCItemStack;

public class ItemStackID implements ID {
  
  public final String id;
  public final int meta;
  public final NBTTagCompound nbt;
  private int hashcode;
  
  public ItemStackID(ItemStack itemstack) {
    this(itemstack.getUnlocalizedName(), itemstack.getItemDamage(), itemstack.getTagCompound());
  }
  
  public ItemStackID(String id, int meta, NBTTagCompound nbt) {
    this.id = id;
    this.meta = meta;
    this.nbt = nbt;
    if (nbt != null) {
      this.hashcode = id.hashCode() ^ (meta + 1) ^ nbt.hashCode();
    } else {
      this.hashcode = id.hashCode() ^ (meta + 1);
    }
  }
  
  public boolean hasNBT() {
    return nbt != null;
  }
  
  @Override
  public NBTTagCompound toNBT() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setString("id", id);
    nbt.setInteger("meta", meta);
    if (hasNBT()) {
      nbt.setTag("nbt", nbt);
    }
    return nbt;
  }
  
  public static ItemStackID fromNBT(NBTTagCompound nbt) {
    NBTTagCompound n = null;
    if (nbt.hasKey("nbt")) {
      n = nbt.getCompoundTag("nbt");
    }
    return new ItemStackID(nbt.getString("id"), nbt.getInteger("meta"), n);
  }
  
  public ItemStack getItemStack() {
    return getItemStack(1);
  }
  
  public ItemStack getItemStack(int stackSize) {
    ItemStack i = new ItemStack((Item) Item.itemRegistry.getObject(id), stackSize, meta);
    if (hasNBT()) {
      i.stackTagCompound = (NBTTagCompound) nbt.copy();
    }
    return i;
  }
  
  @Override
  public Class<? extends QMCHandler> getQMCHandlerClass() {
    return QMCItemStack.class;
  }
  
  public int hashCode() {
    return hashcode;
  }
  
  public boolean equals(Object o) {
    if (o instanceof ItemStackID) {
      ItemStackID i = (ItemStackID) o;
      return i.id == id && i.meta == meta && ((i.nbt == null && nbt == null) || i.nbt == nbt);
    }
    return false;
  }
  
  public String toString() {
    return id;
  }
}
