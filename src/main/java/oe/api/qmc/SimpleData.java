package oe.api.qmc;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

public class SimpleData implements Data {

  public final double qmc;

  public SimpleData(double qmc) {
    this.qmc = qmc;
  }

  @Override
  public NBTBase toNBT() {
    return new NBTTagDouble(qmc);
  }

  public String toString() {
    return qmc + "";
  }

  public static Data getData(NBTBase nbt) {
    return new SimpleData(((NBTTagDouble) nbt).func_150286_g());
  }

}
