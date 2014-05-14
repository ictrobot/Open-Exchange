package oe.qmc.file;

import oe.qmc.QMC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class QMCCustomAction {
  public final ItemStack itemstack;
  public final String ore;
  public final Action action;
  public final double value; // For Add
  
  public QMCCustomAction(Object o) {
    this.action = Action.BlackList;
    if (o instanceof ItemStack) {
      this.itemstack = (ItemStack) o;
      this.ore = null;
    } else if (o instanceof String) {
      this.itemstack = null;
      this.ore = (String) o;
    } else {
      this.itemstack = null;
      this.ore = null;
    }
    this.value = -1;
  }
  
  public QMCCustomAction(Object o, double value) {
    this.action = Action.Add;
    if (o instanceof ItemStack) {
      this.itemstack = (ItemStack) o;
      this.ore = null;
    } else if (o instanceof String) {
      this.itemstack = null;
      this.ore = (String) o;
    } else {
      this.itemstack = null;
      this.ore = null;
    }
    this.value = value;
  }
  
  public void execute() {
    Object o = getObject();
    if (action == Action.Add) {
      QMC.add(o, value);
    } else {
      QMC.blacklist(o);
    }
  }
  
  public boolean isValid() {
    if (itemstack != null || ore != null) {
      return true;
    }
    return false;
  }
  
  public NBTTagCompound toNBT() {
    NBTTagCompound nbt = new NBTTagCompound();
    if (itemstack != null) {
      nbt.setTag("item", itemstack.writeToNBT(new NBTTagCompound()));
    } else {
      nbt.setString("ore", ore);
    }
    if (action == Action.Add) {
      nbt.setDouble("value", value);
    }
    return nbt;
  }
  
  public static QMCCustomAction fromNBT(NBTTagCompound nbt) {
    Object o;
    if (nbt.hasKey("item")) {
      o = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"));
    } else {
      o = nbt.getString("ore");
    }
    if (nbt.hasKey("value")) {
      return new QMCCustomAction(o, nbt.getDouble("value"));
    } else {
      return new QMCCustomAction(o);
    }
  }
  
  private Object getObject() {
    if (itemstack != null) {
      return itemstack;
    } else {
      return ore;
    }
  }
  
  public String toString() {
    return action + " " + getObject();
  }
  
  public static enum Action {
    Add, BlackList;
  }
}
