package oe.network.packet;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import oe.api.OE;
import oe.api.OEItemInterface;
import oe.core.Debug;
import oe.core.data.QuantumToolBlackList;
import oe.core.util.ConfigUtil;
import oe.core.util.NetworkUtil.PacketProcessor;

import java.util.ArrayList;

public class QuantumDestructionPacket implements PacketProcessor {

  private static double QMCNeeded = -1;

  @Override
  public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    int x = nbt.getInteger("x");
    int y = nbt.getInteger("y");
    int z = nbt.getInteger("z");
    if (QMCNeeded == -1) {
      QMCNeeded = ConfigUtil.other("Item", "Quantum tools right click cost per block", 5);
    }
    blockBreak(x, y, z, player);
  }

  private static void blockBreak(int x, int y, int z, EntityPlayer player) {
    String ID = player.worldObj.getBlock(x, y, z).getUnlocalizedName();
    int meta = player.worldObj.getBlockMetadata(x, y, z);
    Block block = player.worldObj.getBlock(x, y, z);
    if (OE.isOE(player.getHeldItem().getItem().getClass())) {
      ItemStack item = player.getHeldItem();
      Float f = item.getItem().getDigSpeed(item, block, meta);
      if (f <= 1) {
        return;
      }
    } else {
      return;
    }
    blockBreak(x, y, z, player, 0, block, ID, meta);
  }

  private static void blockBreak(int x, int y, int z, EntityPlayer player, int times, Block block, String ID, int meta) {
    try {
      if (times == 5) {
        return;
      }
      World world = player.worldObj;
      if (QuantumToolBlackList.isBlackListed(block)) {
        return;
      }
      if (ID == world.getBlock(x, y, z).getUnlocalizedName() && meta == world.getBlockMetadata(x, y, z)) {
        if (OE.isOE(player.getHeldItem().getItem().getClass())) {
          OEItemInterface oe = (OEItemInterface) player.getHeldItem().getItem();
          if (oe.getQMC(player.getHeldItem()) > QMCNeeded) {
            ArrayList<ItemStack> drops = block.getDrops(player.worldObj, x, y, x, meta, 0);
            boolean invSpace = false;
            for (Object o : drops) {
              if (o != null && o instanceof ItemStack) {
                Boolean inv = player.inventory.addItemStackToInventory((ItemStack) o);
                if (inv && !invSpace) {
                  invSpace = true;
                  break;
                }
              }
            }
            if (invSpace) {
              if (!player.capabilities.isCreativeMode) {
                oe.decreaseQMC(QMCNeeded, player.getHeldItem());
              }
              world.setBlockToAir(x, y, z);
            } else {
              return;
            }
          } else {
            return;
          }
        } else {
          return;
        }
      }
      for (int c = 0; c < 6; c++) {
        int targetX = x;
        int targetY = y;
        int targetZ = z;
        switch (c) {
          case 0:
            targetY++;
            break;
          case 1:
            targetY--;
            break;
          case 2:
            targetZ++;
            break;
          case 3:
            targetZ--;
            break;
          case 4:
            targetX++;
            break;
          case 5:
            targetX--;
            break;
        }
        blockBreak(targetX, targetY, targetZ, player, times + 1, block, ID, meta);
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
}
