package oe.block.tile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.lib.Debug;
import oe.lib.helper.ConfigHelper;
import oe.lib.helper.Sided;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileDrill extends TileEntity implements OETileInterface {
  public double stored;
  int facing;
  double mineCost = 256;
  int range = 8;
  int delayTicks = 100;
  int currTicks = 0;
  
  public TileDrill() {
    super();
    ConfigHelper.load();
    mineCost = ConfigHelper.other("block", "Drill Mine Block Cost", 256.0);
    delayTicks = ConfigHelper.other("block", "Drill Tick Delay", 100);
    range = ConfigHelper.other("block", "Drill Range", 8);
    ConfigHelper.save();
  }
  
  @Override
  public void updateEntity() {
    double pStored = stored;
    drill();
    if (pStored != stored) {
      onInventoryChanged();
    }
  }
  
  private void drill() {
    if (Sided.isServer()) {
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
  public void onInventoryChanged() {
    sendChangeToClients();
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
  
  public void sendChangeToClients() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(15);
      outputStream.writeInt(this.xCoord);
      outputStream.writeInt(this.yCoord);
      outputStream.writeInt(this.zCoord);
      outputStream.writeDouble(this.stored);
    } catch (Exception ex) {
      Debug.handleException(ex);
    }
    
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "oe";
    packet.data = bos.toByteArray();
    packet.length = bos.size();
    
    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 64, worldObj.provider.dimensionId, packet);
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
