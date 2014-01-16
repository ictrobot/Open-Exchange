package oe.qmc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import oe.api.QMCHandler;

public class QMCFluid extends QMCHandler {
  
  public QMCFluid() {
    super(new Class<?>[] { Fluid.class, FluidStack.class });
  }
  
  public double decode(Object o, NBTTagCompound nbt) {
    double qmc = nbt.getDouble("qmc");
    if (o instanceof FluidStack) {
      qmc = qmc / 1000 * ((FluidStack) o).amount;
    }
    return qmc;
  }
  
  public NBTTagCompound getID(Object o) {
    NBTTagCompound nbt = new NBTTagCompound();
    if (o instanceof Fluid) {
      nbt.setInteger("ID", ((Fluid) o).getID());
    } else if (o instanceof FluidStack) {
      nbt.setInteger("ID", ((FluidStack) o).fluidID);
      if (((FluidStack) o).tag != null && !((FluidStack) o).tag.hasNoTags()) {
        nbt.setCompoundTag("tag", ((FluidStack) o).tag);
      }
    }
    return nbt;
  }
}
