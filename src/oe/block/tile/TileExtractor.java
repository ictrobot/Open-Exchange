package oe.block.tile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import oe.api.OETileInterface;
import oe.api.OE_API;
import oe.api.lib.OEType;
import oe.lib.Debug;
import oe.lib.helper.Sided;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileExtractor extends TileEntity implements IInventory, OETileInterface {
  public ItemStack[] chestContents;
  public final int size = 27;
  public double stored;
  public int percent = 0; // CLIENT SIDE
  boolean working = false;
  
  public TileExtractor() {
    super();
    this.chestContents = new ItemStack[getSizeInventory()];
  }
  
  @Override
  public void onInventoryChanged() {
    sendChangeToClients();
  }
  
  @Override
  public void updateEntity() {
    if (Sided.isServer()) {
      boolean tmpWorking = false;
      if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) == 0) {
        for (int i = 1; i <= 4; i++) {
          int slot = -1;
          for (int s = 0; s < size; s++) {
            if (getStackInSlot(s) != null && slot == -1 && QMC.hasQMC(getStackInSlot(s)) && QMC.getQMC(getStackInSlot(s).copy()) + stored <= getMaxQMC()) {
              slot = s;
              break;
            }
          }
          if (slot == -1) {
            break;
          }
          ItemStack itemstack = getStackInSlot(slot).copy();
          if (itemstack == null) {
            break;
          }
          double V = QMC.getQMC(itemstack);
          stored = stored + V;
          decrStackSize(slot, 1);
          sendChangeToClients();
          tmpWorking = true;
        }
        stored = OE_API.provide(xCoord, yCoord, zCoord, worldObj, stored);
        sendChangeToClients();
      }
      if (tmpWorking != working) {
        working = tmpWorking;
        if (working) {
          worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 7);
        } else {
          worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 7);
        }
      }
    } else {
      double per = (double) stored / (double) getMaxQMC();
      per = 100 * per;
      this.percent = (int) per;
      return;
    }
  }
  
  public ItemStack[] getContents() {
    return chestContents;
  }
  
  @Override
  public int getSizeInventory() {
    return size;
  }
  
  @Override
  public String getInvName() {
    return "Extractor";
  }
  
  @Override
  public ItemStack getStackInSlot(int i) {
    return chestContents[i];
  }
  
  @Override
  public void closeChest() {
    onInventoryChanged();
  }
  
  @Override
  public void openChest() {
    onInventoryChanged();
  }
  
  @Override
  public ItemStack decrStackSize(int slotnum, int x) {
    if (chestContents[slotnum] != null) {
      if (chestContents[slotnum].stackSize <= x) {
        ItemStack itemstack = chestContents[slotnum];
        chestContents[slotnum] = null;
        onInventoryChanged();
        return itemstack;
      }
      ItemStack itemstack1 = chestContents[slotnum].splitStack(x);
      if (chestContents[slotnum].stackSize == 0) {
        chestContents[slotnum] = null;
      }
      onInventoryChanged();
      return itemstack1;
    } else {
      return null;
    }
  }
  
  @Override
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    chestContents[i] = itemstack;
    if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
      itemstack.stackSize = getInventoryStackLimit();
    }
    onInventoryChanged();
  }
  
  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
    NBTTagList TagList = TagCompound.getTagList("Items");
    chestContents = new ItemStack[getSizeInventory()];
    for (int i = 0; i < TagList.tagCount(); i++) {
      NBTTagCompound nbttagcompound1 = (NBTTagCompound) TagList.tagAt(i);
      int j = nbttagcompound1.getByte("Slot") & 0xff;
      if (j >= 0 && j < chestContents.length) {
        chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
      }
    }
    stored = TagCompound.getDouble("OE_Stored_Value");
  }
  
  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
    NBTTagList TagList = new NBTTagList();
    for (int i = 0; i < chestContents.length; i++) {
      if (chestContents[i] != null) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte) i);
        chestContents[i].writeToNBT(nbttagcompound1);
        TagList.appendTag(nbttagcompound1);
      }
    }
    TagCompound.setTag("Items", TagList);
    TagCompound.setDouble("OE_Stored_Value", stored);
  }
  
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  
  @Override
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    if (worldObj == null) {
      return true;
    }
    if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
      return false;
    }
    return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
  }
  
  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
    return true;
  }
  
  @Override
  public boolean isInvNameLocalized() {
    return false;
  }
  
  public ItemStack getStackInSlotOnClosing(int par1) {
    if (this.chestContents[par1] != null) {
      ItemStack var2 = this.chestContents[par1];
      this.chestContents[par1] = null;
      return var2;
    } else {
      return null;
    }
  }
  
  public void sendChangeToClients() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
    DataOutputStream outputStream = new DataOutputStream(bos);
    try {
      outputStream.writeInt(12);
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
    return 10000000;
  }
  
  @Override
  public int getTier() {
    return 2;
  }
  
  @Override
  public OEType getType() {
    return OEType.Producer;
  }
}
