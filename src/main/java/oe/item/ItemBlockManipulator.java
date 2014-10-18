package oe.item;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import oe.api.OEItemInterface;
import oe.api.OEItemMode;
import oe.api.lib.OEType;
import oe.core.Debug;
import oe.core.Log;
import oe.core.data.QuantumToolBlackList;
import oe.core.util.ConfigUtil;
import oe.core.util.NetworkUtil;
import oe.core.util.NetworkUtil.Channel;
import oe.core.util.NetworkUtil.PacketProcessor;
import oe.core.util.Util;
import oe.qmc.QMC;

import java.util.List;

public class ItemBlockManipulator extends Item implements OEItemInterface, OEItemMode, PacketProcessor {

  public ItemBlockManipulator() {
    super();
    setTextureName(OEItems.Texture(this.getClass().getSimpleName().substring(4).trim()));
    setUnlocalizedName(this.getClass().getSimpleName());
    setCreativeTab(CreativeTabs.tabTools);
    setMaxStackSize(1);
  }

  @SubscribeEvent
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    if (Util.isClientSide()) {
      if (event.action != Action.RIGHT_CLICK_BLOCK) {
        return;
      }
      EntityPlayer player = event.entityPlayer;
      if (player == null) {
        return;
      }
      if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlockManipulator) || player.getHeldItem().stackTagCompound == null || !(player.getHeldItem().getItem() instanceof OEItemInterface)) {
        return;
      }
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("x", event.x);
      nbt.setInteger("y", event.y);
      nbt.setInteger("z", event.z);
      nbt.setInteger("face", event.face);// Bottom = 0, Top = 1, Sides = 2-5
      NetworkUtil.sendToServer(Channel.BlockManipulater, nbt);
      event.setCanceled(true);
    }
  }

  @Override
  public void handlePacket(NBTTagCompound nbt, EntityPlayer player, Side side) {
    int x = nbt.getInteger("x");
    int y = nbt.getInteger("y");
    int z = nbt.getInteger("z");
    int blockFace = nbt.getInteger("face");
    try {
      World world = player.worldObj;
      if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlockManipulator) || player.getHeldItem().stackTagCompound == null) {
        return;
      }
      if (!player.getHeldItem().stackTagCompound.getBoolean("hasBlock")) {
        boolean move = player.getHeldItem().stackTagCompound.getCompoundTag("mode").getBoolean("move");
        double cost;
        String id = world.getBlock(x, y, z).getUnlocalizedName();
        int meta = world.getBlockMetadata(x, y, z);
        OEItemInterface oe = (OEItemInterface) player.getHeldItem().getItem();
        if (move) {
          cost = ConfigUtil.other("item", "blockManipulatorMoveCost", 0);
          if (oe.getQMC(player.getHeldItem()) < cost && !player.capabilities.isCreativeMode) {
            return;
          }
        } else {
          cost = ConfigUtil.other("item", "blockManipulatorCopyCost", 0);
          double blockCost = QMC.getQMC(new ItemStack(world.getBlock(x, y, z), 1, meta));
          if (blockCost < 0) {
            return;
          }
          cost += blockCost;
        }
        if (!player.capabilities.isCreativeMode) {
          oe.decreaseQMC(cost, player.getHeldItem());
        }
        if (QuantumToolBlackList.isBlackListed(world.getBlock(x, y, z))) {
          return;
        }
        if (oe.getQMC(player.getHeldItem()) < cost && !player.capabilities.isCreativeMode) {
          return;
        }
        NBTTagCompound block = new NBTTagCompound();
        block.setString("id", id);
        block.setInteger("meta", meta);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && move) {
          NBTTagCompound nbtTE = new NBTTagCompound();
          te.writeToNBT(nbtTE);
          block.setTag("tile", nbtTE);
        }
        player.getHeldItem().stackTagCompound.setTag("block", nbt);
        player.getHeldItem().stackTagCompound.setBoolean("hasBlock", true);
        player.getHeldItem().setItemDamage(1); // To stop them having values while storing a
        // block
        if (move) {
          if (te != null) {
            TileEntity toSet = world.getBlock(x, y, z).createTileEntity(world, world.getBlockMetadata(x, y, z));
            world.setTileEntity(x, y, z, toSet);
          }
          world.setBlockToAir(x, y, z);
        }
      } else {
        if (blockFace == 0) {
          y--;
        } else if (blockFace == 1) {
          y++;
        } else if (blockFace == 2) {
          z--;
        } else if (blockFace == 3) {
          z++;
        } else if (blockFace == 4) {
          x--;
        } else if (blockFace == 5) {
          x++;
        }
        NBTTagCompound block = player.getHeldItem().stackTagCompound.getCompoundTag("block");

        String id = block.getString("id");
        int meta = block.getInteger("meta");
        Block b = Block.getBlockFromName(id);
        if (!b.canPlaceBlockAt(world, x, y, z) || !world.isAirBlock(x, y, z)) {
          return; // Not valid pos
        }
        world.setBlock(x, y, z, b, meta, 3);
        world.setTileEntity(x, y, z, b.createTileEntity(world, meta));
        if (block.hasKey("tile")) {
          NBTTagCompound tile = block.getCompoundTag("tile");
          tile.setInteger("x", x);
          tile.setInteger("y", y);
          tile.setInteger("z", z);
          world.getTileEntity(x, y, z).readFromNBT(tile);
        }
        player.getHeldItem().stackTagCompound.removeTag("block");
        player.getHeldItem().stackTagCompound.setBoolean("hasBlock", false);
        player.getHeldItem().setItemDamage(0);
      }
    } catch (Exception e) {
      Log.severe("Error occurred while moving block at X:" + x + " Y:" + y + " Z:" + z);
      Debug.handleException(e);
    }
  }

  @Override
  public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
    checkNBT(itemstack);
  }

  private void checkNBT(ItemStack itemstack) {
    if (itemstack.getTagCompound() == null) {
      itemstack.setTagCompound(new NBTTagCompound());
      itemstack.stackTagCompound.setTag("mode", new NBTTagCompound());
      itemstack.stackTagCompound.getCompoundTag("mode").setBoolean("copy", true);
      itemstack.stackTagCompound.setBoolean("Enabled", false);
    }
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
    if (itemStack.getTagCompound() != null) {
      // list.add(QMC.name + ": " + itemStack.stackTagCompound.getDouble("Value")); Because Charging
      // currently does nothing
      if (itemStack.stackTagCompound.getBoolean("hasBlock")) {
        NBTTagCompound nbtBlock = itemStack.stackTagCompound.getCompoundTag("block");
        if (nbtBlock != null) {
          Block block = Block.getBlockFromName(nbtBlock.getString("id"));
          list.add("Currently Holding: " + block.getLocalizedName());
        }
      }
    }
  }

  @Override
  public double getQMC(ItemStack stack) {
    if (stack.getTagCompound() != null) {
      return stack.getTagCompound().getDouble("Value");
    } else {
      return -1;
    }
  }

  @Override
  public void setQMC(double qmc, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      stack.getTagCompound().setDouble("Value", qmc);
    }
  }

  @Override
  public void increaseQMC(double value, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      double current = stack.getTagCompound().getDouble("Value");
      if (current + value > getMaxQMC(stack)) {
        current = getMaxQMC(stack);
      } else {
        current = current + value;
      }
      stack.getTagCompound().setDouble("Value", current);
    }
  }

  @Override
  public void decreaseQMC(double value, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      double current = stack.getTagCompound().getDouble("Value");
      if (current - value < 0) {
        current = 0;
      } else {
        current = current - value;
      }
      stack.getTagCompound().setDouble("Value", current);
    }
  }

  @Override
  public double getMaxQMC(ItemStack itemstack) {
    return 50000;
  }

  @Override
  public int getTier(ItemStack itemstack) {
    return 2;
  }

  @Override
  public OEType getType(ItemStack itemstack) {
    return OEType.Consumer;
  }

  @Override
  public String getMode(ItemStack itemstack) {
    checkNBT(itemstack);
    if (itemstack.stackTagCompound.getCompoundTag("mode").getBoolean("copy")) {
      return "Copy";
    } else {
      return "Move";
    }
  }

  @Override
  public String switchMode(ItemStack itemstack) {
    checkNBT(itemstack);
    if (itemstack.stackTagCompound.getCompoundTag("mode").getBoolean("copy")) {
      itemstack.stackTagCompound.getCompoundTag("mode").removeTag("copy");
      itemstack.stackTagCompound.getCompoundTag("mode").setBoolean("move", true);
    } else {
      itemstack.stackTagCompound.getCompoundTag("mode").removeTag("move");
      itemstack.stackTagCompound.getCompoundTag("mode").setBoolean("copy", true);
    }
    return getMode(itemstack);
  }
}
