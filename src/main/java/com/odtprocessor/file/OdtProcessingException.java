package com.odtprocessor.file;

import java.nio.file.Path;

/**
 * Custom exception for ODT processing errors
 */
public class OdtProcessingException extends Exception {
  private final transient Path problematicFile;

  public OdtProcessingException(String message, Throwable cause, Path file) {
    super(message, cause);
    this.problematicFile = file;
  }

  public Path getProblematicFile() {
    return problematicFile;
  }
}