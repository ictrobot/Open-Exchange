package oe.qmc;

import net.minecraftforge.fluids.Fluid;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class QMCFluid {
  
  public static class Data {
    public double QMC; // QMC Value
    public Fluid fluid;
    
    public Data(Fluid Fluid, double Value) {
      this.fluid = Fluid;
      this.QMC = Value;
    }
  }
  
  private static Data[] data = new Data[0];
  
  public static Double getQMC(Object o) {
    int r = getReference(o);
    if (r >= 0) {
      if (o instanceof FluidStack) {
        FluidStack f = (FluidStack) o;
        return new Double(data[r].QMC / 1000 * f.amount);
      }
      return new Double(data[r].QMC);
    }
    return new Double(-1);
  }
  
  public static Boolean add(Object o, Double Value) {
    if (o instanceof Fluid) {
      increase();
      data[data.length - 1] = new Data((Fluid) o, Value);
      return true;
    } else if (o instanceof FluidStack) {
      increase();
      data[data.length - 1] = new Data((Fluid) o, Value / 1000 * ((FluidStack) o).amount);
      return true;
    }
    return false;
  }
  
  public static Boolean blacklist(Object o) {
    int r = getReference(o);
    if (r >= 0) {
      remove(r);
    }
    add(o, -1.0);
    return true;
  }
  
  public static NBTTagCompound snapshot() {
    NBTTagCompound nbt = new NBTTagCompound();
    for (int i = 0; i < data.length; i++) {
      NBTTagCompound o = new NBTTagCompound();
      o.setDouble("QMC", data[i].QMC);
      o.setInteger("fluid", data[i].fluid.getID());
      nbt.setCompoundTag(i + "", o);
    }
    nbt.setInteger("Length", data.length);
    return nbt;
  }
  
  public static void restoreSnapshot(NBTTagCompound nbt) {
    data = new Data[0];
    for (int i = 0; i < nbt.getInteger("Length"); i++) {
      NBTTagCompound o = nbt.getCompoundTag(i + "");
      double value = o.getDouble("QMC");
      int fluid = o.getInteger("fluid");
      add(FluidRegistry.getFluid(fluid), value);
    }
  }
  
  private static void increase() {
    Data[] tmp = new Data[data.length + 1];
    System.arraycopy(data, 0, tmp, 0, data.length);
    data = tmp;
  }
  
  private static void remove(int i) {
    data = ArrayUtils.removeElement(data, data[i]);
  }
  
  public static int getReference(Object o) {
    if (o instanceof Fluid) {
      for (int i = 0; i < data.length; i++) {
        Data check = data[i];
        if (check.fluid.equals((Fluid) o)) {
          return i;
        }
      }
    } else if (o instanceof FluidStack) {
      return getReference(FluidRegistry.getFluid(((FluidStack) o).fluidID));
    }
    return -1;
  }
  
  public static Integer length() {
    return data.length;
  }
}
