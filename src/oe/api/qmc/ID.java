package oe.api.qmc;

import net.minecraft.nbt.NBTBase;

public interface ID {
  public NBTBase toNBT();
  
  public Class<? extends QMCHandler> getQMCHandlerClass();
}
