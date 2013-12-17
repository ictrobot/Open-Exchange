package oe.core.util.data;

public class Pair<A, B> {
  public final A left;
  public final B right;
  
  public Pair(A left, B right) {
    this.left = left;
    this.right = right;
  }
  
  public String toString() {
    return "<" + (left.toString()).trim() + ", " + (right.toString()).trim() + ">";
  }
}
