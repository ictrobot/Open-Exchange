package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import oe.block.tile.TileStorage;
import oe.core.util.Util;
import oe.qmc.QMC;

public class BlockStorage extends BlockContainer {
  
  public BlockStorage() {
    super(Material.iron);
    setBlockTextureName(OEBlocks.Texture(this.getClass().getSimpleName().substring(5).trim()));
    setBlockName(this.getClass().getSimpleName());
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World, int var2) {
    TileStorage storage = new TileStorage();
    return storage;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    if (Util.isServerSide()) {
      TileEntity te = world.getTileEntity(i, j, k);
      if (te == null || !(te instanceof TileStorage) || world.isRemote) {
        return true;
      }
      TileStorage storage = (TileStorage) te;
      player.addChatMessage(new ChatComponentText("Stored " + storage.getQMC() + " Maximum " + storage.getMaxQMC() + " Percentage " + QMC.formatter.format(storage.stored / storage.getMaxQMC() * 100)));
      storage.onClick(player);
    }
    return true;
  }
}
