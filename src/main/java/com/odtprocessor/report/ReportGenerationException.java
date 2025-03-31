package com.odtprocessor.report;

import java.nio.file.Path;

/**
 * Indicates an error occurred during report generation. Contains information
 * about the target output file path where generation failed.
 */
public class ReportGenerationException extends Exception {
  private final transient Path outputPath;

  /**
   * @param message Description of the failure
   * @param cause Underlying exception that triggered the failure
   * @param path Output path that could not be generated
   */
  public ReportGenerationException(String message, Throwable cause, Path path) {
    super(message, cause);
    this.outputPath = path;
  }

  /** @return The output path where report generation was attempted */
  public Path getOutputPath() {
    return outputPath;
  }
}