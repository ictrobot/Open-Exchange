package oe.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.block.tile.TileDrillRemoteReceiver;
import oe.core.data.Location;
import oe.core.data.RemoteDrillData;
import oe.core.util.Util;

public class BlockDrillRemoteReceiver extends BlockContainer {
  
  public BlockDrillRemoteReceiver(int id) {
    super(id, Material.iron);
    setTextureName(Blocks.Texture(this.getClass().getSimpleName().substring(5).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundMetalFootstep);
  }
  
  @Override
  public TileEntity createNewTileEntity(World par1World) {
    TileDrillRemoteReceiver drill = new TileDrillRemoteReceiver();
    return drill;
  }
  
  @Override
  public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
    if (Util.isServerSide()) {
      TileEntity te = world.getBlockTileEntity(i, j, k);
      if (te == null || !(te instanceof TileDrillRemoteReceiver) || world.isRemote) {
        return true;
      }
      TileDrillRemoteReceiver drill = (TileDrillRemoteReceiver) te;
      Location loc = RemoteDrillData.getLocationDrill(drill.RemoteID);
      player.addChatMessage("ID " + drill.RemoteID + ", Stored " + RemoteDrillData.getQMC(drill.RemoteID) + ", DrillX " + loc.x + ", DrillY " + loc.y + ", DrillZ " + loc.z);
    }
    return true;
  }
  
  @Override
  public int quantityDropped(Random rand) {
    return 0;
  }
}
