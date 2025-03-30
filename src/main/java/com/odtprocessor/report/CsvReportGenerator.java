package com.odtprocessor.report;

import com.odtprocessor.core.model.DocumentImports;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Generates CSV reports showing document import relationships.
 * Creates one row per import relationship with consistent column ordering.
 */
public class CsvReportGenerator implements ReportGenerator {
	// CSV column headers
	private static final String[] HEADERS = {"Document", "Imported Files"};

	/**
	 * Generates CSV report with one row per import reference.
	 * @param imports List of document import relationships
	 * @param outputPath Target file path (.csv extension recommended)
	 * @throws ReportGenerationException If file writing fails
	 */
	@Override
	public void generateReport(List<DocumentImports> imports, Path outputPath)
			throws ReportGenerationException {

		// Use try-with-resources to ensure proper stream closure
		try (BufferedWriter writer = Files.newBufferedWriter(outputPath);
		     CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
				     .withHeader(HEADERS))) {

			// Process each document's imports
			for (DocumentImports doc : imports) {
				printDocumentImports(csvPrinter, doc);
			}

		} catch (Exception e) {
			throw new ReportGenerationException(
					"CSV report generation failed", e, outputPath);
		}
	}

	/** Writes one CSV row per imported file to show individual relationships */
	private void printDocumentImports(CSVPrinter printer, DocumentImports doc)
			throws IOException {

		String documentPath = doc.getDocumentPath().toString();
		// Create separate row for each imported file
		for (String imported : doc.getImportedFiles()) {
			printer.printRecord(documentPath, imported);
		}
	}
}