package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import static com.oracle.truffle.api.CompilerDirectives.shouldNotReachHere;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.InteropArray;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLStringLibrary;

@NodeInfo(shortName = "removeTaint")
public abstract class SLRemoveTaintBuiltin extends SLBuiltinNode {

  /**
   * Removes all taint markers within the given range.
   * @param value possibly tainted {@link String}
   * @param from first taint marker to remove
   * @param to first taint marker not to remove
   */
  @Specialization(guards = "valueLib.canBeTainted(value)")
  public InteropArray removeTaint(Object value, SLBigNumber from, SLBigNumber to,
                  @CachedLibrary(limit = "3") SLStringLibrary valueLib) {
    try {
      return new InteropArray(valueLib.removeTaint(value, from, to));
    } catch (UnsupportedMessageException e) {
      throw shouldNotReachHere();
    }
  }
}
