package com.odtprocessor.content;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ImportBlockDetectorTest {
	private final ImportBlockDetector detector = new ImportBlockDetector();

	@Test
	void detectImports_shouldFindValidImports() {
		String content = "[import file1.odt] Text [IMPORT  subdir/file2.odt  ]";
		List<String> result = detector.detectImports(content);

		assertThat(result)
				.containsExactly("file1.odt", "subdir/file2.odt");
	}

	@Test
	void detectImports_shouldIgnoreInvalidPatterns() {
		String content = "[import] [import file.txt] [import invalid#name.odt]";
		List<String> result = detector.detectImports(content);

		assertThat(result).isEmpty();
	}

	@Test
	void detectImports_shouldHandleMultipleLines() {
		String content = "Header\n[import header.odt]\nFooter\n[import footer.odt]";
		List<String> result = detector.detectImports(content);

		assertThat(result)
				.containsExactly("header.odt", "footer.odt");
	}

	@Test
	void detectImports_shouldHandleSpecialCharacters() {
		String content = "[import file_with_underscore.odt] [import file-with-dash.odt]";
		List<String> result = detector.detectImports(content);

		assertThat(result)
				.containsExactly("file_with_underscore.odt", "file-with-dash.odt");
	}
}