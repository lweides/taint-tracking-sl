package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.InteropArray;
import com.oracle.truffle.sl.runtime.SLStringLibrary;

@NodeInfo(shortName = "getTaint")
public abstract class SLGetTaintBuiltin extends SLBuiltinNode {

  /**
   * Retrivies the taint marker of the given argument.
   * @param value possibly tainted {@link String}
   * @return the taint marker
   */
  @Specialization(guards = "valueLib.canBeTainted(value)")
  public InteropArray getTaint(Object value,
                        @CachedLibrary(limit = "3") SLStringLibrary valueLib) {
    Object[] taint = valueLib.getTaint(value);
    if (taint == null) { return new InteropArray(new Object[valueLib.asString(value).length()]); }
    return new InteropArray(taint);
  }
}
