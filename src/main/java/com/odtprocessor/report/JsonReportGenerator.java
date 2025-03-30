package com.odtprocessor.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.odtprocessor.core.model.DocumentImports;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates JSON reports documenting ODT file import relationships.
 * Produces human-readable indented output with consistent field ordering.
 */
public class JsonReportGenerator implements ReportGenerator {
	// Configure Jackson for pretty-printed JSON
	private final ObjectMapper objectMapper = new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT);

	/**
	 * Generates a JSON report with document paths and their imports.
	 * @param imports List of document import relationships
	 * @param outputPath Target file path (.json extension recommended)
	 * @throws ReportGenerationException If file writing fails
	 */
	@Override
	public void generateReport(List<DocumentImports> imports, Path outputPath)
			throws ReportGenerationException {

		try {
			// Convert to List<Map> for Jackson serialization
			List<Map<String, Object>> reportData = imports.stream()
					.map(this::createEntry)
					.collect(Collectors.toList());

			objectMapper.writeValue(outputPath.toFile(), reportData);
		} catch (Exception e) {
			throw new ReportGenerationException(
					"JSON report generation failed", e, outputPath);
		}
	}

	/** Creates ordered map entry to maintain JSON field sequence */
	private Map<String, Object> createEntry(DocumentImports doc) {
		Map<String, Object> entry = new LinkedHashMap<>();  // Preserves insertion order
		entry.put("document", doc.getDocumentPath().toString());
		entry.put("imports", doc.getImportedFiles());
		return entry;
	}
}