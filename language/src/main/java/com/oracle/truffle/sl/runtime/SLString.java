package com.oracle.truffle.sl.runtime;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.sl.SLLanguage;

@ExportLibrary(InteropLibrary.class)
@SuppressWarnings("static-method")
public final class SLString implements TruffleObject {

  public final String string;
  public final boolean[] taint;

  public SLString(String string) {
    this(string, new boolean[string.length()]);
  }

  public SLString(String string, boolean[] taint) {
    assert string.length() == taint.length;
    this.string = string;
    this.taint = taint;
  }

  public SLString prepend(SLString pre) {
    return prepend(pre.string, pre.taint);
  }

  public SLString prepend(String pre) {
    return prepend(pre, new boolean[pre.length()]);
  }

  public SLString append(SLString post) {
    return append(post.string, post.taint);
  }

  public SLString append(String post) {
    return append(post, new boolean[post.length()]);
  }

  private SLString prepend(String pre, boolean[] preTaint) {
    assert pre.length() == preTaint.length;
    boolean[] taint = new boolean[this.taint.length + preTaint.length];
    System.arraycopy(this.taint, 0, taint, preTaint.length, this.taint.length);
    System.arraycopy(preTaint, 0, taint, 0, preTaint.length);
    String string = pre + this.string;
    return new SLString(string, taint);
  }

  private SLString append(String post, boolean[] postTaint) {
    assert post.length() == postTaint.length;
    boolean[] taint = new boolean[this.taint.length + postTaint.length];
    System.arraycopy(this.taint, 0, taint, 0, this.taint.length);
    System.arraycopy(postTaint, 0, taint, this.taint.length, postTaint.length);
    String string = this.string + post;
    return new SLString(string, taint);
  }

  // TODO which of these are necessary?
  @ExportMessage
  @TruffleBoundary
  Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
      return toString();
  }

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
    return toString();
  }

  @ExportMessage
  boolean hasLanguage() {
      return true;
  }

  @ExportMessage
  Class<? extends TruffleLanguage<?>> getLanguage() {
      return SLLanguage.class;
  }

  @Override
  @TruffleBoundary
  public String toString() {
    return string.toString();
  }

  // hashCode and equals mimic normal string behaviour
  @Override
  public int hashCode() {
    return string.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof SLString)) { return false; }
    return string.equals(((SLString) obj).string);
  }
  
}
