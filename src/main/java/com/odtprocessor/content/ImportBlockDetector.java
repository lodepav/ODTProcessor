package com.odtprocessor.content;

import com.odtprocessor.util.ImportPatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Identifies valid import blocks in document content and extracts filenames.
 * Validates both pattern syntax and filename security constraints.
 */
public class ImportBlockDetector {
	// Precompiled pattern for [import filename.odt] detection
	private static final Pattern IMPORT_PATTERN = ImportPatterns.IMPORT_BLOCK;

	/**
	 * Scans content for valid import declarations.
	 * @param content Text to analyze (typically from ODT document)
	 * @return List of validated import filenames (.odt extension required)
	 */
	public List<String> detectImports(String content) {
		List<String> imports = new ArrayList<>();
		Matcher matcher = IMPORT_PATTERN.matcher(content);

		// Iterate through all pattern matches
		while (matcher.find()) {
			String fileName = matcher.group(1).trim();
			if (isValidFileName(fileName)) {
				imports.add(fileName);
			}
		}
		return imports;
	}

	/**
	 * Validates filename format and security constraints:
	 * - Only alphanumeric, hyphen, underscore, dot and slash characters
	 * - Must end with .odt extension (case-insensitive)
	 */
	private boolean isValidFileName(String fileName) {
		return fileName.matches("^[\\w\\-./]+$") &&  // Safe character set
				fileName.toLowerCase().endsWith(".odt");  // Extension validation
	}
}