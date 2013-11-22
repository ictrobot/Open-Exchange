package oe.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import oe.item.ItemIDs;
import oe.lib.Debug;
import oe.lib.Log;
import oe.lib.misc.QuantumToolBlackList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.Player;

public class BlockMoverPacket {
  
  public static void packet(INetworkManager manager, Packet250CustomPayload packet, Player Player) {
    DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
    
    int x;
    int y;
    int z;
    int blockFace;
    try {
      x = inputStream.readInt();
      y = inputStream.readInt();
      z = inputStream.readInt();
      blockFace = inputStream.readInt();
    } catch (IOException e) {
      return;
    }
    try {
      if (Player instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) Player;
        World world = player.worldObj;
        if (player.getHeldItem() == null || player.getHeldItem().itemID != ItemIDs.blockMover + 256 || player.getHeldItem().stackTagCompound == null) {
          return;
        }
        if (!player.getHeldItem().stackTagCompound.getBoolean("hasBlock")) {
          Block block = Block.blocksList[world.getBlockId(x, y, z)];
          if (QuantumToolBlackList.isBlackListed(block)) {
            return;
          }
          NBTTagCompound nbt = new NBTTagCompound();
          nbt.setInteger("BlockID", world.getBlockId(x, y, z));
          nbt.setInteger("BlockMeta", world.getBlockMetadata(x, y, z));
          TileEntity te = world.getBlockTileEntity(x, y, z);
          if (te != null) {
            NBTTagCompound nbtTE = new NBTTagCompound();
            te.writeToNBT(nbtTE);
            nbt.setTag("tile", nbtTE);
          }
          player.getHeldItem().stackTagCompound.setTag("block", nbt);
          player.getHeldItem().stackTagCompound.setBoolean("hasBlock", true);
          player.getHeldItem().setItemDamage(1); // To stop them having values while storing a block
          if (te != null) {
            TileEntity toSet = Block.blocksList[world.getBlockId(x, y, z)].createTileEntity(world, world.getBlockMetadata(x, y, z));
            world.setBlockTileEntity(x, y, z, toSet);
          }
          world.setBlockToAir(x, y, z);
        } else {
          if (blockFace == 0) {
            y--;
          } else if (blockFace == 1) {
            y++;
          } else if (blockFace == 2) {
            z--;
          } else if (blockFace == 3) {
            z++;
          } else if (blockFace == 4) {
            x--;
          } else if (blockFace == 5) {
            x++;
          }
          NBTTagCompound nbt = player.getHeldItem().stackTagCompound.getCompoundTag("block");
          world.setBlock(x, y, z, nbt.getInteger("BlockID"), nbt.getInteger("BlockMeta"), 3);
          if (nbt.hasKey("tile")) {
            NBTTagCompound tile = nbt.getCompoundTag("tile");
            tile.setInteger("x", x);
            tile.setInteger("y", y);
            tile.setInteger("z", z);
            world.getBlockTileEntity(x, y, z).readFromNBT(tile);
          }
          player.getHeldItem().stackTagCompound.removeTag("block");
          player.getHeldItem().stackTagCompound.setBoolean("hasBlock", false);
          player.getHeldItem().setItemDamage(0);
        }
      }
    } catch (Exception e) {
      Log.severe("Error occurred while moving block at X:" + x + " Y:" + y + " Z:" + z);
      Debug.handleException(e);
    }
  }
}
