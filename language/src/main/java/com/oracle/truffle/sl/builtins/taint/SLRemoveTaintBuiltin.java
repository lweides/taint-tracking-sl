package com.oracle.truffle.sl.builtins.taint;

import static com.oracle.truffle.api.CompilerDirectives.shouldNotReachHere;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLString;

@NodeInfo(shortName = "removeTaint")
public abstract class SLRemoveTaintBuiltin extends SLBuiltinNode {

  /**
   * Removes all taint markers within the given range.
   * @param value possibly tainted {@link String}
   * @param from first taint marker to remove
   * @param to first taint marker not to remove
   * @throws UnsupportedMessageException
   */
  @Specialization
  public SLString removeTaint(SLString value, SLBigNumber from, SLBigNumber to,
                  @CachedLibrary(limit = "3") InteropLibrary fromLib,
                  @CachedLibrary(limit = "3") InteropLibrary toLib) {
    try {
      return value.removeTaint(from, to, fromLib, toLib);
    } catch (UnsupportedMessageException e) {
      throw shouldNotReachHere(e);
    }
  }

  @Specialization
  public String removeTaint(String value, SLBigNumber from, SLBigNumber to) {
    return value;
  }
}
