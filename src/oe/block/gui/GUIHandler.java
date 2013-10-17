package oe.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.container.ContainerCondenser;
import oe.block.tile.TileCondenser;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    if (tileEntity instanceof TileCondenser) {
      return new ContainerCondenser(player.inventory, (TileCondenser) tileEntity);
    }
    return null;
  }
  
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    if (tileEntity instanceof TileCondenser) {
      return new GUICondenser(player.inventory, (TileCondenser) tileEntity);
    }
    return null;
  }
}
