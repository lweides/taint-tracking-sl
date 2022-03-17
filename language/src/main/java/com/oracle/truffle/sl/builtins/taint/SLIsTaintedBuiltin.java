package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.nodes.util.SLToSLStringNode;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "isTainted")
public abstract class SLIsTaintedBuiltin extends SLBuiltinNode {

  /**
   * Returns whether the argument passed is tainted.
   * {@see SLString#isTainted()}
   * @param value to be checked
   * @return {@code true} if the argument is tainted
   */
  @Specialization(guards = "isString(value)")
  public boolean isTainted(Object value, @Cached SLToSLStringNode node) {
    return node.execute(value).isTainted();
  }

  protected boolean isString(Object value) {
    return value instanceof String || value instanceof SLString;
  }
}
