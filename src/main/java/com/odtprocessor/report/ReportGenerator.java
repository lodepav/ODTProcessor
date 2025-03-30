package com.odtprocessor.report;

import com.odtprocessor.core.model.DocumentImports;
import java.nio.file.Path;
import java.util.List;

/**
 * Defines the contract for generating reports documenting import relationships.
 * Implementations handle specific output formats like JSON or CSV.
 */
public interface ReportGenerator {
	/**
	 * Generates a report file from document import data.
	 * @param documentImports Processed document relationships to report
	 * @param outputPath      Target file path for generated report
	 * @throws ReportGenerationException If report creation fails due to I/O errors or invalid data
	 */
	void generateReport(List<DocumentImports> documentImports, Path outputPath)
			throws ReportGenerationException;
}