package com.odtprocessor.content;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

class OdtContentParserTest {
	private final OdtContentParser parser = new OdtContentParser();

	@Test
	void extractText_shouldIgnoreNonTextElements() throws Exception {
		Path testFile = Files.createTempFile("test", ".odt");
		try (OdfTextDocument doc = OdfTextDocument.newTextDocument()) {
			// Add empty frame element (non-text content)
			DrawFrameElement frame = doc.newParagraph().newDrawFrameElement();
			doc.getContentRoot().appendChild(frame);
			doc.save(testFile.toFile());
		}

		String content = parser.extractText(OdfTextDocument.loadDocument(testFile.toFile()), false);
		assertThat(content).isEmpty();
	}

	@Test
	void modifyContent_shouldUpdateTextNodes() throws Exception {
		try (OdfTextDocument doc = OdfTextDocument.newTextDocument()) {
			doc.addText("Original");

			parser.modifyContent(doc, text -> text.replace("Original", "Modified"), false);

			assertThat(doc.getContentDom().getElementsByTagName("text:p").item(0).getTextContent())
					.contains("Modified");
		}
	}

	@Test
	void testImportStructure() throws Exception {
		Path testFile = Paths.get("src/test/resources/testfiles/Templates/template_aa01.odt");
		OdfTextDocument doc = OdfTextDocument.loadDocument(testFile.toFile());
		String content = parser.extractText(doc, true);
		assertThat(content).contains("[import block_1a.odt]");
	}
}