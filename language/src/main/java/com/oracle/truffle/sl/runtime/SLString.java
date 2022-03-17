package com.oracle.truffle.sl.runtime;

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.sl.nodes.SLTypes;

/**
 * This class is used to propagate taint in {@link String}s in Simple Language.
 * Calls to {@link SLTaintBuiltin} return a {@link SLString} instead of {@link String.
 * {@link SLString}s pack a certain runtime overhead, as the taint information has to be propagated
 * using array copies, which are somewhat expensive.
 * <p>
 * For a newly created {@link SLString}, every Taint information is the same.
 * The Taint is shared between every slot of {@link SLString#taint}.
 * <p>
 * Every {@code value != SLNull.SINGLETON} inside {@link SLString#taint} is considered to be a valid Taint marker.
 * For example, the {@link SLString} {@code new SLString("foo", false)} is considered to be tainted.
 */
@ExportLibrary(InteropLibrary.class)
public final class SLString implements TruffleObject {

  /**
   * The underlying {@link String} value.
   */
  private final String value;

  /**
   * The taint of the {@link String}.
   */
  private final Object[] taint;

  /**
   * Creates a new taint tracking {@link String} with only {@link Boolean}s to indicate
   * whether a {@link char} is tainted or not.
   * @param value to be taint tracked
   */
  public SLString(String value) {
    this(value, SLNull.SINGLETON);
  }

  /**
   * Creates a new taint tracking {@link String} with custom Taint.
   * The passed taint will be propagated on concatenation.
   * @param value to be taint tracked
   * @param taint the taint
   */
  public SLString(String value, Object taint) {
    this.value = value;
    this.taint = new Object[value.length()];
    Arrays.fill(this.taint, taint);
  }

  /**
   * @return whether the instance is tainted
   */
  public boolean isTainted() {
    for (Object taint : this.taint) {
      if (!SLTypes.isSLNull(taint)) { return true; }
    }
    return false;
  }

  /**
   * Gets the taint of a specific char.
   * <p>
   * We only support retriving taint on per char basis, as Simple Language
   * does not support arrays.
   * @param index index of the char from which the taint should be retrieved
   * @return the whole taint of the instance
   */
  public Object getTaint(int index) {
    return taint[index];
  }

  /**
   * Removes all the taint of the instance.
   */
  public void removeTaint() {
    removeTaint(0, taint.length);
  }

  /**
   * Removes the taint within {@code to} - {@code from}.
   * @param from first taint to be removed
   * @param to first taint to not be removed
   */
  @TruffleBoundary
  public void removeTaint(int from, int to) {
    Arrays.fill(taint, from, to, SLNull.SINGLETON);
  }

  /**
   * Adds taint to all chars of this instance.
   * @param taint to be propagated
   */
  public void addTaint(Object taint) {
    addTaint(taint, 0, this.taint.length);
  }

  /**
   * Adds taint to this instance in the given range.
   * @param taint to be propagated
   * @param from first char to be tainted
   * @param to first char not to be tainted
   */
  public void addTaint(Object taint, int from, int to) {
    Arrays.fill(this.taint, from, to, taint);
  }

  /**
   * Appends one {@link SLString} to another one.
   * By doing so, it propagates the taint.
   * @param post to be appended
   * @return the concatenated {@link SLString}
   */
  @TruffleBoundary
  public SLString append(SLString post) {
    return append(post.value, post.taint);
  }

  private SLString append(String post, Object[] postTaint) {
    Object[] taint = new Object[this.taint.length + postTaint.length];
    // propagate taint to new instance
    System.arraycopy(this.taint, 0, taint, 0, this.taint.length);
    System.arraycopy(postTaint, 0, taint, this.taint.length, postTaint.length);

    String value = this.value + post;
    return new SLString(value, taint);
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

  // message exports to convert SLStrings to Strings
  
  @ExportMessage
  boolean hasMetaObject() {
      return true;
  }

  @ExportMessage
  Object getMetaObject() {
      return SLType.STRING;
  }

  @ExportMessage
  boolean isString() {
    return true;
  }

  @ExportMessage
  String asString() {
    return value;
  }
}
