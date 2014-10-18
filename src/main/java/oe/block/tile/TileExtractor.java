package oe.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.core.data.TileSync.ServerNetworkedTile;
import oe.core.util.Util;
import oe.qmc.InWorldQMC;
import oe.qmc.QMC;

public class TileExtractor extends TileEntity implements ServerNetworkedTile, OETileInterface, IInventory, IFluidHandler {
  public ItemStack[] chestContents;
  public final int size = 27;
  public double stored;
  public int percent = 0; // CLIENT SIDE
  private FluidStack fluid;
  private boolean working = false;

  public TileExtractor() {
    super();
    this.chestContents = new ItemStack[getSizeInventory()];
  }

  @Override
  public void updateEntity() {
    if (Util.isServerSide()) {
      if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) == 0) {
        boolean tmpWorking = false;
        if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) == 0) {
          // ITEMS
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
            tmpWorking = true;
          }

          // FLUID
          if (fluid != null) {
            double fluidQMC = QMC.getQMC(fluid);
            if (fluidQMC > 0) {
              fluid = null;
              stored = stored + fluidQMC;
            }
          }
          stored = InWorldQMC.provide(xCoord, yCoord, zCoord, worldObj, stored);
        }
        if (tmpWorking != working) {
          working = tmpWorking;
          if (working) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 7);
          } else {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 7);
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
    if (TagCompound.hasKey("Fluid")) {
      fluid = FluidStack.loadFluidStackFromNBT(TagCompound.getCompoundTag("Fluid"));
    }
    stored = TagCompound.getDouble("OE_Stored_Value");
  }

  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
    NBTTagList itemsNBT = new NBTTagList();
    for (int i = 0; i < chestContents.length; i++) {
      if (chestContents[i] != null) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte) i);
        chestContents[i].writeToNBT(nbttagcompound1);
        itemsNBT.appendTag(nbttagcompound1);
      }
    }
    TagCompound.setTag("Items", itemsNBT);
    if (fluid != null) {
      TagCompound.setTag("Fluid", fluid.writeToNBT(new NBTTagCompound()));
    }
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
    return 1000000;
  }

  @Override
  public int getTier() {
    return 2;
  }

  @Override
  public OEType getType() {
    return OEType.Producer;
  }

  // Fluids
  private final int capacity = 2048;

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    if (resource == null || QMC.getQMC(resource) < 0) {
      return 0;
    }

    if (!doFill) {
      if (fluid == null) {
        return Math.min(capacity, resource.amount);
      }

      if (!fluid.isFluidEqual(resource)) {
        return 0;
      }

      return Math.min(capacity - fluid.amount, resource.amount);
    }

    if (fluid == null) {
      fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
      return fluid.amount;
    }

    if (!fluid.isFluidEqual(resource)) {
      return 0;
    }
    int filled = capacity - fluid.amount;

    if (resource.amount < filled) {
      fluid.amount += resource.amount;
      filled = resource.amount;
    } else {
      fluid.amount = capacity;
    }
    return filled;
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    return null;
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    if (fluid != null && QMC.getQMC(fluid) > 0 && this.fluid.fluidID == fluid.getID()) {
      return true;
    }
    return false;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return false;
  }

  @Override
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return new FluidTankInfo[]{new FluidTankInfo(fluid, capacity)};
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
