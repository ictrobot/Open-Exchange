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
import oe.lib.Debug;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileTransfer extends TileEntity implements OETileInterface {
  public double stored;
  public int tier;
  
  public TileTransfer(int Tier) {
    super();
    tier = Tier;
  }
  
  public TileTransfer() {
    super();
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
    tier = TagCompound.getInteger("OE_Tier");
  }
  
  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
    TagCompound.setDouble("OE_Stored_Value", stored);
    TagCompound.setInteger("OE_Tier", tier);
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
    return tier;
  }
  
  @Override
  public OEType getType() {
    boolean target = OE_API.hasTarget(xCoord, yCoord, zCoord, worldObj);
    OEType type;
    if (target) {
      type = OEType.Storage;
    } else {
      type = OEType.None;
    }
    return type;
  }
  
  @Override
  public void isOE(Object o) {
  }
  
  public void onClick(EntityPlayer player) {
    onInventoryChanged();
  }
}
