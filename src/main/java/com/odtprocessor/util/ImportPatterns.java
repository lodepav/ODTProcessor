package com.odtprocessor.util;

import java.util.regex.Pattern;

/**
 * Centralizes regex patterns for detecting and modifying ODT import blocks.
 */
public class ImportPatterns {
	/**
	 * Pattern to match [import filename.odt] blocks. Case-insensitive.
	 * Captures the filename (must end with .odt extension).
	 */
	public static final Pattern IMPORT_BLOCK = Pattern.compile(
			"\\[import\\s+([^]]+\\.odt)\\s*]",
			Pattern.CASE_INSENSITIVE
	);

	/**
	 * Creates a pattern to match specific import blocks for replacement.
	 * @param fileName Exact filename to match (case-insensitive)
	 * @return Compiled regex pattern with proper escaping
	 */
	public static Pattern createReplacementPattern(String fileName) {
		return Pattern.compile(
				"\\[import\\s*" + Pattern.quote(fileName) + "\\s*]",
				Pattern.CASE_INSENSITIVE
		);
	}
}