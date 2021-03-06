package oe.block.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import oe.block.tile.TileExtractor;

public class ContainerExtractor extends Container {

  protected TileExtractor tileEntity;
  int size;

  public ContainerExtractor(InventoryPlayer inventoryPlayer, TileExtractor te) {
    tileEntity = te;
    size = te.size;
    int off = 0;
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        addSlotToContainer(new Slot(tileEntity, off++, 8 + x * 18, 23 + y * 18));
      }
    }
    bindPlayerInventory(inventoryPlayer);
  }

  protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 87 + i * 18));
      }
    }

    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return tileEntity.isUseableByPlayer(player);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer p, int i) {
    ItemStack itemstack = null;
    Slot slot = (Slot) inventorySlots.get(i);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (i < size) {
        if (!mergeItemStack(itemstack1, size, inventorySlots.size(), true)) {
          return null;
        }
      } else if (!tileEntity.isItemValidForSlot(slot.slotNumber, itemstack1)) {
        return null;
      } else if (!mergeItemStack(itemstack1, 0, size, false)) { // 0 First Slot
        return null;
      }
      if (itemstack1.stackSize == 0) {
        slot.putStack(null);
      } else {
        slot.onSlotChanged();
      }
    }
    return itemstack;
  }

  @Override
  public void onContainerClosed(EntityPlayer entityplayer) {

  }
}
