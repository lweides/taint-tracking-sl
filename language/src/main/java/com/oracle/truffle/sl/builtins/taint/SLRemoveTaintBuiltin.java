package com.oracle.truffle.sl.builtins.taint;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.SLException;
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
   */
  @Specialization
  public SLString removeTaintWithoutIndexConversion(SLString value, int from, int to) {
    return value.removeTaint(from, to);
  }

  /**
   * Removes all taint markers within the given range.
   * @param value possibly tainted {@link String}
   * @param from first taint marker to remove
   * @param to first taint marker not to remove
   */
  @Specialization(replaces = "removeTaintWithoutIndexConversion")
  public SLString removeTaint(SLString value, Object from, Object to,
                  @CachedLibrary(limit = "3") InteropLibrary fromLib,
                  @CachedLibrary(limit = "3") InteropLibrary toLib) {
    final int fromIndex = parseIntOrThrow(from, fromLib, "From index is not a number");
    final int toIndex = parseIntOrThrow(to, toLib, "To index is not a number");
    return removeTaintWithoutIndexConversion(value, fromIndex, toIndex);
  }

  private int parseIntOrThrow(Object index, InteropLibrary interop, String errorMessage) {
    if (interop.isNumber(index) && interop.fitsInInt(interop)) {
      try {
        return interop.asInt(index);
      } catch (UnsupportedMessageException e) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        throw CompilerDirectives.shouldNotReachHere("Interop contract violation: value claims to be an int but is not");
      }
    } else {
      CompilerDirectives.transferToInterpreterAndInvalidate();
      throw new SLException(errorMessage, this);
    }
  }

  @Specialization
  public String removeTaint(String value, SLBigNumber from, SLBigNumber to) {
    return value;
  }
}
