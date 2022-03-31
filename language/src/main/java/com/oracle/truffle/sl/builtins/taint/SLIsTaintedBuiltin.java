package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.SLStringLibrary;

@NodeInfo(shortName = "isTainted")
public abstract class SLIsTaintedBuiltin extends SLBuiltinNode {

  /**
   * Returns whether the argument passed is tainted.
   * {@see SLString#isTainted()}
   * @param value to be checked
   * @return {@code true} if the argument is tainted
   */
  @Specialization(guards = "valueLib.canBeTainted(value)")
  public boolean isTainted(Object value,
                  @CachedLibrary(limit = "3") SLStringLibrary valueLib) {
    return valueLib.isTainted(value);
  }
}
