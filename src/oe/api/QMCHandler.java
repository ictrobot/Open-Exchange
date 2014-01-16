package oe.api;

import net.minecraft.nbt.NBTTagCompound;

public class QMCHandler {
  /*
   * Base QMC Database Class
   */
  public final Class<?>[] handles;
  
  public QMCHandler(Class<?>[] handles) {
    this.handles = handles;
  }
  
  public double decode(Object o, NBTTagCompound nbt) {
    return nbt.getDouble("qmc");
  }
  
  public NBTTagCompound encode(Object o, double qmc) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("qmc", qmc);
    return nbt;
  }
  
  public NBTTagCompound blacklist(Object o) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("qmc", -1);
    return nbt;
  }
  
  public Boolean isBlacklisted(Object o, NBTTagCompound nbt) {
    if (nbt.getDouble("qmc") <= 0) {
      return true;
    }
    return false;
  }
  
  public NBTTagCompound getID(Object o) {
    return new NBTTagCompound();
  }
}
