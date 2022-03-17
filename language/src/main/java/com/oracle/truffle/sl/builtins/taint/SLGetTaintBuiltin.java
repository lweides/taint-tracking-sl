package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.nodes.util.SLToSLStringNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "getTaint")
public abstract class SLGetTaintBuiltin extends SLBuiltinNode {

  /**
   * Retrivies the taint marker of the given argument.
   * @param value possibly tainted {@link String}
   * @param index index of the char for which to retrieve the taint marker
   * @return the taint marker
   */
  @Specialization(guards = "isString(value)")
  public Object getTaint(Object value, SLBigNumber index, @Cached SLToSLStringNode node) {
    int i = index.getValue().intValue();
    return node.execute(value).getTaint(i);
  }

  protected boolean isString(Object value) {
    return value instanceof String || value instanceof SLString;
  }
}
