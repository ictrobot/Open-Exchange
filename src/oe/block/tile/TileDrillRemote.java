package oe.block.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.lib.Location;
import oe.block.BlockIDs;
import oe.lib.misc.RemoteDrillData;
import oe.lib.util.Util;

public class TileDrillRemote extends TileEntity {
  int delayTicks = 100;
  int currTicks = 0;
  public int RemoteID = -1;
  public int direction = 2;
  boolean spawnedReceiver = false;
  
  public TileDrillRemote() {
    super();
  }
  
  @Override
  public void updateEntity() {
    if (RemoteID == -1) {
      RemoteID = RemoteDrillData.getNewID();
      if (!spawnedReceiver) {
        TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
        if (te != null && te instanceof TileDrillRemoteReceiver) {
          TileDrillRemoteReceiver receiver = (TileDrillRemoteReceiver) te;
          receiver.RemoteID = RemoteID;
        }
      }
    }
    drill();
  }
  
  private void drill() {
    if (Util.isServer()) {
      RemoteDrillData.setLocationDrill(RemoteID, new Location(xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
      currTicks++;
      if (RemoteDrillData.getQMC(RemoteID) < RemoteDrillData.mineCost || !RemoteDrillData.isReady(RemoteID)) {
        return;
      }
      if (currTicks >= delayTicks) {
        currTicks = 0;
      } else {
        return;
      }
      for (int y = 1; y < yCoord; y++) {
        for (int x = xCoord - RemoteDrillData.range; x <= (xCoord + RemoteDrillData.range); x++) {
          for (int z = zCoord - RemoteDrillData.range; z <= (zCoord + RemoteDrillData.range); z++) {
            int id = worldObj.getBlockId(x, y, z);
            if (id != 0) {
              Block block = Block.blocksList[id];
              int meta = worldObj.getBlockMetadata(x, y, z);
              int oreID = OreDictionary.getOreID(new ItemStack(id, 1, meta));
              if (oreID != -1) {
                if (OreDictionary.getOreName(oreID).toLowerCase().startsWith("ore")) {
                  Object[] drops = block.getBlockDropped(worldObj, x, y, x, meta, 0).toArray();
                  for (Object o : drops) {
                    if (o != null && o instanceof ItemStack) {
                      RemoteDrillData.addItemStack(RemoteID, (ItemStack) o);
                    }
                  }
                  worldObj.setBlockToAir(x, y, z);
                  RemoteDrillData.setQMC(RemoteID, RemoteDrillData.getQMC(RemoteID) - RemoteDrillData.mineCost);
                  return;
                }
              }
            }
          }
        }
      }
      // No More Ores
      if (RemoteDrillData.getQMC(RemoteID) < RemoteDrillData.moveCost || !RemoteDrillData.isReady(RemoteID)) {
        return;
      }
      int targetX = xCoord;
      int targetZ = zCoord;
      switch (direction) {
        case 0:
          targetZ = targetZ + RemoteDrillData.rangeX2;
          break;
        case 1:
          targetX = targetX - RemoteDrillData.rangeX2;
          break;
        case 2:
          targetZ = targetZ - RemoteDrillData.rangeX2;
          break;
        case 3:
          targetX = targetX + RemoteDrillData.rangeX2;
          break;
      }
      worldObj.setBlock(targetX, yCoord, targetZ, BlockIDs.drillRemote);
      TileEntity te = worldObj.getBlockTileEntity(targetX, yCoord, targetZ);
      TileDrillRemote drill = (TileDrillRemote) te;
      drill.spawnedReceiver = true;
      drill.RemoteID = this.RemoteID;
      drill.direction = this.direction;
      worldObj.setBlockToAir(xCoord, yCoord, zCoord);
      this.invalidate();
      RemoteDrillData.setQMC(RemoteID, RemoteDrillData.getQMC(RemoteID) - RemoteDrillData.moveCost);
    }
  }
  
  @Override
  public void onInventoryChanged() {
  }
  
  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
    RemoteID = TagCompound.getInteger("OE_Drill_ID");
    direction = TagCompound.getInteger("OE_Drill_Direction");
    spawnedReceiver = TagCompound.getBoolean("OE_Drill_SpawnedReceiver");
  }
  
  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
    TagCompound.setInteger("OE_Drill_ID", RemoteID);
    TagCompound.setInteger("OE_Drill_Direction", direction);
    TagCompound.setBoolean("OE_Drill_SpawnedReceiver", spawnedReceiver);
  }
  
  public String changeDirection() {
    direction++;
    if (direction > 3) {
      direction = 0;
    }
    return getDirection();
  }
  
  public String getDirection() {
    switch (direction) {
      case 0:
        return "South Z+";
      case 1:
        return "West X-";
      case 2:
        return "North Z-";
      case 3:
        return "East X+";
    }
    return "";
  }
}
