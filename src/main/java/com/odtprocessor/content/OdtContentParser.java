package com.odtprocessor.content;

import com.odtprocessor.file.TextReplacement;
import com.odtprocessor.util.OdtXmlUtils;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * Parses and modifies ODT document content using XPath queries.
 * Handles both full text extraction and targeted import block processing.
 */
public class OdtContentParser {

	/**
	 * Extracts text content from document DOM.
	 * @param importsOnly When true, returns only import block content
	 * @return Concatenated text from matching nodes
	 * @throws ContentProcessingException For XML parsing or IO errors
	 */
	public String extractText(OdfTextDocument doc, boolean importsOnly) {
		try {
			// Select appropriate XPath based on processing mode
			String xpath = importsOnly
					? OdtXmlUtils.IMPORT_BLOCKS_XPATH
					: OdtXmlUtils.ALL_TEXT_XPATH;

			NodeList nodes = OdtXmlUtils.findNodes(doc.getContentDom(), xpath);
			return OdtXmlUtils.concatenateText(nodes);
		} catch (XPathExpressionException | SAXException | IOException e) {
			throw new ContentProcessingException("Text extraction failed", e);
		}
	}

	/**
	 * Modifies document content through node value replacement.
	 * @param replacement Transformation to apply to matching text nodes
	 * @param importsOnly When true, only modifies import blocks
	 * @throws ContentProcessingException For XML processing failures
	 */
	public void modifyContent(OdfTextDocument doc,
	                          TextReplacement replacement,
	                          boolean importsOnly) {
		try {
			String xpath = importsOnly
					? OdtXmlUtils.IMPORT_BLOCKS_XPATH
					: OdtXmlUtils.ALL_TEXT_XPATH;

			NodeList nodes = OdtXmlUtils.findNodes(doc.getContentDom(), xpath);
			OdtXmlUtils.processNodes(nodes, node -> {
				String original = node.getNodeValue();
				String modified = replacement.apply(original);
				// Only update DOM if content changed
				if (!modified.equals(original)) {
					node.setNodeValue(modified);
				}
			});
		} catch (XPathExpressionException | SAXException | IOException e) {
			throw new ContentProcessingException("Content modification failed", e);
		}
	}
}