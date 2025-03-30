package com.odtprocessor.content;

import com.odtprocessor.util.ImportPatterns;
import java.util.regex.Pattern;

/**
 * Safely replaces specific import block patterns while validating filenames.
 * Uses precompiled regex patterns for efficient replacement operations.
 */
public class ImportBlockReplacer {
	// Precompiled pattern for performance in repeated replacements
	private final Pattern targetPattern;
	// Final replacement string with validated filename
	private final String replacement;

	/**
	 * Creates a replacer for specific file import patterns.
	 * @param oldFileName Existing import filename to match (must be .odt)
	 * @param newFileName Replacement filename to use (must be .odt)
	 */
	public ImportBlockReplacer(String oldFileName, String newFileName) {
		validateFileName(oldFileName);
		validateFileName(newFileName);

		// Uses pattern quoting to avoid regex injection
		this.targetPattern = ImportPatterns.createReplacementPattern(oldFileName);
		this.replacement = "[import " + newFileName + "]";  // Maintains import syntax
	}

	/**
	 * Replaces all matching import blocks in content.
	 * @param content Original document text
	 * @return Modified content with updated imports
	 */
	public String replaceImports(String content) {
		return targetPattern.matcher(content).replaceAll(replacement);
	}

	/** Validates filename format (non-empty, .odt extension) */
	private void validateFileName(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			throw new IllegalArgumentException("Invalid file name");
		}
		// Case-insensitive extension check
		if (!fileName.toLowerCase().endsWith(".odt")) {
			throw new IllegalArgumentException("File must be .odt");
		}
	}
}