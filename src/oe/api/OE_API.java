package oe.api;

public class OE_API {
  
  /**
   * Checks if a class extends OEInterface (Checks by looking for a method called "isOE")
   * 
   * @param c
   * @return
   */
  public static boolean isOE(Class<?> c) {
    Class<?>[] classes = c.getInterfaces();
    for (Class<?> i : classes) {
      if (i.getName().contains("OEItemInterface") || i.getName().contains("OETileInterface")) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks if a class extends OEGuessable (Checks by looking for a method called "isOEGuessable")
   * 
   * @param c
   * @return
   */
  public static boolean isOEGuessable(Class<?> c) {
    Class<?> e = c.getSuperclass();
    if (e == null) {
      return false;
    }
    return e.getName().contains("OEGuesser");
  }
}
