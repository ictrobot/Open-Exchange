package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.tile.TileStorage;
import oe.lib.util.Util;
import oe.qmc.QMC;

public class BlockStorage extends BlockContainer {
  
  public BlockStorage(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture(this.getClass().getSimpleName().substring(5).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World) {
    TileStorage storage = new TileStorage();
    return storage;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    if (Util.isServerSide()) {
      TileEntity te = world.getBlockTileEntity(i, j, k);
      if (te == null || !(te instanceof TileStorage) || world.isRemote) {
        return true;
      }
      TileStorage storage = (TileStorage) te;
      storage.onInventoryChanged();
      player.addChatMessage("Stored " + storage.getQMC() + " Maximum " + storage.getMaxQMC() + " Percentage " + QMC.formatter.format(storage.stored / storage.getMaxQMC() * 100));
      storage.onClick(player);
    }
    return true;
  }
}
