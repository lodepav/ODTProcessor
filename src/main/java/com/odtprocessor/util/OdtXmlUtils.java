package com.odtprocessor.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.*;

/**
 * XML utilities for ODT document processing. Handles XPath queries and text extraction.
 */
public class OdtXmlUtils {
	// Precompiled XPath instance with ODF namespace context
	private static final XPath xpath;

	/** XPath to find all text nodes in document */
	public static final String ALL_TEXT_XPATH = "//text()";

	/** XPath to locate import blocks in ODF format */
	public static final String IMPORT_BLOCKS_XPATH =
			"//text:text-input[@text:description='import']/text()";

	static {
		// Initialize XPath with ODF namespace mappings
		XPathFactory xpathFactory = XPathFactory.newInstance();
		xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new OdfNamespaceContext());
	}

	/**
	 * Finds nodes matching XPath expression.
	 * @param context Root node for search
	 * @param xpathExpr XPath 1.0 expression
	 * @return Matching nodes
	 */
	public static NodeList findNodes(Node context, String xpathExpr) throws XPathExpressionException {
		return (NodeList) xpath.evaluate(xpathExpr, context, XPathConstants.NODESET);
	}

	/** Concatenates text content from nodes, skipping empty values */
	public static String concatenateText(NodeList nodes) {
		StringBuilder sb = new StringBuilder();
		processNodes(nodes, node -> {
			String text = node.getNodeValue().trim();
			if (!text.isEmpty()) {
				sb.append(text).append(" ");
			}
		});
		return sb.toString().trim();  // Remove trailing space
	}

	/** Processes each node in list with provided handler */
	public static void processNodes(NodeList nodes, NodeProcessor processor) {
		for (int i = 0; i < nodes.getLength(); i++) {
			processor.process(nodes.item(i));
		}
	}

	/** Functional interface for node processing */
	public interface NodeProcessor {
		void process(Node node);
	}
}