package oe.qmc;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import oe.api.qmc.Data;
import oe.api.qmc.ID;
import oe.api.qmc.QMCHandler;
import oe.api.qmc.SimpleData;

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
    return new SimpleData(qmc);
  }
  
  @Override
  public Data getData(NBTBase nbt) {
    return SimpleData.getData(nbt);
  }
  
  @Override
  public double getQMC(Data data, ID id, Object o) {
    double qmc = ((SimpleData) data).qmc;
    qmc = qmc / 1000 * ((FluidStack) o).amount;
    return qmc;
  }
}
