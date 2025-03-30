package com.odtprocessor.report;

import com.odtprocessor.core.model.DocumentImports;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class CsvReportGeneratorTest {
	@TempDir
	Path tempDir;

	private final CsvReportGenerator generator = new CsvReportGenerator();

	@Test
	void generateReport_shouldCreateValidCsv() throws Exception {
		// Setup test data
		List<DocumentImports> imports = List.of(
				new DocumentImports(Path.of("main.odt"), List.of("header.odt", "footer.odt")),
				new DocumentImports(Path.of("empty.odt"), List.of())
		);

		Path outputPath = tempDir.resolve("report.csv");

		// Execute
		generator.generateReport(imports, outputPath);

		// Verify
		try (CSVParser parser = CSVParser.parse(outputPath, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader())) {
			List<CSVRecord> records = parser.getRecords();

			assertThat(records)
					.hasSize(2)
					.allSatisfy(record -> {
						assertThat(record.get("Document")).isIn("main.odt", "empty.odt");
						assertThat(record.get("Imported Files")).isIn("header.odt", "footer.odt");
					});

			assertThat(records.stream().map(r -> r.get("Document")))
					.containsExactly("main.odt", "main.odt");
		}
	}

	@Test
	void generateReport_shouldCreateHeaderOnlyForEmptyInput() throws Exception {
		Path outputPath = tempDir.resolve("empty.csv");
		generator.generateReport(List.of(), outputPath);

		List<String> lines = Files.readAllLines(outputPath);
		assertThat(lines)
				.hasSize(1)
				.first().isEqualTo("Document,Imported Files");
	}
}