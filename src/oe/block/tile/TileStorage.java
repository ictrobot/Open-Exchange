package oe.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.core.data.TileSync.ServerNetworkedTile;
import oe.qmc.InWorldQMC;

public class TileStorage extends TileEntity implements ServerNetworkedTile, OETileInterface {
  public double stored;
  
  public TileStorage() {
    super();
  }
  
  @Override
  public void updateEntity() {
    double pStored = stored;
    stored = InWorldQMC.provide(xCoord, yCoord, zCoord, worldObj, stored);
    if (pStored != stored) {
      onInventoryChanged();
    }
  }
  
  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
    stored = TagCompound.getDouble("OE_Stored_Value");
  }
  
  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
    TagCompound.setDouble("OE_Stored_Value", stored);
  }
  
  @Override
  public NBTTagCompound snapshotServer() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("stored", stored);
    return nbt;
  }
  
  @Override
  public void restoreClient(NBTTagCompound nbt) {
    stored = nbt.getDouble("stored");
  }
  
  @Override
  public double getQMC() {
    return stored;
  }
  
  @Override
  public void setQMC(double value) {
    if (value > getMaxQMC()) {
      stored = getMaxQMC();
    } else if (stored < 0) {
      stored = 0;
    } else {
      stored = value;
    }
    onInventoryChanged();
  }
  
  @Override
  public void increaseQMC(double value) {
    stored = stored + value;
    if (stored > getMaxQMC()) {
      stored = getMaxQMC();
    } else if (stored < 0) {
      stored = 0;
    }
    onInventoryChanged();
  }
  
  @Override
  public void decreaseQMC(double value) {
    stored = stored - value;
    if (stored < 0) {
      stored = 0;
    }
    onInventoryChanged();
  }
  
  @Override
  public double getMaxQMC() {
    return 1000000000;
  }
  
  @Override
  public int getTier() {
    return 2;
  }
  
  @Override
  public OEType getType() {
    return OEType.Storage;
  }
  
  public void onClick(EntityPlayer player) {
    
  }
  
}
