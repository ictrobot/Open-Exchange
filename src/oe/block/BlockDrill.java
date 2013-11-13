package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.tile.TileDrill;
import oe.lib.helper.Sided;

public class BlockDrill extends BlockContainer {
  
  public BlockDrill(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture("Drill"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockDrill");
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public TileEntity createNewTileEntity(World par1World) {
    TileDrill drill = new TileDrill();
    return drill;
  }
}
