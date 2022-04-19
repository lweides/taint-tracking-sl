package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "addTaint")
public abstract class SLAddTaintBuiltin extends SLBuiltinNode {

  /**
   * Default taint value if none is supplied.
   */
  private static final String TAINT = "DEFAULT_TAINT";

  /**
   * Taints the value passed using the provided taint.
   * Only {@link SLString}s and {@link String}s can be tainted.
   * <p>
   * If the param {@code taint} is not supplied, the default taint {@link SLAddTaintBuiltin#TAINT} is used.
   * This is a hacky solution, as Simple Language provides {@code NULL} as argument if no second argument is used.
   * @param value to be tainted
   * @param taint taint marker
   * @return the tainted {@link SLString}
   */
  @Specialization
  public SLString addTaint(String value, Object taint,
                           @CachedLibrary(limit = "3") InteropLibrary interop) {
    return SLString.addTaint(value, interop.isNull(taint) ? TAINT : taint);
  }

  @Specialization
  public SLString addTaint(SLString value, Object taint,
                           @CachedLibrary(limit = "3") InteropLibrary interop) {
    return SLString.addTaint(value.asString(), interop.isNull(taint) ? TAINT : taint);
  }
}
