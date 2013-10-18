package oe.item;

import java.util.List;
import oe.helper.BlockItem;
import oe.helper.Sided;
import oe.value.Values;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBuildRing extends Item {
  
  public ItemBuildRing(int id) {
    super(id);
    setTextureName(Items.Texture("BuildRing"));
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemStack);
    if (Sided.isServer()) {
      if (itemStack.getTagCompound().getBoolean("Enabled")) {
        if (entity instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) entity;
          ItemStack held = player.getHeldItem();
          if (held != null) {
            if (BlockItem.isBlock(held.itemID)) {
              if (held.stackSize == 1) {
                if (Values.hasValue(held)) {
                  int v = itemStack.getTagCompound().getInteger("Value");
                  if (v >= Values.getValue(held)) {
                    itemStack.getTagCompound().setInteger("Value", itemStack.getTagCompound().getInteger("Value") - Values.getValue(held));
                    held.stackSize++;
                  } else {
                    int vs = ValueSlot(player);
                    if (vs != -1) {
                      itemStack.getTagCompound().setInteger("Value", itemStack.getTagCompound().getInteger("Value") + Values.getValue(player.inventory.mainInventory[vs]));
                      player.inventory.mainInventory[vs].stackSize--;
                      if (player.inventory.mainInventory[vs].stackSize == 0) {
                        player.inventory.mainInventory[vs] = null;
                      }
                      itemStack.getTagCompound().setInteger("Value", itemStack.getTagCompound().getInteger("Value") - Values.getValue(held));
                      held.stackSize++;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.getTagCompound().setInteger("Value", 0);
    }
  }
  
  private int ValueSlot(EntityPlayer player) {
    for (int slot = 35; slot >= 0; slot--) {
      if (player.inventory.mainInventory[slot] != null) {
        if (Values.hasValue(player.inventory.mainInventory[slot]) && player.inventory.currentItem != slot) {
          return slot;
        }
      }
    }
    return -1;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
    if (itemStack.getTagCompound() != null) {
      if (itemStack.getTagCompound().getBoolean("Enabled")) {
        list.add("\u00A77Enabled");
      } else {
        list.add("\u00A77Disabled");
      }
      list.add(Values.name + ": " + itemStack.getTagCompound().getInteger("Value"));
    }
  }
  
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    NBTTagCompound tag = itemStack.getTagCompound();
    if (Sided.isServer()) {
      if (tag.getBoolean("Enabled")) {
        tag.setBoolean("Enabled", false);
        player.addChatMessage("\u00A73\u00A7lBuilder's Ring:\u00A7r\u00A77 Disabled");
      } else {
        tag.setBoolean("Enabled", true);
        player.addChatMessage("\u00A73\u00A7lBuilder's Ring:\u00A7r\u00A77 Enabled");
      }
    }
    return itemStack;
  }
}
