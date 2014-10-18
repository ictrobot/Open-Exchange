package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.tile.TilePipe;

public class BlockPipe extends BlockContainer {

  public BlockPipe() {
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
    TilePipe pipe = new TilePipe();
    return pipe;
  }
}
