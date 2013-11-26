package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.tile.TilePipe;

public class BlockPipe extends BlockContainer {
  
  public BlockPipe(int id) {
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
    TilePipe pipe = new TilePipe();
    return pipe;
  }
}
