package oe.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import oe.api.OEPipeInterface;
import oe.core.data.TileSync.ServerNetworkedTile;

public class TilePipe extends TileEntity implements ServerNetworkedTile, OEPipeInterface {
  public double passThrough;

  public TilePipe() {
    super();
  }

  @Override
  public void updateEntity() {
    passThrough = 0;
  }

  @Override
  public void readFromNBT(NBTTagCompound TagCompound) {
    super.readFromNBT(TagCompound);
  }

  @Override
  public void writeToNBT(NBTTagCompound TagCompound) {
    super.writeToNBT(TagCompound);
  }

  @Override
  public NBTTagCompound snapshotServer() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setDouble("passThrough", passThrough);
    return nbt;
  }

  @Override
  public void restoreClient(NBTTagCompound nbt) {
    passThrough = nbt.getDouble("passThrough");
  }

  @Override
  public double getMaxQMC() {
    return 100;
  }

  public void onClick(EntityPlayer player) {

  }

  @Override
  public double increasePassThrough(double amount) {
    passThrough = passThrough + amount;
    if (passThrough > getMaxQMC()) {
      passThrough = getMaxQMC();
    }
    return passThrough;
  }

  @Override
  public double passThroughLeft() {
    return getMaxQMC() - passThrough;
  }

}
