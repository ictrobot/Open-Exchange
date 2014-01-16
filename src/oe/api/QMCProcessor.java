package oe.api;

import net.minecraft.nbt.NBTTagCompound;

public class QMCProcessor {
  
  public static class Data {
    private NBTTagCompound id;
    private NBTTagCompound data;
    private Boolean changed = false;
    
    public Data(NBTTagCompound id, NBTTagCompound data) {
      this.id = id;
      this.data = data;
    }
    
    public NBTTagCompound getID() {
      return id;
    }
    
    public NBTTagCompound getData() {
      return data;
    }
    
    public void setID(NBTTagCompound id) {
      changed = true;
      this.id = id;
    }
    
    public void setData(NBTTagCompound data) {
      changed = true;
      this.data = data;
    }
    
    public boolean changed() {
      return changed;
    }
  }
  
  /**
   * 
   * @param id
   * @param data
   * @return data
   */
  public void process(QMCProcessor.Data data) {
    
  }
  
}
