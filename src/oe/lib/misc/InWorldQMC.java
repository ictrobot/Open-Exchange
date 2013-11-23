package oe.lib.misc;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oe.api.OEPipeInterface;
import oe.api.OETileInterface;
import oe.api.lib.OEType;
import oe.lib.util.WorldUtil;

public class InWorldQMC {
  
  private static class Path {
    public BlockLocation block;
    public BlockLocation[] pipes;
    
    public Path(BlockLocation Block, BlockLocation[] Pipes) {
      this.block = Block;
      this.pipes = Pipes;
    }
  }
  
  private static class PathFinderData {
    public Path[] paths;
    public BlockLocation[] checkedPipes;
    
    public PathFinderData(Path[] Paths) {
      this.paths = Paths;
      this.checkedPipes = new BlockLocation[0];
    }
  }
  
  /**
   * Provide other blocks with QMC, Coords are for the block providing
   * 
   * @param x
   * @param y
   * @param z
   * @param qmc
   * @return amount left over
   */
  public static double provide(int X, int Y, int Z, World world, double qmc) {
    if (qmc == 0) {
      return 0;
    }
    double qmcLeftOver = qmc;
    for (Path p : getPaths(X, Y, Z, world)) {
      TileEntity tile = world.getBlockTileEntity(p.block.x, p.block.y, p.block.z);
      if (tile != null) {
        if (isOETile(tile.getClass())) {
          OETileInterface oe = (OETileInterface) tile;
          if (oe.getType() == OEType.Consumer || oe.getType() == OEType.Storage) {
            int tier = oe.getTier();
            double canHandle = oe.getMaxQMC() - oe.getQMC();
            double maxPipeTransfer = tier * 10;
            for (BlockLocation pipeLoc : p.pipes) {
              TileEntity tilePipe = world.getBlockTileEntity(pipeLoc.x, pipeLoc.y, pipeLoc.z);
              if (tilePipe != null) {
                if (isOEPipe(tilePipe.getClass())) {
                  OEPipeInterface pipe = (OEPipeInterface) tilePipe;
                  maxPipeTransfer = Math.min(maxPipeTransfer, pipe.passThroughLeft());
                }
              }
            }
            double amount = 0;
            if (canHandle > maxPipeTransfer) {
              amount = maxPipeTransfer;
            } else {
              amount = canHandle;
            }
            if (amount > qmcLeftOver) {
              amount = qmcLeftOver;
            }
            oe.increaseQMC(amount);
            qmcLeftOver = qmcLeftOver - amount;
            for (BlockLocation pipeLoc : p.pipes) {
              TileEntity tilePipe = world.getBlockTileEntity(pipeLoc.x, pipeLoc.y, pipeLoc.z);
              if (tilePipe != null) {
                if (isOEPipe(tilePipe.getClass())) {
                  OEPipeInterface pipe = (OEPipeInterface) tilePipe;
                  pipe.increasePassThrough(amount);
                }
              }
            }
          }
        }
      }
    }
    return qmcLeftOver;
  }
  
  public static Path[] getPaths(int x, int y, int z, World world) {
    Path[] path = new Path[0];
    for (int i = 0; i < 6; i++) {
      BlockLocation b = WorldUtil.getLocationOnSide(x, y, z, i);
      TileEntity te = world.getBlockTileEntity(b.x, b.y, b.z);
      if (te != null && isOETile(te.getClass())) {
        path = addPath(path, b, new BlockLocation[] {});
      }
    }
    path = getPaths(x, y, z, world, new PathFinderData(path), new BlockLocation[] {}).paths;
    return path;
  }
  
  private static PathFinderData getPaths(int x, int y, int z, World world, PathFinderData data, BlockLocation[] Pipes) {
    BlockLocation[] pipes = Pipes.clone();
    for (int c = 0; c < 6; c++) {
      BlockLocation check = WorldUtil.getLocationOnSide(x, y, z, c);
      TileEntity tile = world.getBlockTileEntity(check.x, check.y, check.z);
      if (tile != null) {
        if (isOEPipe(tile.getClass())) {
          if (!WorldUtil.exists(data.checkedPipes, check)) {
            data.checkedPipes = WorldUtil.increaseAdd(data.checkedPipes, check);
            pipes = WorldUtil.increaseAdd(pipes, check);
            data = getPaths(check.x, check.y, check.z, world, data, pipes);
          }
        } else if (isOETile(tile.getClass())) {
          boolean checked = false;
          for (Path p : data.paths) {
            if (p.block.equals(check)) {
              checked = true;
              break;
            }
          }
          if (!checked) {
            Path[] tmp = new Path[data.paths.length + 1];
            System.arraycopy(data.paths, 0, tmp, 0, data.paths.length);
            data.paths = tmp;
            data.paths[data.paths.length - 1] = new Path(check, Pipes);
          }
        }
      }
    }
    return data;
  }
  
  /**
   * Checks if OE pipe
   * 
   * @param c
   * @return
   */
  public static boolean isOEPipe(Class<?> c) {
    Class<?>[] classes = c.getInterfaces();
    for (Class<?> i : classes) {
      if (i.getName().contains("OEPipeInterface")) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isOETile(Class<?> c) {
    Class<?>[] classes = c.getInterfaces();
    for (Class<?> i : classes) {
      if (i.getName().contains("OETileInterface")) {
        return true;
      }
    }
    return false;
  }
  
  private static Path[] addPath(Path[] path, BlockLocation block, BlockLocation[] pipes) {
    Path[] tmp = new Path[path.length + 1];
    System.arraycopy(path, 0, tmp, 0, path.length);
    path = tmp;
    path[path.length - 1] = new Path(block, pipes);
    return path;
  }
}
