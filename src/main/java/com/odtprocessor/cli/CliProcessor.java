package com.odtprocessor.cli;

import com.odtprocessor.config.AppConfig;
import com.odtprocessor.core.model.DocumentImports;
import com.odtprocessor.report.ReportGenerationException;
import com.odtprocessor.report.ReportGenerator;
import com.odtprocessor.report.ReportGeneratorFactory;
import com.odtprocessor.service.OdtProcessorService;
import com.odtprocessor.service.ProcessingException;

import java.util.List;
import java.util.Map;

/**
 * Executes CLI commands by coordinating document processing and report generation.
 * Translates configuration into concrete file operations.
 */
public class CliProcessor {
	private final OdtProcessorService processorService;

	/**
	 * @param processorService Configured document processor with dependencies
	 */
	public CliProcessor(OdtProcessorService processorService) {
		this.processorService = processorService;
	}

	/**
	 * Executes command specified in configuration.
	 * @param config Validated application parameters
	 * @throws ProcessingException For document processing failures
	 * @throws ReportGenerationException For report output failures
	 */
	public void process(AppConfig config) throws ProcessingException, ReportGenerationException {
		// Route to command-specific handlers
		switch (config.getCommand()) {
			case ANALYZE -> handleAnalysis(config);
			case REPLACE -> handleReplacement(config);
			default -> throw new IllegalArgumentException(
					"Unsupported command: " + config.getCommand());
		}
	}

	/** Handles document analysis and report generation workflow */
	private void handleAnalysis(AppConfig config) throws ProcessingException, ReportGenerationException {
		List<DocumentImports> results = processorService.analyzeDocuments(
				config.getRootDirectory()
		);

		ReportGenerator generator = ReportGeneratorFactory.create(
				config.getOutputFormat()
		);

		generator.generateReport(results, config.getOutputPath());
	}

	/** Handles import replacement workflow with validation */
	private void handleReplacement(AppConfig config) throws ProcessingException {
		// Require both filenames for replacement operation
		if (config.getOldFileName() == null || config.getNewFileName() == null) {
			throw new IllegalArgumentException("Replacement requires old and new file names");
		}

		// Single replacement pair - extend for bulk replacements
		processorService.updateImports(
				config.getRootDirectory(),
				Map.of(config.getOldFileName(), config.getNewFileName())
		);
	}
}