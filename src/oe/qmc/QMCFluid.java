package oe.qmc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import oe.api.QMCHandler;
import oe.qmc.QMC.Data;
import oe.qmc.QMC.DataNormal;
import oe.qmc.QMC.ID;

public class QMCFluid extends QMCHandler {
  
  public static class FluidID implements ID {
    
    public final FluidStack fluidStack;
    
    public FluidID(FluidStack fluidStack) {
      fluidStack.amount = 1000;
      this.fluidStack = fluidStack;
    }
    
    @Override
    public NBTTagCompound toNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setTag("stack", fluidStack.writeToNBT(new NBTTagCompound()));
      return nbt;
    }
    
    @Override
    public Class<? extends QMCHandler> getQMCHandlerClass() {
      return QMCFluid.class;
    }
  }
  
  public QMCFluid() {
    super(new Class<?>[] { FluidStack.class });
  }
  
  public ID getID(Object o) {
    return new FluidID((FluidStack) o);
  }
  
  @Override
  public ID getIDFromNBT(NBTTagCompound nbt) {
    return new FluidID(FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("stack")));
  }
  
  @Override
  public Data getData(double qmc, ID id, Object o) {
    FluidStack f = (FluidStack) o;
    qmc = qmc / f.amount * 1000;
    return new DataNormal(qmc);
  }
  
  @Override
  public Data getData(NBTTagCompound nbt) {
    return DataNormal.getData(nbt);
  }
  
  @Override
  public double getQMC(Data data, ID id, Object o) {
    double qmc = ((DataNormal) data).qmc;
    qmc = qmc / 1000 * ((FluidStack) o).amount;
    return qmc;
  }
}
