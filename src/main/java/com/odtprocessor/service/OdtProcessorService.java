package com.odtprocessor.service;

import com.odtprocessor.content.ImportBlockReplacerFactory;
import com.odtprocessor.core.model.DocumentImports;
import com.odtprocessor.file.OdtFileFinder;
import com.odtprocessor.file.OdtFileHandler;
import com.odtprocessor.file.OdtProcessingException;
import com.odtprocessor.content.ImportBlockDetector;
import com.odtprocessor.file.TextReplacement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Coordinates ODT document processing workflow including analysis and modification of import blocks.
 */
public class OdtProcessorService {
	private static final Logger logger = LoggerFactory.getLogger(OdtProcessorService.class);

	private final OdtFileFinder fileFinder;
	private final OdtFileHandler fileHandler;
	private final ImportBlockDetector importDetector;
	private final ImportBlockReplacerFactory replacerFactory;

	/**
	 * @param fileFinder      Locates ODT files in directory structure
	 * @param fileHandler     Handles file content extraction and modification
	 * @param importDetector  Identifies import blocks in document content
	 * @param replacerFactory Creates text replacement strategies for imports
	 */
	public OdtProcessorService(OdtFileFinder fileFinder,
	                           OdtFileHandler fileHandler,
	                           ImportBlockDetector importDetector,
	                           ImportBlockReplacerFactory replacerFactory) {
		this.fileFinder = fileFinder;
		this.fileHandler = fileHandler;
		this.importDetector = importDetector;
		this.replacerFactory = replacerFactory;
	}

	/**
	 * Scans directory and analyzes import relationships between documents.
	 * @param rootDir Root directory to process
	 * @return List of documents with their import dependencies
	 * @throws ProcessingException For file system errors or processing failures
	 */
	public List<DocumentImports> analyzeDocuments(Path rootDir) throws ProcessingException {
		try {
			List<DocumentImports> results = new ArrayList<>();
			List<Path> odtFiles = fileFinder.findOdtFiles(rootDir);

			for (Path file : odtFiles) {
				processDocument(file, results);
			}

			return results;
		} catch (IOException e) {
			throw new ProcessingException("Failed to scan directory: " + rootDir, e);
		}
	}

	private void processDocument(Path file, List<DocumentImports> results) throws ProcessingException {
		try {
			String content = fileHandler.extractTextContent(file, true);
			List<String> imports = importDetector.detectImports(content);
			results.add(new DocumentImports(file, imports));
		} catch (OdtProcessingException e) {
			handleProcessingError(file, e);
		}
	}

	/**
	 * Updates import references across all documents in directory.
	 * @param rootDir      Root directory to process
	 * @param replacements Map of old to new filenames to replace
	 * @throws ProcessingException For invalid input or processing failures
	 */
	public void updateImports(Path rootDir, Map<String, String> replacements) throws ProcessingException {
		if (replacements == null || replacements.isEmpty()) {
			throw new IllegalArgumentException("Replacements map cannot be empty");
		}

		try {
			List<Path> odtFiles = fileFinder.findOdtFiles(rootDir);
			for (Path file : odtFiles) {
				processImportReplacement(file, replacements);
			}
		} catch (Exception e) {
			throw new ProcessingException("Import replacement failed", e);
		}
	}

	private void processImportReplacement(Path file, Map<String, String> replacements) throws ProcessingException {
		TextReplacement replacement = replacerFactory.createReplacer(replacements);
		try {
			fileHandler.replaceTextContent(file, replacement, true);
		} catch (OdtProcessingException e) {
			handleProcessingError(file, e);
		}
	}

	/** Centralizes error handling and logging for document processing failures */
	private void handleProcessingError(Path file, OdtProcessingException e)
			throws ProcessingException {
		logger.error("Error processing file {}: {}", file, e.getMessage());
		throw new ProcessingException("Failed to process file: " + file, e);
	}
}