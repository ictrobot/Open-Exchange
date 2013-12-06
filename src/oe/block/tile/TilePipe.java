package oe.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import oe.api.OEPipeInterface;

public class TilePipe extends TileEntity implements TileNetwork, OEPipeInterface {
  public double passThrough;
  
  public TilePipe() {
    super();
  }
  
  @Override
  public void updateEntity() {
    onInventoryChanged();
    passThrough = 0;
  }
  
  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
  }
  
  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
  }
  
  @Override
  public NBTTagCompound networkSnapshot() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("passThrough", passThrough);
    return nbt;
  }
  
  @Override
  public void restoreSnapshot(NBTTagCompound nbt) {
    passThrough = nbt.getDouble("passThrough");
  }
  
  @Override
  public double getMaxQMC() {
    return 100;
  }
  
  public void onClick(EntityPlayer player) {
    
  }
  
  @Override
  public double increasePassThrough(double amount) {
    passThrough = passThrough + amount;
    if (passThrough > getMaxQMC()) {
      passThrough = getMaxQMC();
    }
    return passThrough;
  }
  
  @Override
  public double passThroughLeft() {
    return getMaxQMC() - passThrough;
  }
  
}
