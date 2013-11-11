package oe.block.tile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import oe.api.OEPipeInterface;
import oe.lib.Debug;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePipe extends TileEntity implements OEPipeInterface {
  public double passThrough;
  
  public TilePipe() {
    super();
  }
  
  @Override
  public void onInventoryChanged() {
    sendChangeToClients();
  }
  
  @Override
  public void updateEntity() {
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
  
  public void sendChangeToClients() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(14);
      outputStream.writeInt(this.xCoord);
      outputStream.writeInt(this.yCoord);
      outputStream.writeInt(this.zCoord);
      outputStream.writeDouble(this.passThrough);
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
