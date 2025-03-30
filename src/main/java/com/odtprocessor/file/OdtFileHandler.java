package com.odtprocessor.file;

import com.odtprocessor.content.OdtContentParser;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import java.nio.file.Path;

/**
 * Manages ODT file operations including content extraction and modification.
 * Uses ODFDOM library for document manipulation while maintaining file validity.
 */
public class OdtFileHandler {
	// Shared parser for both extraction and modification
	private final OdtContentParser contentParser = new OdtContentParser();

	/**
	 * Extracts text content from ODT file, optionally filtering to import blocks.
	 * @param importsOnly When true, returns only [import ...] blocks
	 * @return Extracted text content
	 * @throws OdtProcessingException If file is corrupted or inaccessible
	 */
	public String extractTextContent(Path filePath, boolean importsOnly) throws OdtProcessingException {
		// Auto-close document with try-with-resources
		try (OdfTextDocument odtDoc = OdfTextDocument.loadDocument(filePath.toFile())) {
			return contentParser.extractText(odtDoc, importsOnly);
		} catch (Exception e) {
			throw new OdtProcessingException(
					"Failed to extract content from file: " + filePath,
					e,  // Preserve original exception
					filePath
			);
		}
	}

	/**
	 * Modifies ODT file content using provided replacement strategy.
	 * @param replacement Text transformation to apply
	 * @param importsOnly When true, only modifies import blocks
	 * @throws OdtProcessingException If changes can't be saved
	 */
	public void replaceTextContent(Path filePath, TextReplacement replacement, boolean importsOnly)
			throws OdtProcessingException {

		// Load-modify-save pattern with auto-closing
		try (OdfTextDocument odtDoc = OdfTextDocument.loadDocument(filePath.toFile())) {
			contentParser.modifyContent(odtDoc, replacement, importsOnly);
			odtDoc.save(filePath.toFile());  // Overwrite original file
		} catch (Exception e) {
			throw new OdtProcessingException(
					"Failed to update content in file: " + filePath,
					e,
					filePath
			);
		}
	}
}