package com.oracle.truffle.sl.runtime;

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.sl.nodes.SLTypes;

/**
 * This class is used to propagate taint in {@link String}s in Simple Language.
 * Calls to {@link SLAddTaintBuiltin} return a {@link SLString} instead of {@link String}.
 * {@link SLString}s pack a certain runtime overhead, as the taint information has to be propagated
 * using array copies, which are somewhat expensive.
 * <p>
 * For a newly created {@link SLString}, every Taint information is the same.
 * The Taint is shared between every slot of {@link SLString#taint}.
 * <p>
 * Every {@code value != SLNull.SINGLETON} inside {@link SLString#taint} is considered to be a valid Taint marker.
 * For example, tainting with {@code false} is still considered to be tainted.
 */
@ExportLibrary(InteropLibrary.class)
@ExportLibrary(value = SLStringLibrary.class, priority = 1)
@SuppressWarnings("static-method")
public final class SLString implements TruffleObject {

  /**
   * Concatenates 2 stringlike Objects, propagating their taint.
   * @param left left side of the concatenation
   * @param right right side of the concatenation
   * @param taintLeft taint of the left side
   * @param taintRight taint of the right side
   * @return resulting {@link SLString}
   */
  @TruffleBoundary
  public static SLString concatenate(String left, String right, Object[] taintLeft, Object[] taintRight) {
    String value = left + right;
    Object[] taint = new Object[left.length() + right.length()];
    
    if (taintLeft != null) {
      System.arraycopy(taintLeft, 0, taint, 0, left.length());
    } else {
      Arrays.fill(taint, 0, left.length(), SLNull.SINGLETON);
    }

    if (taintRight != null) {
      System.arraycopy(taintRight, 0, taint, left.length(), right.length());
    } else {
      Arrays.fill(taint, left.length(), left.length() + right.length(), SLNull.SINGLETON);
    }

    return new SLString(value, taint);
  }

  /**
   * The underlying {@link String} value.
   */
  private final String value;

  /**
   * The taint of the {@link String}.
   */
  @CompilationFinal(dimensions = 1)
  private final Object[] taint;

  /**
   * Creates a new taint tracking {@link String} with no taint.
   * @param value to be taint tracked
   */
  public SLString(String value) {
    this.value = value;
    this.taint = taintAll(value.length(), SLNull.SINGLETON);
  }

  public SLString(String value, Object taint) {
    this.value = value;
    this.taint = taintAll(value.length(), taint);
  }

  private static Object[] taintAll(int stringLength, Object taintLabel) {
    final Object[] characterTaint = new Object[stringLength];
    Arrays.fill(characterTaint, taintLabel);
    return characterTaint;
  }

  /**
   * Creates a new taint tracking {@link String} with custom Taint.
   * The passed taint will be propagated on concatenation.
   * @param value to be taint tracked
   * @param taint the taint
   */
  private SLString(String value, Object[] taint) {
    this.value = value;
    this.taint = taint;
  }

  // message exports for SLStringLibrary

  @ExportMessage()
  String asString() {
    return value;
  }

  @ExportMessage
  static boolean isStringLike(SLString receiver) {
    return true;
  }

  @ExportMessage
  static boolean canBeTainted(SLString receiver) {
    return true;
  }

  @ExportMessage
  boolean isTainted() {
    for (Object taint : this.taint) {
      if (!SLTypes.isSLNull(taint)) { return true; }
    }
    return false;
  }

  @ExportMessage
  Object[] getTaint() {
    return taint;
  }
  
  @ExportMessage
  @TruffleBoundary
  Object[] removeTaint(SLBigNumber from, SLBigNumber to,
            @CachedLibrary(limit = "3") InteropLibrary fromLib,
            @CachedLibrary(limit = "3") InteropLibrary toLib) throws UnsupportedMessageException {
    int f = fromLib.asInt(from);
    int t = toLib.asInt(to);
    Object[] prevTaint = Arrays.copyOfRange(taint, f, t);
    Arrays.fill(taint, f, t, SLNull.SINGLETON);
    return prevTaint;
  }

  @ExportMessage
  @TruffleBoundary
  SLString addTaint(Object taint) {
    return new SLString(value, taintAll(value.length(), taint));
  }

  // message exports for InteropLibraray

  @ExportMessage
  static boolean isString(SLString receiver) {
    return true;
  }

  @Override
  public String toString() {
    return value;
  }

  /**
   * Implementation disregards the taint information when checking for equality.
   */
  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /**
   * Implementation disregards the taint information when checking for equality.
   * <p>
   * Comparing with other {@link String}s does however not work, as {@link String#equals(Object)}
   * only returns {@code true} for other {@link String}s.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof SLString)) { return false; }
    return value.equals(((SLString) obj).value);
  }
}
