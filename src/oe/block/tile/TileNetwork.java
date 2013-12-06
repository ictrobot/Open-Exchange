package oe.block.tile;

import net.minecraft.nbt.NBTTagCompound;

public interface TileNetwork {
  
  public NBTTagCompound networkSnapshot();
  
  public void restoreSnapshot(NBTTagCompound nbt);
}
