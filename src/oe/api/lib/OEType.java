package oe.api.lib;

public enum OEType {
  Producer, // Produces QMC (Does not accepts QMC)
  Consumer, // Uses QMC (Accepts QMC)
  Storage, // Output and Input (Both)
}
