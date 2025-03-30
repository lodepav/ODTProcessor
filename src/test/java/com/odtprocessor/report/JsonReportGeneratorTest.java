package com.odtprocessor.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odtprocessor.core.model.DocumentImports;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class JsonReportGeneratorTest {
	@TempDir
	Path tempDir;

	private final JsonReportGenerator generator = new JsonReportGenerator();

	@Test
	void generateReport_shouldCreateValidJsonStructure() throws Exception {
		// Setup test data
		List<DocumentImports> imports = List.of(
				new DocumentImports(Path.of("doc1.odt"), List.of("header.odt", "footer.odt")),
				new DocumentImports(Path.of("empty.odt"), List.of())
		);

		Path outputPath = tempDir.resolve("report.json");

		// Execute
		generator.generateReport(imports, outputPath);

		// Verify
		List<Map<String, Object>> reportData = new ObjectMapper().readValue(
				outputPath.toFile(),
				new TypeReference<>() {}
		);

		assertThat(reportData)
				.hasSize(2)
				.anySatisfy(entry -> {
					assertThat(entry)
							.hasEntrySatisfying("document",
									doc -> assertThat(doc).isEqualTo("doc1.odt"))
							.hasEntrySatisfying("imports",
									importFiles -> assertThat((List<String>) importFiles)
											.containsExactly("header.odt", "footer.odt"));
				})
				.anySatisfy(entry -> {
					assertThat(entry).containsEntry("document", "empty.odt");
					assertThat((List<?>) entry.get("imports")).isEmpty();
				});
	}

	@Test
	void generateReport_shouldHandleEmptyInput() throws Exception {
		Path outputPath = tempDir.resolve("empty.json");
		generator.generateReport(List.of(), outputPath);

		List<?> content = new ObjectMapper().readValue(outputPath.toFile(), List.class);
		assertThat(content).isEmpty();
	}
}