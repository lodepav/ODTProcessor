package com.odtprocessor.file;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.*;

class OdtFileHandlerTest {
	private static Path validOdtPath;
	private static String originalContent;
	private final OdtFileHandler handler = new OdtFileHandler();

	@BeforeAll
	static void setup() throws Exception {
		// Create a test ODT file with known content
		validOdtPath = Files.createTempFile("test", ".odt");
		try (OdfTextDocument doc = OdfTextDocument.newTextDocument()) {
			doc.addText("Test content");
			doc.save(validOdtPath.toFile());
			originalContent = "Test content";

			// Verify file can be loaded
			assertThatCode(() -> OdfTextDocument.loadDocument(validOdtPath.toFile()))
					.doesNotThrowAnyException();
		}
	}

	@Test
	void extractTextContent_shouldReturnTextFromValidFile() throws Exception {
		String content = handler.extractTextContent(validOdtPath, false);
		assertThat(content).contains(originalContent);
	}

	@Test
	void extractTextContent_shouldThrowForInvalidFile(@TempDir Path tempDir) {
		Path invalidPath = tempDir.resolve("nonexistent.odt");
		assertThatExceptionOfType(OdtProcessingException.class)
				.isThrownBy(() -> handler.extractTextContent(invalidPath, false))
				.withMessageContaining("Failed to extract content");
	}

	@Test
	void replaceTextContent_shouldModifyFile(@TempDir Path tempDir) throws Exception {
		Path testFile = tempDir.resolve("test.odt");
		Files.copy(validOdtPath, testFile);

		handler.replaceTextContent(testFile, text -> text.replace("content", "modified"), false);

		String modifiedContent = handler.extractTextContent(testFile, false);
		assertThat(modifiedContent).contains("Test modified");
	}
}