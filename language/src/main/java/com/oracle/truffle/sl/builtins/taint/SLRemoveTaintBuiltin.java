package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.util.SLToSLStringNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "removeTaint")
public abstract class SLRemoveTaintBuiltin extends SLBuiltinNode {

  /**
   * Removes all taint markers within the given range.
   * @param value possibly tainted {@link String}
   * @param from first taint marker to remove
   * @param to first taint marker not to remove
   */
  @Specialization(guards = "isString(value)")
  public Object removeTaint(Object value, SLBigNumber from, SLBigNumber to, @Cached SLToSLStringNode node) {
    SLString tainted = node.execute(value);
    if (SLTypes.isSLNull(from)) { tainted.removeTaint(); }
    else {
      int f = from.getValue().intValue();
      int t = to.getValue().intValue();
      node.execute(value).removeTaint(f, t);
    }
    return value;
  }

  protected boolean isString(Object value) {
    return value instanceof String || value instanceof SLString;
  }
}
