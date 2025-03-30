package com.odtprocessor.config;

import java.nio.file.Path;

/**
 * Holds application configuration parameters parsed from command-line arguments.
 * Includes directory paths, output format, command type, and filename replacements.
 */
public class AppConfig {
	private Path rootDirectory;
	private Path outputPath;
	private OutputFormat outputFormat = OutputFormat.JSON; // Default
	private CliCommand command;
	private String oldFileName;
	private String newFileName;

	public enum OutputFormat { JSON, CSV }
	public enum CliCommand { ANALYZE, REPLACE }

	// Getters and setters
	public Path getRootDirectory() { return rootDirectory; }
	public void setRootDirectory(Path rootDirectory) { this.rootDirectory = rootDirectory; }
	public Path getOutputPath() { return outputPath; }
	public void setOutputPath(Path outputPath) { this.outputPath = outputPath; }
	public OutputFormat getOutputFormat() { return outputFormat; }
	public void setOutputFormat(OutputFormat outputFormat) { this.outputFormat = outputFormat; }
	public CliCommand getCommand() { return command; }
	public void setCommand(CliCommand command) { this.command = command; }
	public String getOldFileName() { return oldFileName; }
	public void setOldFileName(String oldFileName) { this.oldFileName = oldFileName; }
	public String getNewFileName() { return newFileName; }
	public void setNewFileName(String newFileName) { this.newFileName = newFileName; }
}