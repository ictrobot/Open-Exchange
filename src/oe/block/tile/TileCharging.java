package oe.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import oe.api.OE;
import oe.api.OEItemInterface;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.core.util.Util;

public class TileCharging extends TileEntity implements TileNetwork, IInventory, ISidedInventory, OETileInterface {
  public ItemStack[] chestContents;
  public final int size = 18; // 9 Input, 9 Output
  public double stored;
  public int percent;
  
  public TileCharging() {
    super();
    this.chestContents = new ItemStack[getSizeInventory()];
  }
  
  @Override
  public void updateEntity() {
    if (Util.isServerSide()) {
      if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) == 0) {
        for (int slot = 0; slot <= 8; slot++) {
          if (getStackInSlot(slot) != null) {
            if (OE.isOE(getStackInSlot(slot).getItem().getClass())) {
              ItemStack itemstack = getStackInSlot(slot);
              OEItemInterface oe = (OEItemInterface) getStackInSlot(slot).getItem();
              for (int i = 1; i < 6; i++) {
                if (oe.getQMC(itemstack) < oe.getMaxQMC()) {
                  double amount;
                  if (oe.getQMC(itemstack) + 1 < oe.getMaxQMC()) {
                    amount = 1;
                  } else {
                    amount = oe.getMaxQMC() - oe.getQMC(itemstack);
                  }
                  if (stored > amount) {
                    oe.increaseQMC(amount, itemstack);
                    stored = stored - amount;
                  }
                } else {
                  moveOutput(slot);
                }
              }
            }
          }
        }
      }
    } else {
      double per = stored / getMaxQMC();
      per = 100 * per;
      this.percent = (int) per;
      return;
    }
  }
  
  private boolean moveOutput(int slot) {
    int freeS = -1;
    for (int s = 9; s <= 17; s++) {
      if (getStackInSlot(s) == null) {
        freeS = s;
        break;
      }
    }
    if (freeS != -1) {
      chestContents[freeS] = chestContents[slot];
      chestContents[slot] = null;
      return true;
    }
    return false;
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
    return "container." + this.getClass().getSimpleName().substring(4) + ".name";
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
    return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
  }
  
  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
    return true;
  }
  
  @Override
  public boolean isInvNameLocalized() {
    return false;
  }
  
  @Override
  public ItemStack getStackInSlotOnClosing(int par1) {
    if (this.chestContents[par1] != null) {
      ItemStack var2 = this.chestContents[par1];
      this.chestContents[par1] = null;
      return var2;
    } else {
      return null;
    }
  }
  
  @Override
  public NBTTagCompound networkSnapshot() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("stored", stored);
    return nbt;
  }
  
  @Override
  public void restoreSnapshot(NBTTagCompound nbt) {
    stored = nbt.getDouble("stored");
  }
  
  @Override
  public int[] getAccessibleSlotsFromSide(int side) {
    if (side == 1) {
      int[] tmp = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
      return tmp;
    } else if (side == 0) {
      int[] tmp = { 9, 10, 11, 12, 13, 14, 15, 16, 17 };
      return tmp;
    } else {
      int[] tmp = {};
      return tmp;
    }
  }
  
  @Override
  public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
    if (side == 1 && slot >= 8) {
      return true;
    } else if (side == 0 && slot <= 9) {
      return true;
    }
    return false;
  }
  
  @Override
  public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
    if (side == 1 && slot >= 8) {
      return true;
    } else if (side == 0 && slot <= 9) {
      return true;
    }
    return false;
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
