package com.oracle.truffle.sl.builtins;

import java.util.Arrays;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "taint")
public abstract class SLTaintBuiltin extends SLBuiltinNode {

  @Specialization
  public SLString taint(SLString value) {
    Arrays.fill(value.taint, true);
    return value;
  }
}
