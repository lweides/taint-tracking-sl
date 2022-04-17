package com.oracle.truffle.sl.runtime;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.library.GenerateLibrary;
import com.oracle.truffle.api.library.Library;
import com.oracle.truffle.api.library.GenerateLibrary.DefaultExport;

/**
 * Library which provides methods to interact with {@link String}s in Simple Language.
 * For a sample implementation, see {@link SLString}.
 */
@GenerateLibrary
@DefaultExport(SLStringLibrary.DefaultStringExports.class)
@DefaultExport(SLStringLibrary.DefaultObjectExports.class)
public abstract class SLStringLibrary extends Library {
  
  /**
   * Transforms the {@code receiver} into its {@link String} represenation.
   * @param receiver to be transformed
   * @return {@link String} representation
   */
  public abstract String asString(Object receiver);

  /**
   * Checks whether the {@code receiver} can be concatonated with any other
   * stringlike entity. This is not the case for a general {@link Object}.
   * @param receiver to be concatonated
   * @return whether the {@code receiver} can be concatonated
   */
  public abstract boolean isStringLike(Object receiver);

  /**
   * Checks if the {@code receiver} is tainted.
   * Has to return {@code false} if {@link SLStringLibrary#canBeTainted(Object)} returns {@code false}.
   * @param receiver to be tested
   * @return whether the {@code receiver} is tainted
   */
  public abstract boolean isTainted(Object receiver);

  /**
   * Retrives the taint of the {@link receiver}.
   * This method may return {@code null} if the {@code receiver} is not tainted.
   * This method may also return an array of size {@code receiver.toSTring().length()},
   * filled with {@link SLNull#SINGLETON} if the {@code receiver} is not tainted.
   * If {@link SLStringLibrary#canBeTainted(Object)} returns {@code false}, this method
   * if required to return {@code null}.
   * @param receiver some possibly tainted stringlike entity
   * @return the taint of the {@code receiver}
   */
  public abstract Object[] getTaint(Object receiver);

  @ExportLibrary(value = SLStringLibrary.class, receiverType = Object.class)
  @SuppressWarnings("static-method")
  static class DefaultObjectExports {
    
    @ExportMessage
    @TruffleBoundary
    static String asString(Object receiver) {
      return receiver.toString();
    }
    
    @ExportMessage
    static boolean isStringLike(Object receiver) {
      return false;
    }

    @ExportMessage
    static boolean isTainted(Object receiver) {
      return false;
    }

    @ExportMessage
    static Object[] getTaint(Object receiver) {
      return null;
    }
  }

  @ExportLibrary(value = SLStringLibrary.class, receiverType = String.class, priority = 1)
  @SuppressWarnings("static-method")
  static class DefaultStringExports {

    @ExportMessage
    @TruffleBoundary
    static String asString(String receiver) {
      return receiver;
    }
    
    @ExportMessage
    static boolean isStringLike(String receiver) {
      return true;
    }

    @ExportMessage
    static boolean isTainted(String receiver) {
      return false;
    }

    @ExportMessage
    static Object[] getTaint(String receiver) {
      return null;
    }
  }
}
