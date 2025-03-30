package com.odtprocessor.content;

/**
 * Custom exception for content processing errors
 */
public class ContentProcessingException extends RuntimeException {
  public ContentProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}