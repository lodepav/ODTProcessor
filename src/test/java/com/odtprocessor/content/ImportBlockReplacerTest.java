package com.odtprocessor.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.*;

class ImportBlockReplacerTest {

	@Test
	void replaceImports_shouldReplaceExactMatches() {
		ImportBlockReplacer replacer = new ImportBlockReplacer("old.odt", "new.odt");
		String result = replacer.replaceImports("Text [import old.odt] more text");

		assertThat(result).isEqualTo("Text [import new.odt] more text");
	}

	@ParameterizedTest
	@CsvSource({
			"[import OLD.odt], [import new.odt]", // Case insensitivity
			"[import  old.odt  ], [import new.odt]", // Extra spaces
			"prefix[import old.odt]suffix, prefix[import new.odt]suffix" // Embedded match
	})
	void replaceImports_shouldHandleVariations(String input, String expected) {
		ImportBlockReplacer replacer = new ImportBlockReplacer("old.odt", "new.odt");
		assertThat(replacer.replaceImports(input)).isEqualTo(expected);
	}

	@Test
	void replaceImports_shouldNotAffectOtherImports() {
		ImportBlockReplacer replacer = new ImportBlockReplacer("specific.odt", "replaced.odt");
		String content = "[import specific.odt] [import another.odt]";

		String result = replacer.replaceImports(content);
		assertThat(result).isEqualTo("[import replaced.odt] [import another.odt]");
	}

	@Test
	void replaceImports_shouldHandleMultipleReplacements() {
		ImportBlockReplacer replacer = new ImportBlockReplacer("old.odt", "new.odt");
		String content = "[import old.odt] [import old.odt]";

		String result = replacer.replaceImports(content);
		assertThat(result).isEqualTo("[import new.odt] [import new.odt]");
	}

	@Test
	void constructor_shouldValidateFilenames() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new ImportBlockReplacer("invalid.txt", "new.odt"))
				.withMessageContaining("File must be .odt");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> new ImportBlockReplacer("old.odt", "invalid"))
				.withMessageContaining("File must be .odt");
	}

	@Test
	void replaceImports_shouldHandleSpecialCharacters() {
		ImportBlockReplacer replacer = new ImportBlockReplacer("file[1].odt", "file_1.odt");
		String content = "[import file[1].odt]";

		String result = replacer.replaceImports(content);
		assertThat(result).isEqualTo("[import file_1.odt]");
	}

	@Test
	void replaceImports_shouldNotModifyNonMatchingText() {
		ImportBlockReplacer replacer = new ImportBlockReplacer("old.odt", "new.odt");
		String content = "Regular text [import other.odt]";

		String result = replacer.replaceImports(content);
		assertThat(result).isEqualTo(content);
	}
}