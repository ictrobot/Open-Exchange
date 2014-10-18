package oe.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
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
import oe.core.util.FluidUtil;
import oe.core.util.ItemStackUtil;
import oe.core.util.Util;
import oe.qmc.QMC;

public class TileCondenser extends TileEntity implements ServerNetworkedTile, IInventory, ISidedInventory, OETileInterface, IFluidHandler {
  public ItemStack[] chestContents;
  public final int size = 28;
  public double stored;
  public boolean hasTarget;
  public int percent = 0; // CLIENT SIDE
  public boolean[] isDifferent = new boolean[size];
  public boolean finished = false;
  public FluidStack fluidTarget;
  public FluidStack fluidStored;
  private boolean fluidBehaviour = true;

  public TileCondenser() {
    super();
    this.chestContents = new ItemStack[getSizeInventory()];
  }

  public String getFluidBehaviour() {
    if (fluidBehaviour) {
      return "Pump out Fluids";
    } else {
      return "Condense Fluid Containing Item";
    }
  }

  public String toggleFluidBehaviour() {
    if (fluidBehaviour) {
      fluidBehaviour = false;
    } else {
      fluidBehaviour = true;
    }
    return getFluidBehaviour();
  }

  @Override
  public void updateEntity() {
    updateDifferent();
    if (Util.isServerSide()) {
      if (worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) == 0) {
        for (int i = 1; i <= 4; i++) {
          fluidTarget = FluidUtil.getFluidStack(getStackInSlot(0));
          if (fluidTarget != null && fluidBehaviour) {
            FluidStack toAdd = new FluidStack(fluidTarget.fluidID, 25);
            if (fluidStored == null || fluidTarget.fluidID == fluidStored.fluidID) {
              finished = false;
              double V = QMC.getQMC(toAdd);
              if (stored >= V) {
                if (fluidStored == null || toAdd.amount + fluidStored.amount <= capacity) {
                  if (fluidStored == null) {
                    fluidStored = toAdd.copy();
                  } else {
                    fluidStored.amount += toAdd.amount;
                  }
                  stored = stored - V;
                }
              }
            }
          } else {
            if (getStackInSlot(0) != null) {
              ItemStack target = getStackInSlot(0);
              target.stackSize = 1;
              double V = QMC.getQMC(target);
              if (V > 0) {
                hasTarget = true;
                if (stored >= V) {
                  if (incrTarget()) {
                    stored = stored - V;
                    finished = false;
                  } else {
                    finished = true;
                  }
                }
              } else {
                hasTarget = false;
              }
            }
          }
        }
      }
    } else {
      fluidTarget = FluidUtil.getFluidStack(getStackInSlot(0));
      if (fluidTarget != null && fluidBehaviour) {
        this.percent = -1;
      } else {
        if (getStackInSlot(0) != null) {
          double V = QMC.getQMC(getStackInSlot(0));
          if (V > 0) {
            double per = stored / V;
            per = 100 * per;
            this.percent = (int) per;
            return;
          }
        } else {
          double per = stored / getMaxQMC();
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
      if (ItemStackUtil.equals(chestContents[i], chestContents[0])) {
        state = false;
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

  public boolean incrTarget() {
    if (chestContents[0] != null) {
      int slot = -1;
      for (int i = 1; i < size; i++) {
        if (chestContents[i] != null) {
          ItemStack tmp = getStackInSlot(i);
          if (!isDifferent[i] && slot == -1 && tmp.getMaxStackSize() > tmp.stackSize && QMC.getQMC(getStackInSlot(i)) + stored <= getMaxQMC()) {
            slot = i;
            break;
          }
        }
      }
      if (slot != -1) {
        chestContents[slot].stackSize++;
        return true;
      }
      int free = freeSlot();
      if (free != -1) {
        ItemStack tmp = chestContents[0].copy();
        tmp.stackSize = 1;
        setInventorySlotContents(free, tmp);
        isDifferent[free] = false; // Instead of doing a full check ( updateDifferent() )
        return true;
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
    fluidBehaviour = TagCompound.getBoolean("FluidBehaviour");
    if (TagCompound.hasKey("Fluid")) {
      fluidStored = FluidStack.loadFluidStackFromNBT(TagCompound.getCompoundTag("Fluid"));
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
    TagCompound.setBoolean("FluidBehaviour", fluidBehaviour);
    if (fluidStored != null) {
      TagCompound.setTag("Fluid", fluidStored.writeToNBT(new NBTTagCompound()));
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
    nbt.setBoolean("hasTarget", hasTarget);
    nbt.setBoolean("fluidBehaviour", fluidBehaviour);
    return nbt;
  }

  @Override
  public void restoreClient(NBTTagCompound nbt) {
    stored = nbt.getDouble("stored");
    hasTarget = nbt.getBoolean("hasTarget");
    fluidBehaviour = nbt.getBoolean("fluidBehaviour");
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side) {
    if (side == 1) {
      int[] tmp = {0};
      return tmp;
    } else if (side == 0) {
      return targetCopyExtract();
    } else {
      int[] tmp = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
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
    int[] toReturn = new int[0];
    for (int i = 1; i < size; i++) {
      if (!isDifferent[i]) {
        int[] tmp = new int[toReturn.length + 1];
        System.arraycopy(toReturn, 0, tmp, 0, toReturn.length);
        toReturn = tmp;
        toReturn[toReturn.length - 1] = i;
      }
    }
    return toReturn;
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
    return 1000000; // 10,000 (Diamond rounded up) * 100
  }

  @Override
  public int getTier() {
    return 2;
  }

  @Override
  public OEType getType() {
    if (chestContents[0] != null && QMC.hasQMC(chestContents[0]) && !finished) {
      return OEType.Consumer;
    } else {
      return OEType.None;
    }
  }

  // Fluids
  private final int capacity = 2048;

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    return 0;
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    if (fluidStored == null || resource.fluidID != fluidStored.fluidID) {
      return null;
    }

    int drained = resource.amount;
    if (fluidStored.amount < drained) {
      drained = fluidStored.amount;
    }

    FluidStack stack = new FluidStack(fluidStored.fluidID, drained);
    if (doDrain) {
      fluidStored.amount -= drained;
      if (fluidStored.amount <= 0) {
        fluidStored = null;
      }
    }
    return stack;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    if (fluidStored == null) {
      return null;
    }

    int drained = maxDrain;
    if (fluidStored.amount < drained) {
      drained = fluidStored.amount;
    }

    FluidStack stack = new FluidStack(fluidStored.fluidID, drained);
    if (doDrain) {
      fluidStored.amount -= drained;
      if (fluidStored.amount <= 0) {
        fluidStored = null;
      }
    }
    return stack;
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return false;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    if (fluid != null && QMC.getQMC(fluid) > 0 && this.fluidTarget.fluidID == fluid.getID()) {
      return true;
    }
    return false;
  }

  @Override
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return new FluidTankInfo[]{new FluidTankInfo(fluidStored, capacity)};
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
