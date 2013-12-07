package oe.block.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.core.data.TileSync.ServerNetworkedTile;
import oe.core.util.ConfigUtil;
import oe.core.util.Util;

public class TileDrill extends TileEntity implements ServerNetworkedTile, OETileInterface {
  public double stored;
  int facing;
  double mineCost = 256;
  int range = 8;
  int delayTicks = 100;
  int currTicks = 0;
  
  public TileDrill() {
    super();
    ConfigUtil.load();
    mineCost = ConfigUtil.other("block", "Drill Mine Block Cost", 256.0);
    delayTicks = ConfigUtil.other("block", "Drill Tick Delay", 100);
    range = ConfigUtil.other("block", "Drill Range", 8);
    ConfigUtil.save();
  }
  
  @Override
  public void updateEntity() {
    drill();
  }
  
  private void drill() {
    if (Util.isServerSide()) {
      currTicks++;
      if (getQMC() < mineCost) {
        return;
      }
      if (currTicks >= delayTicks) {
        currTicks = 0;
      } else {
        return;
      }
      for (int y = 1; y < yCoord; y++) {
        for (int x = xCoord - range; x <= (xCoord + range); x++) {
          for (int z = zCoord - range; z <= (zCoord + range); z++) {
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
                      EntityItem entityitem = new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, (ItemStack) o);
                      worldObj.spawnEntityInWorld(entityitem);
                    }
                  }
                  worldObj.setBlockToAir(x, y, z);
                  decreaseQMC(mineCost);
                  return;
                }
              }
            }
          }
        }
      }
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
    return 5000;
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
