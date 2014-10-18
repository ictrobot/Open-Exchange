package oe.block;

import net.minecraft.block.Block;
import oe.core.Debug;
import oe.core.util.ConfigUtil;
import oe.core.util.RegisterUtil;
import oe.core.util.RegisterUtil.Register;

import java.lang.reflect.Field;

public class OEBlocks {

  @Register(BlockCondenser.class)
  public static Block condenser;
  @Register(BlockCharging.class)
  public static Block charging;
  @Register(BlockExtractor.class)
  public static Block extractor;
  @Register(BlockStorage.class)
  public static Block storage;
  @Register(BlockExperienceConsumer.class)
  public static Block experienceConsumer;
  @Register(BlockPipe.class)
  public static Block pipe;

  public static void load() {
    for (Field f : OEBlocks.class.getFields()) {
      try {
        if (f.isAnnotationPresent(Register.class)) {
          Register r = f.getAnnotation(Register.class);
          Class<?> c = r.value();
          if (c != null && ConfigUtil.other("block", c.getSimpleName() + "Enabled", r.enabled())) {
            f.set(null, c.newInstance());
          }

        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
  }

  public static void register() {
    for (Field f : OEBlocks.class.getFields()) {
      try {
        Object o = f.get(null);
        if (f.isAnnotationPresent(Register.class) && o != null && o instanceof Block) {
          RegisterUtil.Block((Block) o);
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
  }

  public static String Texture(String str) {
    return "oe:block" + str;
  }
}
