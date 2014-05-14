package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.tile.TileExperienceConsumer;

public class BlockExperienceConsumer extends BlockContainer {
  
  public BlockExperienceConsumer() {
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
    TileExperienceConsumer storage = new TileExperienceConsumer();
    return storage;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    TileEntity te = world.getTileEntity(i, j, k);
    if (te == null || !(te instanceof TileExperienceConsumer) || world.isRemote) {
      return true;
    }
    TileExperienceConsumer experienceConsumer = (TileExperienceConsumer) te;
    experienceConsumer.onClick(player);
    return true;
  }
}
