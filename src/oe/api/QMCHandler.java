package oe.api;

import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import oe.qmc.QMC.Data;
import oe.qmc.QMC.ID;
import oe.qmc.QMC.IDWrapper;

public abstract class QMCHandler {
  /*
   * Base QMC Database Class
   */
  public final Class<?>[] handles;
  
  public QMCHandler(Class<?>[] handles) {
    this.handles = handles;
  }
  
  public abstract ID getID(Object o);
  
  public abstract ID getIDFromNBT(NBTTagCompound nbt);
  
  public abstract Data getData(double qmc, ID id, Object o);
  
  public abstract Data getData(NBTTagCompound nbt);
  
  public abstract double getQMC(Data data, ID id, Object o);
  
  public Map<IDWrapper, Data> process(Map<IDWrapper, Data> map) {
    return map;
  }
}
