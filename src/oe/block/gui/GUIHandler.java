package oe.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.container.ContainerCharging;
import oe.block.container.ContainerCondenser;
import oe.block.container.ContainerExtractor;
import oe.block.tile.TileCharging;
import oe.block.tile.TileCondenser;
import oe.block.tile.TileExtractor;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    if (id == 0) {
      if (tileEntity instanceof TileCondenser) {
        return new ContainerCondenser(player.inventory, (TileCondenser) tileEntity);
      } else if (tileEntity instanceof TileCharging) {
        return new ContainerCharging(player.inventory, (TileCharging) tileEntity);
      } else if (tileEntity instanceof TileExtractor) {
        return new ContainerExtractor(player.inventory, (TileExtractor) tileEntity);
      }
    }
    return null;
  }
  
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    if (id == 0) {
      if (tileEntity instanceof TileCondenser) {
        return new GUICondenser(player.inventory, (TileCondenser) tileEntity);
      } else if (tileEntity instanceof TileCharging) {
        return new GUICharging(player.inventory, (TileCharging) tileEntity);
      } else if (tileEntity instanceof TileExtractor) {
        return new GUIExtractor(player.inventory, (TileExtractor) tileEntity);
      }
    }
    return null;
  }
}
