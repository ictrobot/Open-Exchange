package oe.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import oe.api.lib.Location;
import oe.block.tile.TileDrillRemoteReceiver;
import oe.lib.RemoteDrillData;
import oe.lib.helper.Sided;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDrillRemoteReceiver extends BlockContainer {
  
  public BlockDrillRemoteReceiver(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture("DrillRemoteReceiver"));
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
    setUnlocalizedName("BlockDrillRemoteReceiver");
  }
  
  public TileEntity createNewTileEntity(World par1World) {
    TileDrillRemoteReceiver drill = new TileDrillRemoteReceiver();
    return drill;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    if (Sided.isServer()) {
      TileEntity te = world.getBlockTileEntity(i, j, k);
      if (te == null || !(te instanceof TileDrillRemoteReceiver) || world.isRemote) {
        return true;
      }
      TileDrillRemoteReceiver drill = (TileDrillRemoteReceiver) te;
      Location loc = RemoteDrillData.getLocationDrill(drill.RemoteID);
      player.addChatMessage("ID " + drill.RemoteID + " Stored " + RemoteDrillData.getQMC(drill.RemoteID) + " DrillX " + loc.x + " DrillY " + loc.y + " DrillZ " + loc.z);
    }
    return true;
  }
  
  public int quantityDropped(Random rand) {
    return 0;
  }
  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister par1IconRegister) {
    icons = new Icon[1];
    icons[0] = par1IconRegister.registerIcon(Blocks.Texture("Drill_Side"));
  }
  
  @SideOnly(Side.CLIENT)
  public Icon getIcon(int par1, int par2) {
    return icons[0];
  }
}
