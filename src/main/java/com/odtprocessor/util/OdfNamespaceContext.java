package com.odtprocessor.util;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

/**
 * Provides namespace mappings for ODF XML documents. Required for proper XPath
 * evaluation with OpenDocument format namespaces.
 */
class OdfNamespaceContext implements NamespaceContext {
	/**
	 * Returns namespace URIs for standard ODF prefixes.
	 * Supported prefixes: style, text, draw, office.
	 */
	public String getNamespaceURI(String prefix) {
		return switch (prefix) {
			// ODF 1.0 namespace definitions
			case "style" -> "urn:oasis:names:tc:opendocument:xmlns:style:1.0";
			case "text" -> "urn:oasis:names:tc:opendocument:xmlns:text:1.0";
			case "draw" -> "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0";
			case "office" -> "urn:oasis:names:tc:opendocument:xmlns:office:1.0";
			default -> XMLConstants.NULL_NS_URI;  // Unknown prefixes return no namespace
		};
	}

	// These methods are not required for XPath evaluation
	public String getPrefix(String uri) { return null; }
	public Iterator<String> getPrefixes(String uri) { return null; }
}