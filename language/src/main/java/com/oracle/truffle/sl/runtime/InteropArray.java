package com.oracle.truffle.sl.runtime;

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

/**
 * Small implementation of arrays in Simple Language, used to verify the taint markers.
 */
@ExportLibrary(InteropLibrary.class)
@SuppressWarnings("static-method")
public class InteropArray implements TruffleObject {

  @CompilationFinal(dimensions = 1)
  private final Object[] values;

  public InteropArray(Object[] values) {
    this.values = values;
  }

  @ExportMessage
  static boolean hasArrayElements(InteropArray receiver) {
    return true;
  }

  @ExportMessage
  Object readArrayElement(long index) {
    return values[(int) index];
  }

  @ExportMessage
  long getArraySize() {
    return values.length;
  }

  @ExportMessage
  boolean isArrayElementReadable(long index) {
    return Long.compareUnsigned(index, values.length) < 0;
  }

  @ExportMessage
  @TruffleBoundary
  String toDisplayString(boolean allowSideEffects) {
    return Arrays.toString(values);
  }
}
