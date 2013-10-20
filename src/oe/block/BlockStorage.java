package oe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import oe.block.tile.TileStorage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStorage extends BlockContainer {
  
  public BlockStorage(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture("Storage"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockStorage");
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public TileEntity createNewTileEntity(World par1World) {
    TileStorage storage = new TileStorage();
    return storage;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    TileEntity te = world.getBlockTileEntity(i, j, k);
    if (te == null || !(te instanceof TileStorage) || world.isRemote) {
      return true;
    }
    TileStorage storage = (TileStorage) te;
    storage.onInventoryChanged();
    player.addChatMessage("Stored " + storage.stored);
    storage.onClick(player);
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister par1IconRegister) {
    icons = new Icon[1];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Storage"));
  }
  
  @SideOnly(Side.CLIENT)
  public Icon getIcon(int par1, int par2) {
    return icons[0];
  }
}
