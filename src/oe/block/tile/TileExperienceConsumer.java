package oe.block.tile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.core.data.TileSync.ServerNetworkedTile;
import oe.core.util.ConfigUtil;
import oe.qmc.InWorldQMC;

public class TileExperienceConsumer extends TileEntity implements ServerNetworkedTile, OETileInterface {
  public double stored;
  public int factor;
  
  public TileExperienceConsumer() {
    super();
    ConfigUtil.load();
    factor = ConfigUtil.other("QMC", "XP to QMC Factor", 10);
    ConfigUtil.save();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void updateEntity() {
    double pStored = stored;
    stored = InWorldQMC.provide(xCoord, yCoord, zCoord, worldObj, stored);
    if (pStored != stored) {
      onInventoryChanged();
    }
    Block block = Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
    AxisAlignedBB boundingBox = block.getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
    int radius = 2;
    List<EntityXPOrb> list = worldObj.getEntitiesWithinAABB(EntityXPOrb.class, boundingBox.expand(radius, radius, radius));
    for (EntityXPOrb xp : list) {
      if (xp.xpOrbAge >= 20) {
        increaseQMC(xp.getXpValue() * factor);
        worldObj.removeEntity(xp);
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
    return 1000000000;
  }
  
  @Override
  public int getTier() {
    return 2;
  }
  
  @Override
  public OEType getType() {
    return OEType.Producer;
  }
  
  public void onClick(EntityPlayer player) {
    int Level = player.experienceLevel;
    int xp = 0;
    
    if (Level == 0) {
      return;
    } else if (Level <= 16) {
      xp = 17;
    } else if (Level <= 30) {
      xp = (3 * (Level - 16)) + 17;
    } else {
      xp = (7 * (Level - 30)) + 55;
    }
    
    xp = xp * 10;
    if (stored + xp <= getMaxQMC()) {
      player.addExperienceLevel(-1);
      increaseQMC(xp);
    }
  }
}
