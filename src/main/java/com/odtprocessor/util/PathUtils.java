package com.odtprocessor.util;

import java.nio.file.Path;

/**
 * Utility class for path validation operations.
 */
public class PathUtils {
	/**
	 * Validates that a path exists and is a directory.
	 *
	 * @param path Path to validate
	 * @throws IllegalArgumentException if path doesn't exist or isn't a directory
	 */
	public static void validateDirectory(Path path) {
		if (!path.toFile().exists()) {
			throw new IllegalArgumentException("Directory does not exist: " + path);
		}
		if (!path.toFile().isDirectory()) {
			throw new IllegalArgumentException("Path is not a directory: " + path);
		}
	}
}