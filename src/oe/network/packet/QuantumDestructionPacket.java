package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import oe.api.OEItemInterface;
import oe.api.OE_API;
import oe.lib.Debug;
import oe.lib.helper.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;

public class QuantumDestructionPacket {
  
  private static double QMCNeeded;
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player Player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    int x;
    int y;
    int z;
    
    try {
      x = inputStream.readInt();
      y = inputStream.readInt();
      z = inputStream.readInt();
    } catch (IOException e) {
      return;
    }
    if (Player instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) Player;
      ConfigHelper.load();
      QMCNeeded = ConfigHelper.other("Item", "Quantum tools right click cost per block", 5);
      ConfigHelper.save();
      blockBreak(x, y, z, player);
    }
  }
  
  private static void blockBreak(int x, int y, int z, EntityPlayer player) {
    int ID = player.worldObj.getBlockId(x, y, z);
    int meta = player.worldObj.getBlockMetadata(x, y, z);
    if (OE_API.isOE(player.getHeldItem().getItem().getClass())) {
      Item item = player.getHeldItem().getItem();
      Float f = item.getStrVsBlock(player.getHeldItem(), Block.blocksList[ID], meta);
      if (f <= 1) {
        return;
      }
    } else {
      return;
    }
    blockBreak(x, y, z, player, 0, ID, meta);
  }
  
  private static void blockBreak(int x, int y, int z, EntityPlayer player, int times, int ID, int meta) {
    try {
      if (times == 5) {
        return;
      }
      World world = player.worldObj;
      Block block = Block.blocksList[ID];
      if (ID == world.getBlockId(x, y, z) && meta == world.getBlockMetadata(x, y, z)) {
        ItemStack newStack = new ItemStack(block.idDropped(meta, world.rand, 0), block.quantityDropped(world.rand), block.damageDropped(meta));
        if (OE_API.isOE(player.getHeldItem().getItem().getClass())) {
          OEItemInterface oe = (OEItemInterface) player.getHeldItem().getItem();
          if (oe.getQMC(player.getHeldItem()) > QMCNeeded) {
            if (player.inventory.addItemStackToInventory(newStack)) {
              oe.decreaseQMC(QMCNeeded, player.getHeldItem());
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
        blockBreak(targetX, targetY, targetZ, player, times + 1, ID, meta);
      }
    } catch (Exception e) {
      Debug.handleException(e);
    }
  }
}
