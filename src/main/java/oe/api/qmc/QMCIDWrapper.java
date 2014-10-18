package oe.api.qmc;

import net.minecraft.nbt.NBTTagCompound;
import oe.qmc.QMC;

public final class QMCIDWrapper {
  public final ID id;
  public final QMCHandler handler;

  public QMCIDWrapper(ID id, QMCHandler handler) {
    this.id = id;
    this.handler = handler;
  }

  public QMCIDWrapper(NBTTagCompound nbt) {
    this.handler = QMC.getHandlerFromString(nbt.getString("handler"));
    this.id = handler.getIDFromNBT(nbt.getCompoundTag("id"));
  }

  public NBTTagCompound getNBT() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setTag("id", id.toNBT());
    nbt.setString("handler", handler.getClass().getSimpleName());
    return nbt;
  }

  public String toString() {
    return handler.getClass().getSimpleName() + ":" + id.toString();
  }

  public int hashCode() {
    return id.hashCode();
  }

  public boolean equals(Object o) {
    if (o instanceof QMCIDWrapper) {
      return id.equals(((QMCIDWrapper) o).id);
    }
    return false;
  }
}
