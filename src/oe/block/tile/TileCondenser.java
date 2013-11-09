package oe.block.tile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.lib.Debug;
import oe.lib.helper.Sided;
import oe.qmc.QMC;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileCondenser extends TileEntity implements IInventory, ISidedInventory, OETileInterface {
  public ItemStack[] chestContents;
  public final int size = 28;
  public double stored;
  private double prevStored;
  private int ticks;
  public boolean hasTarget;
  public int percent = 0; // CLIENT SIDE
  public boolean[] isDifferent = new boolean[size];
  
  public TileCondenser() {
    super();
    this.chestContents = new ItemStack[getSizeInventory()];
  }
  
  @Override
  public void onInventoryChanged() {
    sendChangeToClients();
  }
  
  @Override
  public void updateEntity() {
    updateDifferent();
    if (Sided.isServer()) {
      if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) == 0) {
        if (prevStored != stored) {
          onInventoryChanged();
        }
        ticks++;
        if (ticks > 5) {
          ticks = 1;
          prevStored = stored;
        }
        for (int i = 1; i <= 4; i++) {
          if (getStackInSlot(0) != null) {
            ItemStack target = getStackInSlot(0).copy();
            target.stackSize = 1;
            if (QMC.hasQMC(target)) {
              hasTarget = true;
              onInventoryChanged();
              double V = QMC.getQMC(target);
              if (stored >= V) {
                if (incrTarget()) {
                  stored = stored - V;
                  onInventoryChanged();
                }
              }
            } else {
              hasTarget = false;
              onInventoryChanged();
            }
          }
          updateDifferent();
          
          int slot = ValueSlot();
          if (slot == -1) {
            return;
          }
          ItemStack itemstack = getStackInSlot(slot).copy();
          if (itemstack == null) {
            return;
          }
          double V = QMC.getQMC(itemstack);
          stored = stored + V;
          decrStackSize(slot, 1);
          sendChangeToClients();
        }
      }
    } else {
      if (getStackInSlot(0) != null) {
        ItemStack target = getStackInSlot(0).copy();
        target.stackSize = 1;
        if (QMC.hasQMC(target)) {
          double V = QMC.getQMC(target);
          double per = (double) stored / (double) V;
          per = 100 * per;
          this.percent = (int) per;
          return;
        }
      }
    }
  }
  
  private void updateDifferent() {
    boolean state;
    for (int i = 1; i < size; i++) {
      state = true;
      if (chestContents[i] != null && chestContents[0] != null) {
        if (chestContents[i].itemID == chestContents[0].itemID) {
          if (chestContents[i].getItemDamage() == chestContents[0].getItemDamage()) {
            state = false;
          }
        }
      }
      isDifferent[i] = state;
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
    return "Condenser";
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
  
  public boolean incrTarget() {
    if (chestContents[0] != null) {
      int slot = -1;
      for (int i = 1; i < size; i++) {
        if (chestContents[i] != null) {
          ItemStack tmp = getStackInSlot(i).copy();
          if (!isDifferent[i] && slot == -1 && tmp.getMaxStackSize() > tmp.stackSize && QMC.getQMC(getStackInSlot(i)) + stored <= getMaxQMC()) {
            slot = i;
          }
        }
      }
      if (slot != -1) {
        chestContents[slot].stackSize++;
        onInventoryChanged();
        return true;
      }
      int free = freeSlot();
      if (free != -1) {
        ItemStack tmp = chestContents[0].copy();
        tmp.stackSize = 0;
        setInventorySlotContents(free, tmp);
      }
    }
    return false;
  }
  
  public int freeSlot() {
    for (int i = 1; i < size; i++) {
      if (chestContents[i] == null) {
        return i;
      }
    }
    return -1;
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
  
  private int ValueSlot() {
    if (getStackInSlot(0) != null) {
      ItemStack target = getStackInSlot(0).copy();
      target.stackSize = 1;
      for (int slot = 1; slot < size; slot++) {
        if (getStackInSlot(slot) != null) {
          if (QMC.hasQMC(getStackInSlot(slot)) && isDifferent[slot]) {
            return slot;
          }
        }
      }
    }
    return -1;
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
      outputStream.writeInt(10);
      outputStream.writeInt(this.xCoord);
      outputStream.writeInt(this.yCoord);
      outputStream.writeInt(this.zCoord);
      outputStream.writeDouble(this.stored);
      outputStream.writeBoolean(this.hasTarget);
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
  public int[] getAccessibleSlotsFromSide(int side) {
    if (side == 1) {
      int[] tmp = { 0 };
      return tmp;
      // } else if (side == 0) {
      // return targetCopyExtract();
    } else {
      int[] tmp = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
      return tmp;
    }
  }
  
  @Override
  public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
    if (side == 1 && slot == 0) {
      return true;
    } else if (side == 0) {
      boolean s = false;
      for (int i = 0; i < targetCopyExtract().length; i++) {
        if (targetCopyExtract()[i] == slot) {
          s = true;
        }
      }
      return s;
    } else if (side != 0 && slot > 0 && slot < size) {
      return true;
    }
    return false;
  }
  
  @Override
  public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
    if (side == 1 && slot == 0) {
      return true;
    } else if (side == 0) {
      boolean s = false;
      for (int i = 0; i < targetCopyExtract().length; i++) {
        if (targetCopyExtract()[i] == slot) {
          s = true;
        }
      }
      return s;
    } else if (side != 0 && slot > 0 && slot < size) {
      return true;
    }
    return false;
  }
  
  public int[] targetCopyExtract() {
    int[] tmp = new int[numTargetCopies()];
    if (tmp.length == 0) {
      return new int[] {};
    }
    int l = 0;
    for (int i = 1; i < size; i++) {
      if (!isDifferent[i]) {
        tmp[l] = i;
        l++;
      }
    }
    return tmp;
  }
  
  public int numTargetCopies() {
    int tmp = 0;
    for (int i = 1; i < size; i++) {
      if (!isDifferent[i]) {
        tmp++;
      }
    }
    return tmp;
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
    return 10000000;
  }
  
  @Override
  public int getTier() {
    return 2;
  }
  
  @Override
  public OEType getType() {
    return OEType.Consumer;
  }
  
  @Override
  public void isOE(Object o) {
  }
  
}
