package oe.block.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.lib.util.RemoteDrillData;

public class TileDrillRemoteReceiver extends TileEntity implements OETileInterface {
  public int RemoteID;
  
  public TileDrillRemoteReceiver() {
    super();
  }
  
  @Override
  public void onInventoryChanged() {
    
  }
  
  @Override
  public void updateEntity() {
    ItemStack[] stacks = RemoteDrillData.getItemStacks(RemoteID);
    if (stacks != null && stacks.length > 0) {
      for (ItemStack stack : stacks) {
        EntityItem entityitem = new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, stack);
        worldObj.spawnEntityInWorld(entityitem);
      }
    }
  }
  
  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
    RemoteID = TagCompound.getInteger("OE_Drill_ID");
  }
  
  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
    TagCompound.setInteger("OE_Drill_ID", RemoteID);
  }
  
  @Override
  public double getQMC() {
    return RemoteDrillData.getQMC(RemoteID);
  }
  
  @Override
  public void setQMC(double value) {
    if (value > getMaxQMC()) {
      RemoteDrillData.setQMC(RemoteID, getMaxQMC());
    } else if (value < 0) {
      RemoteDrillData.setQMC(RemoteID, 0);
    } else {
      RemoteDrillData.setQMC(RemoteID, value);
    }
  }
  
  @Override
  public void increaseQMC(double value) {
    RemoteDrillData.setQMC(RemoteID, RemoteDrillData.getQMC(RemoteID) + value);
    if (RemoteDrillData.getQMC(RemoteID) > getMaxQMC()) {
      RemoteDrillData.setQMC(RemoteID, getMaxQMC());
    } else if (RemoteDrillData.getQMC(RemoteID) < 0) {
      RemoteDrillData.setQMC(RemoteID, 0);
    }
    onInventoryChanged();
  }
  
  @Override
  public void decreaseQMC(double value) {
    RemoteDrillData.setQMC(RemoteID, RemoteDrillData.getQMC(RemoteID) - value);
    if (RemoteDrillData.getQMC(RemoteID) < 0) {
      RemoteDrillData.setQMC(RemoteID, 0);
    }
    onInventoryChanged();
  }
  
  @Override
  public double getMaxQMC() {
    return 10000;
  }
  
  @Override
  public int getTier() {
    return 2;
  }
  
  @Override
  public OEType getType() {
    return OEType.Consumer;
  }
}
