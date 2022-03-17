package com.oracle.truffle.sl.nodes.util;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.runtime.SLString;

/**
 * {@link Node} which translated either a {@link String} or a {@link SLString} into
 * a {@link SLString}.
 */
public abstract class SLToSLStringNode extends Node {

  public abstract SLString execute(Object value);

  @Specialization
  protected SLString translateString(String value) {
    return new SLString(value);
  }

  @Specialization
  protected SLString translateSLString(SLString value) {
    return value;
  }

  @Specialization(replaces = "translateString", guards = "!isSLString(value)")
  @TruffleBoundary
  protected SLString translateObject(Object value) {
    return new SLString(value.toString());
  }

  protected boolean isSLString(Object value) {
    return value instanceof SLString;
  }
}
