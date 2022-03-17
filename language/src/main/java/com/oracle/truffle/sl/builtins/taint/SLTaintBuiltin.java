package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.util.SLToSLStringNode;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "taint")
public abstract class SLTaintBuiltin extends SLBuiltinNode {

  /**
   * Default taint value if none is supplied.
   */
  private static final Object TAINT = new Object();

  /**
   * Taints the value passed using the provided taint.
   * Only {@link SLString}s and {@link String}s can be tainted.
   * <p>
   * If the param {@code taint} is not supplied, the default taint {@link SLTaintBuiltin#TAINT} is used.
   * This is a hacky solution, as Simple Language provides {@code NULL} as argument if no second argument is used.
   * @param value to be tainted
   * @param taint taint marker
   * @return the tainted {@link SLString}
   */
  @Specialization(guards = "isString(value)")
  public SLString taint(Object value, Object taint, @Cached SLToSLStringNode node) {
    SLString tainted = node.execute(value);
    tainted.addTaint(SLTypes.isSLNull(taint) ? TAINT : taint);
    return tainted;
  }

  protected boolean isString(Object value) {
    return value instanceof String || value instanceof SLString;
  }
}
