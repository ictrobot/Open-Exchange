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
import oe.core.data.TileSync.ClientNetworkedTile;
import oe.core.data.TileSync.ServerNetworkedTile;
import oe.core.util.Util;
import oe.qmc.InWorldQMC;

public class TileCharging extends TileEntity implements ServerNetworkedTile, ClientNetworkedTile, IInventory, ISidedInventory, OETileInterface {
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
              if (mode) {
                if (oe.getQMC(itemstack) < oe.getMaxQMC(itemstack)) {
                  double amount;
                  if (oe.getQMC(itemstack) + (InWorldQMC.factor * oe.getTier(itemstack)) < oe.getMaxQMC(itemstack)) {
                    amount = InWorldQMC.factor * oe.getTier(itemstack);
                  } else {
                    amount = oe.getMaxQMC(itemstack) - oe.getQMC(itemstack);
                  }
                  if (stored < amount) {
                    amount = stored;
                  }
                  oe.increaseQMC(amount, itemstack);
                  stored = stored - amount;
                } else {
                  moveOutput(slot);
                }
              } else {
                if (oe.getQMC(itemstack) > 0) {
                  double amount;
                  if (oe.getQMC(itemstack) - (InWorldQMC.factor * oe.getTier(itemstack)) > 0) {
                    amount = InWorldQMC.factor * oe.getTier(itemstack);
                  } else {
                    amount = oe.getQMC(itemstack);
                  }
                  if (stored + amount > getMaxQMC()) {
                    amount = getMaxQMC() - stored;
                  }
                  oe.decreaseQMC(amount, itemstack);
                  stored = stored + amount;
                } else {
                  moveOutput(slot);
                }
              }
            }
          }
        }
        if (!mode) {
          stored = InWorldQMC.provide(xCoord, yCoord, zCoord, worldObj, stored);
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
  public ItemStack getStackInSlot(int i) {
    return chestContents[i];
  }

  @Override
  public ItemStack decrStackSize(int slotnum, int x) {
    if (chestContents[slotnum] != null) {
      if (chestContents[slotnum].stackSize <= x) {
        ItemStack itemstack = chestContents[slotnum];
        chestContents[slotnum] = null;
        return itemstack;
      }
      ItemStack itemstack1 = chestContents[slotnum].splitStack(x);
      if (chestContents[slotnum].stackSize == 0) {
        chestContents[slotnum] = null;
      }
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
  }

  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
    NBTTagList TagList = TagCompound.getTagList("Items", chestContents.length);
    chestContents = new ItemStack[getSizeInventory()];
    for (int i = 0; i < TagList.tagCount(); i++) {
      NBTTagCompound nbttagcompound1 = TagList.getCompoundTagAt(i);
      int j = nbttagcompound1.getByte("Slot") & 0xff;
      if (j >= 0 && j < chestContents.length) {
        chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
      }
    }
    stored = TagCompound.getDouble("OE_Stored_Value");
    mode = TagCompound.getBoolean("Mode");
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
    TagCompound.setBoolean("Mode", mode);
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
    if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
      return false;
    }
    return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
    return true;
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
  public NBTTagCompound snapshotServer() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("stored", stored);
    if (modeToClients) {
      nbt.setBoolean("mode", mode);
      modeToClients = false;
    }
    return nbt;
  }

  @Override
  public void restoreClient(NBTTagCompound nbt) {
    stored = nbt.getDouble("stored");
    if (nbt.hasKey("mode")) {
      mode = nbt.getBoolean("mode");
    }
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side) {
    if (side == 1) {
      int[] tmp = {0, 1, 2, 3, 4, 5, 6, 7, 8};
      return tmp;
    } else if (side == 0) {
      int[] tmp = {9, 10, 11, 12, 13, 14, 15, 16, 17};
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
  }

  @Override
  public void increaseQMC(double value) {
    stored = stored + value;
    if (stored > getMaxQMC()) {
      stored = getMaxQMC();
    } else if (stored < 0) {
      stored = 0;
    }
  }

  @Override
  public void decreaseQMC(double value) {
    stored = stored - value;
    if (stored < 0) {
      stored = 0;
    }
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
    if (mode) {
      return OEType.Consumer;
    }
    return OEType.Producer;
  }

  boolean mode = true; // True==Charging, False==Draining
  boolean modeToServer = false;
  boolean modeToClients = false;

  public String getMode() {
    if (mode) {
      return "Charging";
    }
    return "Draining";
  }

  public String toggleMode() {
    modeToServer = true;
    if (mode) {
      mode = false;
    } else {
      mode = true;
    }
    return getMode();
  }

  @Override
  public NBTTagCompound snapshotClient() {
    NBTTagCompound nbt = new NBTTagCompound();
    if (modeToServer) {
      nbt.setBoolean("mode", mode);
      modeToServer = false;
    }
    return nbt;
  }

  @Override
  public void restoreServer(NBTTagCompound nbt) {
    mode = nbt.getBoolean("mode");
    modeToClients = true;
  }

  @Override
  public String getInventoryName() {
    return "container." + this.getClass().getSimpleName().substring(4) + ".name";
  }

  @Override
  public boolean hasCustomInventoryName() {
    return false;
  }

  @Override
  public void openInventory() {

  }

  @Override
  public void closeInventory() {

  }
}
