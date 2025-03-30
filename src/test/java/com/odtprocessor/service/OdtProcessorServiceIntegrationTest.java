package com.odtprocessor.service;

import com.odtprocessor.content.ImportBlockDetector;
import com.odtprocessor.content.ImportBlockReplacerFactory;
import com.odtprocessor.core.model.DocumentImports;
import com.odtprocessor.file.OdtFileFinder;
import com.odtprocessor.file.OdtFileHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OdtProcessorServiceIntegrationTest {
	private OdtProcessorService processor;
	private final OdtFileHandler handler = new OdtFileHandler();
	private Path testFilesDir;
	private Path sourceDir;

	@TempDir
	static Path tempDir;

	@BeforeAll
	void setup() throws Exception {
		// Copy original templates to temp directory
		sourceDir = Paths.get("src/test/resources/testfiles/Templates");
		copyDirectory(sourceDir, tempDir);

		// Initialize processor with temp directory
		testFilesDir = tempDir;
		// Initialize service with real components
		processor = new OdtProcessorService(
				new OdtFileFinder(),
				new OdtFileHandler(),
				new ImportBlockDetector(),
				new ImportBlockReplacerFactory()
		);
	}

	@Test
	void analyzeDocuments_shouldFindAllImports() throws Exception {
		List<DocumentImports> results = processor.analyzeDocuments(sourceDir);

		assertThat(results)
				.filteredOn(d -> d.getDocumentPath().endsWith("template_aa01.odt"))
				.flatExtracting(DocumentImports::getImportedFiles)
				.containsExactly("block_1.odt", "block_1a.odt");
	}

	@Test
	void replaceImports_shouldUpdateFiles() throws Exception {
		// Replace block_1.odt with new_block.odt
		processor.updateImports(testFilesDir, Map.of(
				"block_1.odt", "new_block.odt"
		));

		// Verify template_aa01.odt
		Path templatePath = testFilesDir.resolve("template_aa01.odt");
		String content = handler.extractTextContent(templatePath, true);
		assertThat(content)
				.contains("[import new_block.odt]")
				.doesNotContain("[import block_1.odt]");
	}

	private void copyDirectory(Path source, Path target) throws IOException {
		try (Stream<Path> pathStream = Files.walk(source)) {
			pathStream.forEach(sourcePath -> {
				try {
					Path destPath = target.resolve(source.relativize(sourcePath));
					if (Files.isDirectory(sourcePath)) {
						Files.createDirectories(destPath);
					} else {
						Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					throw new UncheckedIOException("Failed to copy: " + sourcePath, e);
				}
			});
		} catch (UncheckedIOException e) {
			throw e.getCause();
		}
	}
}