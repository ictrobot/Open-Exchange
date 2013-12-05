package oe.api;

import net.minecraft.nbt.NBTTagCompound;

public class QMCHandler {
  /*
   * Base QMC Database Class
   */
  public Double getQMC(Object o) {
    return new Double(-1);
  }
  
  public Boolean add(Object o, Double Value) {
    return false;
  }
  
  public Boolean blacklist(Object o) {
    return false;
  }
  
  public Boolean isBlacklisted(Object o) {
    return false;
  }
  
  public NBTTagCompound snapshot() {
    return new NBTTagCompound();
  }
  
  public void restoreSnapshot(NBTTagCompound nbt) {
    
  }
  
  public Integer length() {
    return 0;
  }
}
