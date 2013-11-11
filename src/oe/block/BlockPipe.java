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
    setTextureName(Blocks.Texture("Pipe"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("Pipe");
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public TileEntity createNewTileEntity(World par1World) {
    TilePipe pipe = new TilePipe();
    return pipe;
  }
}
