package oe.api.qmc.id;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import oe.api.qmc.ID;
import oe.api.qmc.QMCHandler;
import oe.qmc.QMCItemStack;

public class OreDictionaryID implements ID {

  public final String ore;

  public OreDictionaryID(String ore) {
    this.ore = ore;
  }

  @Override
  public NBTBase toNBT() {
    return new NBTTagString(ore);
  }

  @Override
  public Class<? extends QMCHandler> getQMCHandlerClass() {
    return QMCItemStack.class;
  }

  public int hashCode() {
    return ore.hashCode();
  }

  public boolean equals(Object o) {
    if (o instanceof OreDictionaryID) {
      return ((OreDictionaryID) o).ore == this.ore;
    }
    return false;
  }

  public String toString() {
    return ore;
  }

}
