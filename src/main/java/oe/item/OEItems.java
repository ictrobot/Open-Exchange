package oe.item;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import oe.core.Debug;
import oe.core.util.ConfigUtil;
import oe.core.util.RegisterUtil;
import oe.core.util.RegisterUtil.Register;

import java.lang.reflect.Field;

public class OEItems {

  @Register(ItemBuildRing.class)
  public static Item buildRing;
  @Register(ItemInfo.class)
  public static Item info;
  @Register(ItemQuantumPickaxe.class)
  public static Item pickaxe;
  @Register(ItemQuantumAxe.class)
  public static Item shovel;
  @Register(ItemQuantumShovel.class)
  public static Item axe;
  @Register(ItemRepair.class)
  public static Item repair;
  @Register(ItemBlockManipulator.class)
  public static Item blockManipulator;

  public static final Item.ToolMaterial quantumToolMaterial = EnumHelper.addToolMaterial("quantum", 3, 0, 8.0F, 3.0F, 10);

  public static void load() {
    for (Field f : OEItems.class.getFields()) {
      try {
        if (f.isAnnotationPresent(Register.class)) {
          Register r = f.getAnnotation(Register.class);
          Class<?> c = r.value();
          if (c != null && ConfigUtil.other("item", c.getSimpleName() + "Enabled", r.enabled())) {
            f.set(null, c.newInstance());
          }

        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
  }

  public static void register() {
    for (Field f : OEItems.class.getFields()) {
      try {
        Object o = f.get(null);
        if (f.isAnnotationPresent(Register.class) && o != null && o instanceof Item) {
          RegisterUtil.Item((Item) o);
        }
      } catch (Exception e) {
        Debug.handleException(e);
      }
    }
  }

  public static String Texture(String str) {
    return "OE:item" + str;
  }
}
