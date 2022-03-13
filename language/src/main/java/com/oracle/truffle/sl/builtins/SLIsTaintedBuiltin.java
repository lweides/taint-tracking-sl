package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "isTainted")
public abstract class SLIsTaintedBuiltin extends SLBuiltinNode {

  @Specialization()
  public boolean isTainted(SLString value) {
    for (boolean t : value.taint) {
      if (t) { return true; }
    }
    return false;
  }
}
