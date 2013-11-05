package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import oe.lib.Log;
import oe.qmc.QMC;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;

public class TransmutationPacket {
  
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
    try {
      if (Player instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) Player;
        World world = player.worldObj;
        int id = world.getBlockId(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (world.getBlockTileEntity(x, y, z) != null) {
          return;
        }
        ItemStack stack = new ItemStack(id, 1, meta);
        if (!isValid(stack)) {
          return;
        }
        double value = QMC.getQMC(stack);
        if (value > 0) {
          ItemStack[] stacks = QMC.getItemStacksFromQMC(value);
          if (stacks.length > 1) {
            int pos = -1;
            for (int i = 0; i < stacks.length; i++) {
              if (stacks[i].itemID == id && stacks[i].getItemDamage() == meta && pos == -1) {
                pos = i;
                break;
              }
            }
            int loops = 0;
            while (true) {
              loops++;
              if (loops == stacks.length + 1) {
                break;
              }
              if (pos == stacks.length - 1) {
                pos = 0;
              } else {
                pos++;
              }
              ItemStack toSet = stacks[pos];
              if (toSet.itemID < 4096) {
                if (isValid(toSet)) {
                  world.setBlock(x, y, z, toSet.itemID, toSet.getItemDamage(), 3);
                  break;
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      Log.severe("Error occurred while transmuting block at X:" + x + " Y:" + y + " Z:" + z);
      e.printStackTrace();
    }
  }
  
  private static boolean isValid(ItemStack stack) {
    String str = stack.toString();
    boolean contains = str.contains("item.");
    if (!contains) {
      return Block.blocksList[stack.itemID].isOpaqueCube();
    } else {
      return false;
    }
  }
}
