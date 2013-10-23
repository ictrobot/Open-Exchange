package oe.api.lib;

public enum OEType {
  /**
   * Produces QMC (Does not accepts QMC)
   */
  Producer,
  /**
   * Uses QMC (Accepts QMC)
   */
  Consumer,
  /**
   * Output and Input (Both)
   */
  Storage,
  /**
   * Does not Output or Input;
   */
  None,
}
