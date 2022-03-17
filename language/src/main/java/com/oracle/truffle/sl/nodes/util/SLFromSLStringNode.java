package com.oracle.truffle.sl.nodes.util;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.runtime.SLString;

/**
 * {@link Node} which extracts a {@link String} from either a {@link String} 
 * or from a {@link SLString}.
 */
public abstract class SLFromSLStringNode extends Node {

  public abstract String execute(Object value);

  @Specialization
  protected String extractSLString(SLString value) {
    return value.toString();
  }

  @Specialization
  protected String extractString(String value) {
    return value;
  }
}
