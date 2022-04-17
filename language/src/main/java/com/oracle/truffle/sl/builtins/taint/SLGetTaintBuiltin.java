package com.oracle.truffle.sl.builtins.taint;

import java.util.Arrays;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.InteropArray;
import com.oracle.truffle.sl.runtime.SLNull;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "getTaint")
public abstract class SLGetTaintBuiltin extends SLBuiltinNode {

  /**
   * Retrivies the taint marker of the given argument.
   * @param value possibly tainted {@link String}
   * @return the taint marker
   */
  @Specialization
  public InteropArray getTaint(String value) {
    Object[] taintArr = new Object[value.length()];
    Arrays.fill(taintArr, SLNull.SINGLETON);
    return new InteropArray(taintArr);
  }

  @Specialization
  public InteropArray getTaint(SLString value) {
    return new InteropArray(value.getTaint());
  }
}
