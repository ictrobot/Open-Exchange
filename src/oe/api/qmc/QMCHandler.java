package oe.api.qmc;

import java.util.Map;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

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
  
  public abstract Data getData(NBTBase nbt);
  
  public abstract double getQMC(Data data, ID id, Object o);
  
  public Map<QMCIDWrapper, Data> process(Map<QMCIDWrapper, Data> map) {
    return map;
  }
}
