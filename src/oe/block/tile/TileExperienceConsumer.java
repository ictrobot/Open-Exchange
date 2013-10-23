package oe.block.tile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import oe.api.OETileInterface;
import oe.api.OE_API;
import oe.api.lib.OEType;
import oe.helper.ConfigHelper;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileExperienceConsumer extends TileEntity implements OETileInterface {
  public double stored;
  public int factor;
  
  public TileExperienceConsumer() {
    super();
    ConfigHelper.load();
    factor = ConfigHelper.other("QMC", "XP to QMC Factor", 10);
    ConfigHelper.save();
  }
  
  @Override
  public void onInventoryChanged() {
    sendChangeToClients();
  }
  
  @Override
  public void updateEntity() {
    double pStored = stored;
    stored = OE_API.provide(xCoord, yCoord, zCoord, worldObj, stored);
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
  
  public void sendChangeToClients() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(14);
      outputStream.writeInt(this.xCoord);
      outputStream.writeInt(this.yCoord);
      outputStream.writeInt(this.zCoord);
      outputStream.writeDouble(this.stored);
    } catch (Exception ex) {
      ex.printStackTrace();
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
    stored = value;
    onInventoryChanged();
  }
  
  @Override
  public void increaseQMC(double value) {
    stored = stored + value;
    onInventoryChanged();
  }
  
  @Override
  public void decreaseQMC(double value) {
    stored = stored - value;
    onInventoryChanged();
  }
  
  @Override
  public int getMaxQMC() {
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
  
  @Override
  public void isOE() {
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