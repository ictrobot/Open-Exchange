package oe.api.qmc.id;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import oe.api.qmc.ID;
import oe.api.qmc.QMCHandler;
import oe.qmc.QMCFluid;

public class FluidID implements ID {

  public final int id;
  public final NBTTagCompound nbt;

  public FluidID(FluidStack fluidStack) {
    this(fluidStack.fluidID, fluidStack.tag);
  }

  public FluidID(int id, NBTTagCompound nbt) {
    this.id = id;
    this.nbt = nbt;
  }

  @Override
  public NBTTagCompound toNBT() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("id", id);
    if (nbt != null) {
      nbt.setTag("nbt", nbt);
    }
    return nbt;
  }

  public static FluidID fromNBT(NBTTagCompound nbt) {
    NBTTagCompound n = null;
    if (nbt.hasKey("nbt")) {
      n = nbt.getCompoundTag("nbt");
    }
    return new FluidID(nbt.getInteger("id"), n);
  }

  @Override
  public Class<? extends QMCHandler> getQMCHandlerClass() {
    return QMCFluid.class;
  }
}
